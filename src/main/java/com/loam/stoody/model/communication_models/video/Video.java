/*
@fileName:  Video

@aka:       Video Model

@purpose:   Contains the data (that's either transient or non-transient) of a video.

@author:    OrkhanGG

@created:   01.12.2022
*/

package com.loam.stoody.model.communication_models.video;

import com.loam.stoody.model.product_models.course_models.Course;
import lombok.Data;

import javax.persistence.*;

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