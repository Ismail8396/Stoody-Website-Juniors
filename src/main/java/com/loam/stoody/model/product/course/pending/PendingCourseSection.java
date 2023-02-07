package com.loam.stoody.model.product.course.pending;

import com.loam.stoody.model.product.course.core.SectionCore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(indexes = {
        @Index(name = "courseSectIndex_id", columnList = "id", unique = true)
})
public class PendingCourseSection extends SectionCore {

    @ManyToOne(targetEntity = PendingCourse.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private PendingCourse pendingCourse;
}