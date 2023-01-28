package com.loam.stoody.repository.product;

import com.loam.stoody.global.annotations.UnderDevelopment;
import com.loam.stoody.model.product.course.Course;
import com.loam.stoody.model.product.course.CourseSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@UnderDevelopment
@Repository
public interface CourseSectionRepository extends JpaRepository<CourseSection, Long> {
    List<CourseSection> findAllByCourse_Id(Long courseId);
}