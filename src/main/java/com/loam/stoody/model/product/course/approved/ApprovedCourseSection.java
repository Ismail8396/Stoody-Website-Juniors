package com.loam.stoody.model.product.course.approved;


import com.loam.stoody.model.product.course.core.SectionCore;
import com.loam.stoody.model.product.course.pending.PendingCourse;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(indexes = {
        @Index(name = "courseSectIndex_id", columnList = "id", unique = true)
})
public class ApprovedCourseSection extends SectionCore {

    @ManyToOne(targetEntity = ApprovedCourse.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private ApprovedCourse approvedCourse;
}
