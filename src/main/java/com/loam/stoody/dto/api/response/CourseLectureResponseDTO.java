package com.loam.stoody.dto.api.response;

import com.loam.stoody.dto.api.request.QuizRequestDTO;
import com.loam.stoody.model.product.course.CourseLecture;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class CourseLectureResponseDTO {
    private Long id;
    private String title;
    private String article;
    private String articleName;
    private String description;
    private String imageUrl;// TODO: What is this?
    private Boolean locked;
    private Boolean uiCollapsed;
    private QuizResponseDTO quiz;
    public CourseLectureResponseDTO(CourseLecture courseLecture) {
        BeanUtils.copyProperties(courseLecture, this);
    }
}