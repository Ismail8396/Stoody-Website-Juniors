package com.loam.stoody.model.product.course.core;

import com.loam.stoody.model.ParentModel;
import com.loam.stoody.model.communication.file.UserFile;
import com.loam.stoody.model.communication.video.Video;
import com.loam.stoody.model.product.course.pending.PendingCourseLecture;
import com.loam.stoody.model.product.course.pending.PendingCourseSection;
import com.loam.stoody.model.product.course.quiz.Quiz;
import jakarta.persistence.*;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class LectureCore extends ParentModel {
    @ManyToOne(targetEntity = PendingCourseSection.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_section_id", referencedColumnName = "id")
    PendingCourseSection pendingCourseSection;

    @ManyToOne(targetEntity = UserFile.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_file_id", referencedColumnName = "id")
    private UserFile userFile;

    @ManyToOne(targetEntity = Video.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", referencedColumnName = "id")
    private Video video;

    @OneToOne(targetEntity = Quiz.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", referencedColumnName = "id")
    private Quiz quiz;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String description;

    // UI Serialization
    private Boolean locked;
    private Boolean uiCollapsed;
}
