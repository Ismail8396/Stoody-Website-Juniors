package com.loam.stoody.dto;

import com.loam.stoody.model.Category;
import com.loam.stoody.model.User;
import com.loam.stoody.model.Video;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class CourseDTO {
    private Long id;
    private String courseTitle;
    private String courseSubtitle;
    private String courseDescription;
    private String thumbnailURL;
    private String promoVideoURL;

    private String languageCode;
    private String level;

    private String welcomeMessage;
    private String congratulationsMessage;

    private Integer categoryId;

    private Integer authorId;// I'm not sure about this. Maybe using User would be better?

    private Set<Integer> videosId;

    private Long viewCount;
    private LocalDateTime uploadDate;

    private String currency;
    private Double price;
    private Double discount;
}
