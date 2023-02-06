/*
@fileName:  BookmarkedCourse

@aka:       Bookmarked PendingCourse Model

@purpose:   Contains the data (that's either transient or non-transient) of a bookmarked pendingCourse.
            Bookmarked PendingCourse will be linked to User model.

@author:    aleemkhowaja

@created:   16.12.2022
*/

package com.loam.stoody.model.user.courses;

import com.loam.stoody.model.product.course.pending.PendingCourse;
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

    @ManyToOne(targetEntity = PendingCourse.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private PendingCourse pendingCourse;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
