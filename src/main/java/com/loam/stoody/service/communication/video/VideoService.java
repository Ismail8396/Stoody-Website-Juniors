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
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InvalidClassException;
import java.util.*;

@Service
@AllArgsConstructor
public class VideoService {
    private CustomUserDetailsService customUserDetailsService;
    private UserFileService userFileService;
    private VideoRepository videoRepository;
    private SubtitlesRepository subtitlesRepository;

    @Transactional
    public Video save(Video video) {
        return videoRepository.save(video);
    }

    @Transactional
    public OutdoorResponse<?> saveConverted(VideoRequestDTO video, boolean saveSubtitles) {
        try {
            Video videoEntity = mapVideoRequestToEntity(video);
            if (videoEntity == null) throw new RuntimeException();
            videoRepository.save(videoEntity);

            // Save the nonNull elements if there's any.
            // Setting video for the Subtitle instance will be done here.
            if (saveSubtitles)
                video.getSubtitles().stream().map(this::mapSubtitleRequestToEntity).filter(Objects::nonNull).forEach(e -> {
                    e.setVideo(videoEntity);
                    subtitlesRepository.save(e);
                });

            return new OutdoorResponse<>(IndoorResponse.SUCCESS, IndoorResponse.SUCCESS);
        } catch (Exception ignore) {
            return new OutdoorResponse<>(IndoorResponse.FAIL, IndoorResponse.FAIL);
        }
    }

    public Optional<Video> findById(Long id) {
        return videoRepository.findById(id);
    }

    public void delete(Video video) {
        videoRepository.delete(video);
    }

    @Transactional
    public Subtitle saveSubtitle(Subtitle subtitles) {
        return subtitlesRepository.save(subtitles);
    }

    @Transactional
    public OutdoorResponse<?> saveSubtitle(SubtitleRequestDTO subtitle) {
        Subtitle subtitleEntity = mapSubtitleRequestToEntity(subtitle);
        if (subtitleEntity == null)
            return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST, IndoorResponse.BAD_REQUEST);

        subtitlesRepository.save(subtitleEntity);
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, IndoorResponse.SUCCESS);
    }

    public Optional<Subtitle> findSubtitleById(Long id) {
        return subtitlesRepository.findById(id);
    }

    public void deleteSubtitle(Subtitle subtitles) {
        subtitlesRepository.delete(subtitles);
    }

    // Warning: This method does not save the subtitles! You have to do that after saving the video.
    public Video mapVideoRequestToEntity(VideoRequestDTO video) throws IllegalAccessException, InvalidClassException, IllegalArgumentException {
        if (video == null) {
            throw new InvalidClassException("");
        }
        if (video.getVideoDuration() == null || video.getVideoDuration() < 0) {
            throw new IllegalArgumentException();
        }
        Video videoEntity = new Video();
        videoEntity.setId(video.getId());
        videoEntity.setName(video.getName());
        videoEntity.setUrl(video.getUrl());
        videoEntity.setVideoDuration(video.getVideoDuration());
        if (ProjectConfigurationVariables.stoodyEnvironment.equals(ProjectConfigurationVariables.developmentMode)) {
            if (customUserDetailsService.getCurrentUser() != null)
                videoEntity.setOwner(customUserDetailsService.getCurrentUser());
        } else {
            if (customUserDetailsService.getCurrentUser() == null) throw new IllegalAccessException();
            videoEntity.setOwner(customUserDetailsService.getCurrentUser());
        }

        return videoEntity;
    }

    public VideoRequestDTO mapVideoEntityToRequest(Video video) throws IllegalAccessException, InvalidClassException, IllegalArgumentException {
        if (video == null) {
            throw new InvalidClassException("");
        }

        VideoRequestDTO videoRequestDTO = new VideoRequestDTO();
        if (video.getSubtitles() != null && video.getSubtitles().size() > 0) {

            Set<SubtitleRequestDTO> subtitleRequestDTOS = new HashSet<>();
            for (var i : video.getSubtitles()) {
                SubtitleRequestDTO subtitleRequestDTO = mapSubtitleEntityToRequest(i);
                if (subtitleRequestDTO != null) {
                    subtitleRequestDTOS.add(subtitleRequestDTO);
                }
            }
            if (!subtitleRequestDTOS.isEmpty()) videoRequestDTO.setSubtitles(subtitleRequestDTOS);
        }
        videoRequestDTO.setVideoDuration(video.getVideoDuration());
        videoRequestDTO.setId(video.getId());
        videoRequestDTO.setName(video.getName());
        videoRequestDTO.setUrl(video.getUrl());
        return videoRequestDTO;
    }

    public Subtitle mapSubtitleRequestToEntity(SubtitleRequestDTO subtitle) {
        if (subtitle == null) return null;
        if (subtitle.getLanguageCode() == null || subtitle.getLanguageCode().isBlank()) {
            return null;
        }
        if (subtitle.getUserFileId() == null || subtitle.getUserFileId() < 1) {
            return null;
        }
        OutdoorResponse<?> outdoorResponse = userFileService.findUserFileById(subtitle.getUserFileId());
        if (!outdoorResponse.getResponseStatus().equals(IndoorResponse.SUCCESS.toString())) return null;

        Subtitle subtitleEntity = new Subtitle();
        subtitleEntity.setLanguageCode(subtitle.getLanguageCode());
        subtitleEntity.setSubtitleFile((UserFile) outdoorResponse.getResponse());

        return subtitleEntity;
    }

    public SubtitleRequestDTO mapSubtitleEntityToRequest(Subtitle subtitle) {
        if (subtitle == null) return null;

        if (subtitle.getLanguageCode() == null || subtitle.getLanguageCode().isBlank()) {
            return null;
        }
        if (subtitle.getSubtitleFile() == null) {
            return null;
        }

        SubtitleRequestDTO subtitleRequestDTO = new SubtitleRequestDTO();
        subtitleRequestDTO.setUserFileId(subtitle.getSubtitleFile().getId());
        subtitleRequestDTO.setLanguageCode(subtitle.getLanguageCode());
        return subtitleRequestDTO;
    }

    public List<Video> getUserVideos() {
//        if (customUserDetailsService.getCurrentUser() == null && ProjectConfigurationVariables.stoodyEnvironment.equals(ProjectConfigurationVariables.developmentMode))
//            return videoRepository.findAll();
//        return videoRepository.findAll().stream().filter(e -> e.).toList();
        return Collections.emptyList();
    }
}