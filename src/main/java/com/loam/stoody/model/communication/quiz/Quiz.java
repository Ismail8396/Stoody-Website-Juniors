package com.loam.stoody.model.communication.quiz;

import com.loam.stoody.model.product.course.CourseLecture;
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
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "COMMENTS_ID")
    private List<QuizQuestion> questions;
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;
    @OneToOne(targetEntity = CourseLecture.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id", referencedColumnName = "id")
    private CourseLecture courseLecture;
}
