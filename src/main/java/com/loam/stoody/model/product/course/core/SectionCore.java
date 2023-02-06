package com.loam.stoody.model.product.course.core;

import com.loam.stoody.model.ParentModel;
import com.loam.stoody.model.product.course.pending.PendingCourse;
import jakarta.persistence.*;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class SectionCore extends ParentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;

    @ManyToOne(targetEntity = PendingCourse.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private PendingCourse pendingCourse;
}
