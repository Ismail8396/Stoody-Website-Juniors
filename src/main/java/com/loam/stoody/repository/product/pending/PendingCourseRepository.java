package com.loam.stoody.repository.product.pending;

import com.loam.stoody.global.annotations.UnderDevelopment;
import com.loam.stoody.model.product.course.pending.PendingCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PendingCourseRepository extends JpaRepository<PendingCourse, Long> {
    List<PendingCourse> findAllByCourseCategory_Id(long id);

    @Modifying
    @Query(value = "update PendingCourse SET isDeleted=true where id =: courseId")
    void deleteCourse(@Param("courseId") Long courseId);
}