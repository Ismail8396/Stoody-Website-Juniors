package com.loam.stoody.dto.api.response;

import com.loam.stoody.model.product.course.CourseSection;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Data
public class CourseSectionResponseDTO {
    private Long id;
    private String title;
    private List<CourseLectureResponseDTO> lectures;

    public CourseSectionResponseDTO(CourseSection courseSection) {
        BeanUtils.copyProperties(courseSection, this);
    }

}
