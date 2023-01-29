package com.loam.stoody.model.communication.quiz;

import com.loam.stoody.model.user.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Entity
@Data
@DynamicUpdate
@DynamicInsert
public class QuizResults {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    User user;
    @ManyToOne(targetEntity = QuizQuestionAnswer.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_question_answer_id", referencedColumnName = "id")
    QuizQuestionAnswer quizQuestionAnswer;
}
