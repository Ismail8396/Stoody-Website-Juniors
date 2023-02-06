package com.loam.stoody.model.product.course.attributes;

import com.loam.stoody.model.product.course.pending.PendingCourse;
import com.loam.stoody.model.user.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Data
@Entity
@DynamicUpdate
@DynamicInsert
public class CourseRating {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long rating;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    @ManyToOne(targetEntity = PendingCourse.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private PendingCourse pendingCourse;
}