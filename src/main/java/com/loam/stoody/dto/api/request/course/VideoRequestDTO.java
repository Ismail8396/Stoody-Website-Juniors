package com.loam.stoody.dto.api.request.course;

import lombok.Data;

import java.util.Set;

@Data
public class VideoRequestDTO {
    private Long id;
    private String name;
    private String url;
    private Double videoDuration;
    private Set<SubtitleRequestDTO> subtitles;
}
