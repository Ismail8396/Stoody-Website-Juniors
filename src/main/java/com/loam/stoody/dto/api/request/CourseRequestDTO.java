package com.loam.stoody.dto.api.request;

import com.loam.stoody.enums.CourseLevel;
import com.loam.stoody.enums.CourseStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class CourseRequestDTO {
    private Long id;
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