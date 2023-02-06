package com.loam.stoody.service.communication.file;

import com.loam.stoody.dto.api.request.file.UserFileRequestDTO;
import com.loam.stoody.dto.api.response.OutdoorResponse;
import com.loam.stoody.enums.IndoorResponse;
import com.loam.stoody.global.constants.ProjectConfigurationVariables;
import com.loam.stoody.model.communication.file.UserFile;
import com.loam.stoody.repository.communication.file.UserFileRepository;
import com.loam.stoody.repository.user.UserRepository;
import com.loam.stoody.service.user.CustomUserDetailsService;
import com.loam.stoody.service.utils.aws.S3BucketNames;
import com.loam.stoody.service.utils.aws.S3Service;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.LimitExceededException;

@Service
@AllArgsConstructor
public class UserFileService {
    private final S3Service s3Service;

    private final CustomUserDetailsService customUserDetailsService;
    private final UserFileRepository userFileRepository;
    private final UserRepository userRepository;

    @Transactional
    public OutdoorResponse<?> saveUserFile(UserFile userFile) {
        userFileRepository.save(userFile);
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, IndoorResponse.SUCCESS.toString());
    }

    public OutdoorResponse<?> findUserFileById(Long id) {
        final UserFile userFile = userFileRepository.findById(id).orElse(null);
        if (userFile != null) return new OutdoorResponse<>(IndoorResponse.SUCCESS, userFile);
        return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST, null);
    }

    public OutdoorResponse<?> deleteUserFileById(Long id) {
        final UserFile userFile = userFileRepository.findById(id).orElse(null);
        if (userFile != null) {
            userRepository.deleteById(id);
            return new OutdoorResponse<>(IndoorResponse.SUCCESS, IndoorResponse.SUCCESS.toString());
        }
        return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST, null);
    }

    public OutdoorResponse<?> uploadObject(MultipartFile multipartFile) {
        try {
            long fileSizeInBytes = multipartFile.getSize();
            double fileSizeInGB = fileSizeInBytes / (1024.0 * 1024.0 * 1024.0);
            if (fileSizeInGB >= 1) {
                throw new LimitExceededException();
            }
            final String url = s3Service.putObject(S3BucketNames.S3BucketNameStoodyTeacherCourseVideo, multipartFile);

            UserFile userFile = new UserFile();
            userFile.setName(multipartFile.getOriginalFilename());
            userFile.setUrl(url);
            if (ProjectConfigurationVariables.stoodyEnvironment.equals(ProjectConfigurationVariables.developmentMode)) {
                if (customUserDetailsService.getCurrentUser() != null)
                    userFile.setOwner(customUserDetailsService.getCurrentUser());
            } else {
                if (customUserDetailsService.getCurrentUser() != null) {
                    throw new RuntimeException();
                }
                userFile.setOwner(customUserDetailsService.getCurrentUser());
            }

            saveUserFile(userFile);

            return new OutdoorResponse<>(IndoorResponse.SUCCESS, mapUserFileEntityToRequest(userFile));

        } catch (LimitExceededException exception) {
            return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST, "You cannot upload a file larger than 1 GB!");
        } catch (Exception ignore) {
        }
        return new OutdoorResponse<>(IndoorResponse.FAIL, "FAILED");
    }

    public UserFile mapUserFileRequestToEntity(UserFileRequestDTO userFileRequestDTO) {
        try {
            if (userFileRequestDTO == null) throw new RuntimeException();

            UserFile userFile;
            try {
                if (userFileRequestDTO.getId() == null && userFileRequestDTO.getId() < 1) throw new RuntimeException();

                OutdoorResponse<?> outdoorResponse = findUserFileById(userFileRequestDTO.getId());
                if(outdoorResponse == null)throw new RuntimeException();
                if(outdoorResponse.getResponse() == null) throw new RuntimeException();
                if (!outdoorResponse.getResponseStatus().equals(IndoorResponse.SUCCESS.toString()))
                    throw new RuntimeException();

                userFile = (UserFile) outdoorResponse.getResponse();
            } catch (Exception ignore) {
                userFile = new UserFile();
                try{
                 if(userFileRequestDTO.getName() == null || userFileRequestDTO.getUrl() == null)
                     throw new RuntimeException();
                    if(userFileRequestDTO.getName().isBlank() || userFileRequestDTO.getUrl().isBlank())
                        throw new RuntimeException();
                }catch(RuntimeException ignore2){
                    userFileRequestDTO.setName("Blank");
                    userFileRequestDTO.setUrl("Blank");
                }
            }

            if (ProjectConfigurationVariables.stoodyEnvironment.equals(ProjectConfigurationVariables.developmentMode)) {
                if (customUserDetailsService.getCurrentUser() != null) {
                    userFile.setOwner(customUserDetailsService.getCurrentUser());
                }
            } else {
                if (customUserDetailsService.getCurrentUser() == null) throw new RuntimeException();
                userFile.setOwner(customUserDetailsService.getCurrentUser());
            }

            userFile.setName(userFileRequestDTO.getName());
            userFile.setUrl(userFileRequestDTO.getUrl());

            return userFile;
        } catch (Exception ignore) {
            return null;
        }
    }

    public UserFileRequestDTO mapUserFileEntityToRequest(UserFile userFile) {
        if (userFile == null) return null;
        UserFileRequestDTO userFileRequestDTO = new UserFileRequestDTO();

        userFileRequestDTO.setId(userFile.getId());
        if (userFile.getName() == null || userFile.getName().isBlank()) return null;
        userFileRequestDTO.setName(userFile.getName());
        if (userFile.getUrl() == null || userFile.getUrl().isBlank()) return null;
        userFileRequestDTO.setUrl(userFile.getUrl());

        return userFileRequestDTO;
    }
}
