/*
@fileName:  CourseHistory

@aka:       Course History Model

@purpose:   Contains the data (that's either transient or non-transient) of a course history model.
            Course History will be linked to User model.

@author:    aleemkhowaja

@created:   16.12.2022
*/

package com.loam.stoody.model.user_models;

import com.loam.stoody.model.ParentModel;
import com.loam.stoody.model.product_models.course_models.Course;
import com.loam.stoody.model.user_models.User;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class CourseHistory extends ParentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // TODO: aleemkhowaja, Course should be a collection since CourseHistory is not going to contain only one course.
    @ManyToOne(targetEntity = Course.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private  User user;

}
