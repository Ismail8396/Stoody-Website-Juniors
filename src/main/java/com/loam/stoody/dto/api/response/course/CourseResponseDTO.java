package com.loam.stoody.dto.api.response.course;

import com.loam.stoody.dto.api.request.course.pending.PendingCourseSectionDTO;
import com.loam.stoody.dto.api.request.course.VideoRequestDTO;
import com.loam.stoody.dto.api.request.file.UserFileRequestDTO;
import com.loam.stoody.enums.CourseLevel;
import com.loam.stoody.enums.CourseStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Deprecated
@NoArgsConstructor
public class CourseResponseDTO {
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
    private VideoRequestDTO promoVideo;// Video class
    private List<PendingCourseSectionDTO> sections;
    private String tags;
    private UserFileRequestDTO thumbnail;// UserFile class
    private String title;
    private String welcomeMessage;
}