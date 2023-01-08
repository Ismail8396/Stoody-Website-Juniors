/*
@fileName:  Comment

@aka:       Comment Model

@purpose:   Contains the data (that's either transient or non-transient) of a comment.

@author:    OrkhanGG

@created:   01.12.2022
*/

package com.loam.stoody.model.communication.misc;

import com.loam.stoody.model.product.course.Course;
import com.loam.stoody.model.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String content;
    private Integer likeCount;
    private Integer disLikeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToMany
    @JoinColumn(name = "replies_id")
    @ToString.Exclude
    private Set<Comment> replies;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Comment comment = (Comment) o;
        return id != null && Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}