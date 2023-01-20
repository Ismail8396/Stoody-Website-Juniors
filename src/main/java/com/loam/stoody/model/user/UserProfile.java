/*
@fileName:  UserSocialLooking

@aka:       User Social Looking model

@purpose:   Contains the data of a user like social links, city, country, DOB and etc.

@author:    OrkhanGG

@created:   20.12.2022
*/

package com.loam.stoody.model.user;

import com.loam.stoody.model.user.misc.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Misc
    private String profilePictureURL;
    private String firstName;
    private String lastName;
    private String dateOfBirth;

    // Location
    private String addressLine;
    private String addressLineAddition;
    private String state;
    private String country;

    private UserStatus userStatus = UserStatus.Online;

    @Transient
    public String getUserStatus() {
        if (userStatus != null)
            return userStatus.toString();
        return "Offline";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserProfile that = (UserProfile) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
