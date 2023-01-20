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
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(indexes = {
        @Index(name = "courseIndex_id", columnList = "id", unique = true),
})
@Data
@DynamicUpdate
@DynamicInsert
@Where(clause = "is_deleted='false'")
public class Course extends ParentModel {
    private String congratulationsMessage;
    private String contextTags;
    private CourseStatus courseStatus;
    private String currency = "USD";// TODO: remove hardcode
    @Lob
    private String description;
    private Double discount;
    private String languageCode;
    private CourseLevel level;
    private Double price;
    private String promoVideoName;
    private String promoVideoURL;
    private String tags;
    private String thumbnailName;
    private String thumbnailURL;
    private String title;
    private String welcomeMessage;

    private Long viewCount;
    private LocalDateTime uploadDate;
    private Long enrolledStudents;
    private Long rating;

    @ManyToOne(targetEntity = CourseCategory.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_category_id", referencedColumnName = "id")
    private CourseCategory courseCategory;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="COMMENTS_ID")
    private Set<Comment> comments;

//    @OneToMany(
//            cascade = CascadeType.ALL,
//            targetEntity = CourseSection.class
//    )
//    @JoinColumn(name="course_id")
//    private List<CourseSection> sections;
}
