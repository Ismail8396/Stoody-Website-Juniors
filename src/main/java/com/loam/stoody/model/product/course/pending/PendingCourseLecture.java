package com.loam.stoody.model.product.course.pending;

import com.loam.stoody.model.product.course.core.LectureCore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(indexes = {@Index(name = "lectureIndex_id", columnList = "id", unique = true)})
public class PendingCourseLecture extends LectureCore {

    @ManyToOne(targetEntity = PendingCourseSection.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_section_id", referencedColumnName = "id")
    PendingCourseSection pendingCourseSection;
}