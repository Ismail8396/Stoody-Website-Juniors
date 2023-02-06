package com.loam.stoody.dto.api.response.course;

import lombok.Data;

@Data
@Deprecated
public class CourseCategoryResponseDTO {
    private String name;
    private Integer parentId;
    private Boolean published;
}