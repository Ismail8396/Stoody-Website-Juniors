/*
@fileName:  PendingCourse
@aka:       PendingCourse Model
@purpose:   Contains the data (that's either transient or non-transient) of a course.
@author:    OrkhanGG
@created:   16.12.2022
*/

package com.loam.stoody.model.product.course.pending;


import com.loam.stoody.model.product.course.core.CourseCore;
import com.loam.stoody.model.product.course.approved.ApprovedCourse;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

@Entity
@Table(indexes = {
        @Index(name = "courseIndex_id", columnList = "id", unique = true),
})
@Data
@DynamicUpdate
@DynamicInsert
@Where(clause = "is_deleted='false'")
public class PendingCourse extends CourseCore {
}
