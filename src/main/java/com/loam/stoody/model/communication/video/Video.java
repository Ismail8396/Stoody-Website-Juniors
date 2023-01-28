/*
@fileName:  Video

@aka:       Video Model

@purpose:   Contains the data (that's either transient or non-transient) of a video.

@author:    OrkhanGG

@created:   01.12.2022
*/

package com.loam.stoody.model.communication.video;

import com.loam.stoody.model.product.course.CourseLecture;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "videos")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String thumbnailUrl;
    private String thumbnailSpriteUrl;
    private String subtitlesURL;
    private Long durationInMinutes;

    @ManyToOne(targetEntity = CourseLecture.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_lecture_id", referencedColumnName = "id")
    CourseLecture courseLecture;
}