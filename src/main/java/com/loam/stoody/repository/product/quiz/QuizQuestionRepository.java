package com.loam.stoody.repository.product.quiz;

import com.loam.stoody.model.product.course.quiz.QuizQuestion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizQuestionRepository extends CrudRepository<QuizQuestion, Long> {
    List<QuizQuestion> findAllByQuiz_Id(Long quizId);
    void deleteAllByQuiz_Id(Long quizId);
}
