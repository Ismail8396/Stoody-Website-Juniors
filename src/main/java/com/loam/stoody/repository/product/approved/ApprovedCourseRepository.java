package com.loam.stoody.repository.product.approved;

import com.loam.stoody.model.product.course.approved.ApprovedCourse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovedCourseRepository extends JpaRepository<ApprovedCourse, Long> {
}
