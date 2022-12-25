package com.loam.stoody.repository.product;

import com.loam.stoody.global.annotations.UnderDevelopment;
import com.loam.stoody.model.product.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@UnderDevelopment
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findAllByCategoryId(int id);
}
