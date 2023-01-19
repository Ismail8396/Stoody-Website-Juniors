/*
@fileName:  UserSocialLooking

@aka:       User Social Looking model

@purpose:   Contains the data of a user like social links, city, country, DOB and etc.

@author:    OrkhanGG

@created:   20.12.2022
*/

package com.loam.stoody.model.user;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
public class UserPrivacy {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Privacy
    private Boolean showProfileOnSearchEngines = true;
    private Boolean showCoursesBought = false;
    private Boolean showProfilePublic = true;
    private Boolean showCurrentlyLearning = true;
    private Boolean showCompletedCourses = true;
    private Boolean showInterests = true;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserPrivacy that = (UserPrivacy) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
