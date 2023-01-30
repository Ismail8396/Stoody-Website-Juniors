package com.loam.stoody.repository.product.quiz;

import com.loam.stoody.model.product.course.quiz.Quiz;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends CrudRepository<Quiz, Long> {
    Quiz findByCourseLecture_Id(Long id);
    void deleteAllByCourseLecture_Id(Long id);
}
