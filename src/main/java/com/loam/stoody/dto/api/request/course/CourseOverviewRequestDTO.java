package com.loam.stoody.dto.api.request.course;

import lombok.Data;

@Data
public class CourseOverviewRequestDTO {
    private Long id;
    private String name;
    private String authorUsername;
    private String courseThumbnail;
    private String status;
    private String createdAt;
}
