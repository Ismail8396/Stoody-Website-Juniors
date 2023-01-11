package com.loam.stoody.repository.product;

import com.loam.stoody.global.annotations.UnderDevelopment;
import com.loam.stoody.model.product.course.CourseLecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@UnderDevelopment
@Repository
public interface CourseLectureRepository extends JpaRepository<CourseLecture, Long> {

    List<CourseLecture> findAllByCourseSection_Id(Long courseId);
}
