package com.loam.stoody.model.product.course.quiz;

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

    private Long timeoutInSeconds;
    @ManyToOne(targetEntity = Quiz.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", referencedColumnName = "id")
    private Quiz quiz;
}
