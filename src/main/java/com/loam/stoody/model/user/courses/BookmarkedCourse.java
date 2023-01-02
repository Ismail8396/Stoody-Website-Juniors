/*
@fileName:  BookmarkedCourse

@aka:       Bookmarked Course Model

@purpose:   Contains the data (that's either transient or non-transient) of a bookmarked course.
            Bookmarked Course will be linked to User model.

@author:    aleemkhowaja

@created:   16.12.2022
*/

package com.loam.stoody.model.user.courses;

import com.loam.stoody.model.product.course.Course;
import com.loam.stoody.model.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class BookmarkedCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    // TODO: aleemkhowaja, Course should be a collection since BookmarkedCourse is not going to contain only one course.
    @ManyToOne(targetEntity = Course.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
