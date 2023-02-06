package com.loam.stoody.model.product.course.approved;


import com.loam.stoody.model.product.course.core.SectionCore;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(indexes = {
        @Index(name = "courseSectIndex_id", columnList = "id", unique = true)
})
public class ApprovedCourseSection extends SectionCore {
}
