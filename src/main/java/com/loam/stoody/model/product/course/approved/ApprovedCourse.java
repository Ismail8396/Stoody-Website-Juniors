package com.loam.stoody.model.product.course.approved;

import com.loam.stoody.model.product.course.core.CourseCore;
import com.loam.stoody.model.product.course.pending.PendingCourse;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import java.util.List;

@Entity
@Table(indexes = {
        @Index(name = "courseIndex_id", columnList = "id", unique = true),
})
@Data
@DynamicUpdate
@DynamicInsert
@Where(clause = "is_deleted='false'")
public class ApprovedCourse extends CourseCore {
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "approvedCourse", cascade = CascadeType.ALL)
    private PendingCourse pendingCourse;
}
