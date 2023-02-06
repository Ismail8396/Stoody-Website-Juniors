/*
@fileName:  PurchasedCourse

@aka:       Purchased PendingCourse model

@purpose:   Contains the data (that's either transient or non-transient) of a purchased pendingCourse.

@author:    aleemkhowaja

@created:   16.12.2022
*/

package com.loam.stoody.model.user.courses;

import com.loam.stoody.enums.PaymentMethod;
import com.loam.stoody.model.ParentModel;
import com.loam.stoody.model.product.course.pending.PendingCourse;
import com.loam.stoody.model.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class PurchasedCourse extends ParentModel {

    // TODO: aleemkhowaja, PendingCourse should be a collection since PurchasedCourse is not going to contain only one pendingCourse.
    @ManyToOne(targetEntity = PendingCourse.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private PendingCourse pendingCourse;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private  User user;

    private Long paidAmount;

    private PaymentMethod paymentMethod;

}
