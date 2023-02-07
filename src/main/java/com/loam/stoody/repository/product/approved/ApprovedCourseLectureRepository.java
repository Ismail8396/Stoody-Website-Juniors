package com.loam.stoody.repository.product.approved;

import com.loam.stoody.model.product.course.approved.ApprovedCourseLecture;
import com.loam.stoody.model.product.course.pending.PendingCourseLecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApprovedCourseLectureRepository extends JpaRepository<ApprovedCourseLecture, Long> {

    List<ApprovedCourseLecture> findAllByApprovedCourseSection_Id(Long id);
}
