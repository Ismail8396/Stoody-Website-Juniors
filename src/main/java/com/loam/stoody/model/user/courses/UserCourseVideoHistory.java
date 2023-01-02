/*
@fileName:  UserVideoHistory

@aka:       User Video History model

@purpose:   Contains the video history, how much time a user spent on it etc.

@author:    aleemkhowaja

@created:   01.12.2022
*/

package com.loam.stoody.model.user.courses;

import com.loam.stoody.model.communication.video.Video;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
public class UserCourseVideoHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(targetEntity = CourseHistory.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_history_id")
    @ToString.Exclude
    private CourseHistory courseHistory;

    @ManyToOne(targetEntity = Video.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    @ToString.Exclude
    private Video video;

    private Long timeElapsed;
    private Boolean isCompleted;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserCourseVideoHistory that = (UserCourseVideoHistory) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}