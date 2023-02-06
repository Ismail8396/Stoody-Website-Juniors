package com.loam.stoody.dto.api.response.course;

import com.loam.stoody.model.product.course.pending.PendingCourseSection;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Data
@Deprecated
public class CourseSectionResponseDTO {
    private String title;
    private List<CourseLectureResponseDTO> lectures;

    public CourseSectionResponseDTO(PendingCourseSection pendingCourseSection) {
        BeanUtils.copyProperties(pendingCourseSection, this);
    }
}