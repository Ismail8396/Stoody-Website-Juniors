package com.loam.stoody.repository.product;

import com.loam.stoody.global.annotations.UnderDevelopment;
import com.loam.stoody.model.product.course.CourseLecture;
import com.loam.stoody.model.product.course.CourseRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@UnderDevelopment
@Repository
public interface CourseRatingRepository extends JpaRepository<CourseRating, Long> {

    List<CourseLecture> findAllByCourse_IdAndAuthor_Id(Long courseId, Long authorId);
}
