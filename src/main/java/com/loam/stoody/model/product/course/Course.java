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
import org.apache.logging.log4j.util.Strings;
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
    private String congratulationsMessage = Strings.EMPTY;
    private String contextTags = Strings.EMPTY;
    private CourseStatus courseStatus = CourseStatus.Draft;
    private String currency = "USD";// TODO: remove hardcode
    @Column(columnDefinition = "TEXT", length = 100000)
    private String description = Strings.EMPTY;
    private Double discount = 0D;
    private String languageCode = "EN";
    private CourseLevel level = CourseLevel.Beginners;
    private Double price = 0D;
    private String promoVideoName = Strings.EMPTY;
    private String promoVideoURL = Strings.EMPTY;
    private String tags = Strings.EMPTY;
    private String thumbnailName = Strings.EMPTY;
    private String thumbnailURL = Strings.EMPTY;
    private String title = Strings.EMPTY;
    private String welcomeMessage = Strings.EMPTY;

    private Long viewCount = 0L;
    private LocalDateTime uploadDate;// Remove
    private Long enrolledStudents = 0L;//?
    private Long rating = 0L;//?

    @ManyToOne(targetEntity = CourseCategory.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_category_id", referencedColumnName = "id")
    private CourseCategory courseCategory;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="COMMENTS_ID")
    private Set<Comment> comments;
}