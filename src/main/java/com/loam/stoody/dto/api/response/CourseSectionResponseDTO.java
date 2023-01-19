package com.loam.stoody.dto.api.response;

import com.loam.stoody.model.product.course.CourseSection;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class CourseSectionResponseDTO {
    private Long id;
    private String title;
    private List<CourseLectureResponseDTO> lectures;
    public CourseSectionResponseDTO(CourseSection courseSection) {
        BeanUtils.copyProperties(courseSection, this);
        if (!CollectionUtils.isEmpty(courseSection.getLectures())) {
            this.setLectures(courseSection.getLectures().stream().map(courseLecture -> {
                CourseLectureResponseDTO courseLectureResponseDTO = new CourseLectureResponseDTO(courseLecture);
                return courseLectureResponseDTO;
            }).collect(Collectors.toList()));
        }
    }

}
