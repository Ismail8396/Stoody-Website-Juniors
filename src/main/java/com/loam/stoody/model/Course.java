package com.loam.stoody.model;


import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
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
