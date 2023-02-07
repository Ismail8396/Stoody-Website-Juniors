package com.loam.stoody.repository.product.approved;

import com.loam.stoody.model.product.course.approved.ApprovedCourseSection;
import com.loam.stoody.model.product.course.pending.PendingCourseSection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApprovedCourseSectionRepository extends JpaRepository<ApprovedCourseSection, Long> {

    List<ApprovedCourseSection> findAllByApprovedCourse_Id(Long id);
}
