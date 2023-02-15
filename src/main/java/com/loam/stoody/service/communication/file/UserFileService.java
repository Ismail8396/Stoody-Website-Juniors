package com.loam.stoody.service.communication.file;

import com.loam.stoody.dto.api.request.file.UserFileRequestDTO;
import com.loam.stoody.dto.api.response.OutdoorResponse;
import com.loam.stoody.enums.IndoorResponse;
import com.loam.stoody.global.constants.ProjectConfigurationVariables;
import com.loam.stoody.model.communication.file.UserFile;
import com.loam.stoody.model.user.User;
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
    // Return DTO!
    public OutdoorResponse<?> saveUserFile(UserFile userFile) {
        userFileRepository.save(userFile);
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, IndoorResponse.SUCCESS.toString());
    }

    // Return DTO!
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

    @Transactional
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
        UserFile userFile = new UserFile();
        userFile.setId(userFileRequestDTO.getId());
        userFile.setName(userFileRequestDTO.getName());
        userFile.setUrl(userFileRequestDTO.getUrl());
        final User currentUser = customUserDetailsService.getCurrentUser();
        if(currentUser != null)
            userFile.setOwner(currentUser);
        return userFile;
    }

    public UserFileRequestDTO mapUserFileEntityToRequest(UserFile userFile) {
        UserFileRequestDTO dto = new UserFileRequestDTO();
        dto.setId(userFile.getId());
        dto.setName(userFile.getName());
        dto.setUrl(userFile.getUrl());
        return dto;
    }
}
