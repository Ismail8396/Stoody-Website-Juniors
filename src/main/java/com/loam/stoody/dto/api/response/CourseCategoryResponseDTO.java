package com.loam.stoody.dto.api.response;

import lombok.Data;

@Data
public class CourseCategoryResponseDTO {
    private String name;
    private Integer parentId;
    private Boolean published;
}
