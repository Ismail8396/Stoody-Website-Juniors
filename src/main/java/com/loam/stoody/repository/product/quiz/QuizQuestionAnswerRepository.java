package com.loam.stoody.repository.product.quiz;

import com.loam.stoody.model.product.course.quiz.QuizQuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizQuestionAnswerRepository extends JpaRepository<QuizQuestionAnswer, Long> {
}
