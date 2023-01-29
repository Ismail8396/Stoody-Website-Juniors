package com.loam.stoody.repository.product.quiz;

import com.loam.stoody.model.product.course.quiz.QuizQuestionAnswer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizQuestionAnswerRepository extends CrudRepository<QuizQuestionAnswer, Long> {

    List<QuizQuestionAnswer> findAllByQuestion_Id(Long id);

    @Modifying
    @Query(nativeQuery = true, value = "delete from quiz_question_answer qqa where qqa.quiz_question_id IN (select id from quiz_question qq where qq.quiz_id = :quizId)")
    void deleteAllByQuizId(Long quizId);
}
