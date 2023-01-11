package com.loam.stoody.repository.product;

import com.loam.stoody.global.annotations.UnderDevelopment;
import com.loam.stoody.model.product.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@UnderDevelopment
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findAllByCourseCategory_Id(long id);

    @Modifying
    @Query(value = "update Course SET isDeleted=true where id =: courseId")
    void deleteCourse(@Param("courseId") Long courseId);


}
