/*
@fileName:  VisitedCourseHistory

@aka:       PendingCourse History Model

@purpose:   Contains the data (that's either transient or non-transient) of a pendingCourse history model.
            PendingCourse History will be linked to User model.

@author:    aleemkhowaja

@created:   16.12.2022
*/

package com.loam.stoody.model.user.courses;

import com.loam.stoody.model.ParentModel;
import com.loam.stoody.model.product.course.pending.PendingCourse;
import com.loam.stoody.model.user.User;
import lombok.Data;

import jakarta.persistence.*;

@Data
@Entity
public class VisitedCourseHistory extends ParentModel {

    @ManyToOne(targetEntity = PendingCourse.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private PendingCourse pendingCourse;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private  User user;

}
