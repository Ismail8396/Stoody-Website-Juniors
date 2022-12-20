/*
@fileName:  Comment

@aka:       Comment Model

@purpose:   Contains the data (that's either transient or non-transient) of a comment.

@author:    OrkhanGG

@created:   01.12.2022
*/

package com.loam.stoody.model.communication_models.misc;

import com.loam.stoody.model.product_models.course_models.Course;
import com.loam.stoody.model.user_models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String content;
    private Integer likeCount;
    private Integer disLikeCount;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToMany
    @JoinColumn(name = "replies_id")
    private Set<Comment> replies;
}