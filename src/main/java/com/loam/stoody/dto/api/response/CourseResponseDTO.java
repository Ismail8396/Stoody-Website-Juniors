package com.loam.stoody.dto.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.loam.stoody.enums.CourseLevel;
import com.loam.stoody.enums.CourseStatus;
import com.loam.stoody.model.product.course.Course;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CourseResponseDTO {
    private String title;
    private String subTitle;
    private String description;
    private String thumbnailURL;
    private String promoVideoURL;
    private String languageCode;
    private CourseLevel level;
    private String welcomeMessage;
    private String congratulationsMessage;
    private Long viewCount;
    private LocalDateTime uploadDate;
    private String currency = "USD";// TODO: remove hardcode
    private Double price;
    private Double discount;
    private Long enrolledStudents;
    private Long rating;
    private CourseStatus courseStatus;
    private long courseCategoryId;
    private List<CourseSectionResponseDTO> sections;


    public CourseResponseDTO(Course course, boolean courseWithDetails) {
        BeanUtils.copyProperties(course, this);
        if(courseWithDetails) {
            if (!CollectionUtils.isEmpty(course.getCourseSections())) {
                this.setSections(course.getCourseSections().stream().map(courseSection -> {
                    CourseSectionResponseDTO courseSectionResponseDTO = new CourseSectionResponseDTO(courseSection);
                    return courseSectionResponseDTO;
                }).collect(Collectors.toList()));
            }
        }
        if (!ObjectUtils.isEmpty(course.getCourseCategory()))
            this.courseCategoryId = course.getCourseCategory().getId();
    }
}
