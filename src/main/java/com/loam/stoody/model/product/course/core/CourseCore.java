package com.loam.stoody.model.product.course.core;

import com.loam.stoody.enums.CourseLevel;
import com.loam.stoody.enums.CourseStatus;
import com.loam.stoody.model.ParentModel;
import com.loam.stoody.model.communication.file.UserFile;
import com.loam.stoody.model.communication.video.Video;
import com.loam.stoody.model.product.course.CourseCategory;
import com.loam.stoody.model.user.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

@Data
@MappedSuperclass
public abstract class CourseCore extends ParentModel {
    private String congratulationsMessage;
    private String contextTags;
    private CourseStatus courseStatus;
    private String currency = "USD";
    @Column(columnDefinition = "TEXT", length = 100000)
    private String description;
    private Double discount;
    private String languageCode;
    private CourseLevel level;
    private Double price;

    private String tags;

    private String title;
    private String welcomeMessage;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", referencedColumnName = "id")
    private Video promoVideo;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_file_id", referencedColumnName = "id")
    private UserFile thumbnail;

    @ManyToOne(targetEntity = CourseCategory.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_category_id", referencedColumnName = "id")
    private CourseCategory courseCategory;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;
}
