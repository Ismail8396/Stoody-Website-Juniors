package com.loam.stoody.dto.api.request;

import lombok.Data;

@Data
public class CourseLectureRequestDTO {
    private Long id;
    private String title;
    private String article;
    private String articleName;
    private String description;
    private String imageUrl;// TODO: What is this?
}
