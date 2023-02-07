package com.loam.stoody.model.product.course.approved;

import com.loam.stoody.model.product.course.core.LectureCore;
import com.loam.stoody.model.product.course.pending.PendingCourseSection;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(indexes = {@Index(name = "lectureIndex_id", columnList = "id", unique = true)})
public class ApprovedCourseLecture extends LectureCore {

    @ManyToOne(targetEntity = ApprovedCourseSection.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_section_id", referencedColumnName = "id")
    ApprovedCourseSection approvedCourseSection;
}
