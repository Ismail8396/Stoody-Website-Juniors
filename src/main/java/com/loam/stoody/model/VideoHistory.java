package com.loam.stoody.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
public class VideoHistory {

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