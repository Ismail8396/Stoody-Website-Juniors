package com.loam.stoody.dto.api.response.course;

import com.loam.stoody.dto.api.response.QuizResponseDTO;
import com.loam.stoody.model.product.course.pending.PendingCourseLecture;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
@Deprecated
public class CourseLectureResponseDTO {
    private String title;
    private String article;
    private String articleName;
    private String description;
    private String imageUrl;// TODO: What is this?
    private Boolean locked;
    private Boolean uiCollapsed;
    private QuizResponseDTO quiz;
    public CourseLectureResponseDTO(PendingCourseLecture pendingCourseLecture) {
        BeanUtils.copyProperties(pendingCourseLecture, this);
    }
}