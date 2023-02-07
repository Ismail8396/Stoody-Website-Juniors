package com.loam.stoody.repository.product.pending;

import com.loam.stoody.global.annotations.UnderDevelopment;
import com.loam.stoody.model.product.course.pending.PendingCourseSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@UnderDevelopment
@Repository
public interface PendingCourseSectionRepository extends JpaRepository<PendingCourseSection, Long> {
    @Modifying
    @Query(nativeQuery = true, value = "delete from pending_course_section cl where cl.course_id = :courseId")
    void removeAllByCourseId(@Param("courseId") Long courseId);

    List<PendingCourseSection> findAllByPendingCourse_Id(Long id);
}