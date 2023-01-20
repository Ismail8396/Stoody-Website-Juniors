package com.loam.stoody.dto.api.request;

import com.loam.stoody.enums.CourseLevel;
import com.loam.stoody.enums.CourseStatus;
import com.loam.stoody.model.product.course.Course;
import com.loam.stoody.model.product.course.CourseCategory;
import com.loam.stoody.model.product.course.CourseLecture;
import com.loam.stoody.model.product.course.CourseSection;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class CourseRequestDTO {
    private Long courseId;
    private String congratulationsMessage;
    private String contextTags;
    private Integer courseCategoryId;
    private CourseStatus courseStatus;
    private String currency = "USD";// TODO: remove hardcode
    private String description;
    private Double discount;
    private String languageCode;
    private CourseLevel level;
    private Double price;
    private String promoVideoName;
    private String promoVideoURL;
    private List<CourseSectionRequestDTO> sections;
    private String tags;
    private String thumbnailName;
    private String thumbnailURL;
    private String title;
    private String welcomeMessage;

    private Long viewCount;
    private LocalDateTime uploadDate;
    private Long enrolledStudents;
    private Long rating;
}