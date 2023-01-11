package com.loam.stoody.dto.api.response;

import com.loam.stoody.model.product.course.CourseLecture;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class CourseLectureResponseDTO {
    private Long id;
    private String title;
    private String article;
    private String description;
    private String imageUrl;
    public CourseLectureResponseDTO(CourseLecture courseLecture) {
        BeanUtils.copyProperties(courseLecture, this);
    }
}
