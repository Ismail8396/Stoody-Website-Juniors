package com.loam.stoody.service.communication.video;

import com.loam.stoody.dto.api.request.course.SubtitleRequestDTO;
import com.loam.stoody.dto.api.request.course.VideoRequestDTO;
import com.loam.stoody.dto.api.response.OutdoorResponse;
import com.loam.stoody.enums.IndoorResponse;
import com.loam.stoody.global.constants.ProjectConfigurationVariables;
import com.loam.stoody.model.communication.file.UserFile;
import com.loam.stoody.model.communication.video.Subtitle;
import com.loam.stoody.model.communication.video.Video;
import com.loam.stoody.repository.communication.video.SubtitlesRepository;
import com.loam.stoody.repository.communication.video.VideoRepository;
import com.loam.stoody.service.communication.file.UserFileService;
import com.loam.stoody.service.user.CustomUserDetailsService;
import com.loam.stoody.service.utils.aws.S3BucketNames;
import com.loam.stoody.service.utils.aws.S3Service;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.LimitExceededException;
import java.io.InvalidClassException;
import java.util.*;

@Service
@AllArgsConstructor
public class VideoService {
    private final S3Service s3Service;

    private CustomUserDetailsService customUserDetailsService;
    private UserFileService userFileService;
    private VideoRepository videoRepository;
    private SubtitlesRepository subtitlesRepository;

    public Video mapVideoDtoToEntity(VideoRequestDTO dto){
        Video video = new Video();
        video.setId(dto.getId());
        video.setName(dto.getName());
        video.setUrl(dto.getUrl());
        video.setVideoDuration(dto.getVideoDuration());
        return video;
    }

    public VideoRequestDTO mapVideoEntityToDto(Video entity){
        VideoRequestDTO dto = new VideoRequestDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setUrl(entity.getUrl());
        dto.setVideoDuration(entity.getVideoDuration());
        return dto;
    }

    @Transactional
    public OutdoorResponse<?> uploadVideo(MultipartFile multipartFile, Double videoDuration) {
        try {
            long fileSizeInBytes = multipartFile.getSize();
            double fileSizeInGB = fileSizeInBytes / (1024.0 * 1024.0 * 1024.0);
            if (fileSizeInGB >= 1) {
                throw new LimitExceededException();
            }
            final String url = s3Service.putObject(S3BucketNames.S3BucketNameStoodyTeacherCourseVideo, multipartFile);

            Video video = new Video();
            video.setVideoDuration(videoDuration);
            video.setName(multipartFile.getOriginalFilename());
            video.setUrl(url);
            if(customUserDetailsService.getCurrentUser() != null)
                video.setOwner(customUserDetailsService.getCurrentUser());
            video = videoRepository.save(video);

            return new OutdoorResponse<>(IndoorResponse.SUCCESS, mapVideoEntityToDto(video));

        } catch (LimitExceededException exception) {
            return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST, "You cannot upload a file larger than 1 GB!");
        } catch (Exception ignore) {
        }
        return new OutdoorResponse<>(IndoorResponse.FAIL, "FAILED");
    }

}