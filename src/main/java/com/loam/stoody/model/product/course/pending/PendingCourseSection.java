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
}