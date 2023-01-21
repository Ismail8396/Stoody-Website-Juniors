package com.loam.stoody.dto.api.request;

import lombok.Data;

@Data
public class CourseCategoryRequestDTO {
    private String name;
    private Integer parentId;
    private Boolean published;
}