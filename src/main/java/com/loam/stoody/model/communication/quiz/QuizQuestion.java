package com.loam.stoody.model.communication.quiz;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import java.util.List;

@Entity
@Data
@DynamicUpdate
@DynamicInsert
public class QuizQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String question;
    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="quiz_question_id")
    private List<QuizQuestionAnswer> answers;
}
