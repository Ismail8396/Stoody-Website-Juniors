package com.loam.stoody.model.product.course.quiz;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Data
@DynamicUpdate
@DynamicInsert
public class QuizQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String question;

    @ManyToOne(targetEntity = Quiz.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", referencedColumnName = "id")
    private Quiz quiz;
//    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name="quiz_question_id")
//    private List<QuizQuestionAnswer> answers;
}
