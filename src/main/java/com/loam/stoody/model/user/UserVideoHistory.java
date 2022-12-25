/*
@fileName:  UserVideoHistory

@aka:       User Video History model

@purpose:   Contains the video history, how much time a user spent on it etc.

@author:    aleemkhowaja

@created:   01.12.2022
*/

package com.loam.stoody.model.user;

import com.loam.stoody.model.communication.video.Video;
import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
public class UserVideoHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(targetEntity = CourseHistory.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_history_id")
    private CourseHistory courseHistory;

    @ManyToOne(targetEntity = Video.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Video video;

    private Long timeElapsed;
    private Boolean isCompleted;
}