package com.loam.stoody.model.communication.quiz;

import com.loam.stoody.model.user.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Data
@DynamicUpdate
@DynamicInsert
public class QuizQuestionAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
//    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
//    @JoinColumn(name = "quiz_question_id", referencedColumnName = "id")
//    private QuizQuestion question;
    private String answer;
    private Boolean isTrue;
}
