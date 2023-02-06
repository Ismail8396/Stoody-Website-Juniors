package com.loam.stoody.dto.api.request.course;

import lombok.Data;

import java.util.Set;

@Data
public class VideoRequestDTO {
    Long id;
    Long userFileId;
    Long videoDuration;
    private Set<SubtitleRequestDTO> subtitles;
}
