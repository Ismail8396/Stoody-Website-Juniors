/*
@fileName:  Course

@aka:       Course Model

@purpose:   Contains the data (that's either transient or non-transient) of a course.

@author:    OrkhanGG

@created:   16.12.2022
*/

package com.loam.stoody.model.product.course;


import com.loam.stoody.enums.CourseLevel;
import com.loam.stoody.enums.CourseStatus;
import com.loam.stoody.model.ParentModel;
import com.loam.stoody.model.communication.misc.Comment;
import com.loam.stoody.model.user.User;
import lombok.Data;

import jakarta.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(indexes = {
        @Index(name = "courseIndex_id", columnList = "id", unique = true),
        @Index(name = "courseIndex_title", columnList = "title", unique = true),
})
@Data
@DynamicUpdate
@DynamicInsert
public class Course extends ParentModel {
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

    @ManyToOne(targetEntity = CourseCategory.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_category_id", referencedColumnName = "id")
    private CourseCategory courseCategory;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="COMMENTS_ID")
    private Set<Comment> comments;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            targetEntity = CourseSection.class
    )
    @JoinColumn(name="course_id")
    private List<CourseSection> courseSections;
}
