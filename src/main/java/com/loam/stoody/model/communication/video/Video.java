/*
@fileName:  Video

@aka:       Video Model

@purpose:   Contains the data (that's either transient or non-transient) of a video.

@author:    OrkhanGG

@created:   01.12.2022
*/

package com.loam.stoody.model.communication.video;

import com.loam.stoody.model.product.course.Course;
import lombok.Data;

import jakarta.persistence.*;

@Data
@Entity
@Table(name = "videos")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String title;
    private String description;
    private String videoUrl;
    private String thumbnailUrl;
    private String subtitlesURL;
    private Long durationInMinutes;
    @ManyToOne (fetch = FetchType.LAZY)
    private Course course;
}