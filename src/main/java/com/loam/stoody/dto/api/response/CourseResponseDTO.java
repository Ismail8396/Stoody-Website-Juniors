package com.loam.stoody.dto.api.response;

import com.loam.stoody.enums.CourseLevel;
import com.loam.stoody.enums.CourseStatus;
import com.loam.stoody.model.product.course.Course;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

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

    public CourseResponseDTO(Course course) {
        BeanUtils.copyProperties(course, this);
        this.courseCategoryId = course.getCourseCategory().getId();
    }
}
