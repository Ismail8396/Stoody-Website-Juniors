package com.loam.stoody.dto.api.request;

import lombok.Data;

import java.util.List;

@Data
public class CourseSectionRequestDTO {
    private Long id;
    private String title;
    private List<CourseLectureRequestDTO> lectures;
}