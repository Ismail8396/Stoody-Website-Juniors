package com.loam.stoody.repository.product;

import com.loam.stoody.global.annotations.UnderDevelopment;
import com.loam.stoody.model.product.course.CourseLecture;
import com.loam.stoody.model.product.course.CourseSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@UnderDevelopment
@Repository
public interface CourseLectureRepository extends JpaRepository<CourseLecture, Long> {
}
