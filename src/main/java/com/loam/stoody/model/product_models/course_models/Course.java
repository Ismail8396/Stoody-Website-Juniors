/*
@fileName:  Course

@aka:       Course Model

@purpose:   Contains the data (that's either transient or non-transient) of a course.

@author:    OrkhanGG

@created:   16.12.2022
*/

package com.loam.stoody.model.product_models.course_models;


import com.loam.stoody.model.product_models.Category;
import com.loam.stoody.model.communication_models.misc.Comment;
import com.loam.stoody.model.communication_models.video.Video;
import com.loam.stoody.model.user_models.User;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID", referencedColumnName = "CATEGORY_ID")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "AUTHOR_ID")
    private User author;

    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="VIDEOS_ID")
    private Set<Video> videos;

    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="COMMENTS_ID")
    private Set<Comment> comments;

    private Long viewCount;
    private LocalDateTime uploadDate;

    private String currency = "USD";// TODO: remove hardcode
    private Double price;
    private Double discount;
}
