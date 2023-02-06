package com.loam.stoody.repository.product.pending;

import com.loam.stoody.global.annotations.UnderDevelopment;
import com.loam.stoody.model.product.course.pending.PendingCourseLecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PendingCourseLectureRepository extends JpaRepository<PendingCourseLecture, Long> {
    @Modifying
    @Query(nativeQuery = true, value = "delete from pending_course_lecture cl where cl.course_section_id IN (select id from pending_course_section cs where cs.course_id = :courseId)")
    void removeAllByCourseId(@Param("courseId") Long courseId);
}