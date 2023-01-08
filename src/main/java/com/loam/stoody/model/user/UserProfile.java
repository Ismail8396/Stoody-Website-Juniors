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

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(mappedBy = "userProfile")
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

    public String userStatusCSSClass(){
        if(userStatus.equals(UserStatus.Online)){
            return "avatar-indicators avatar-online";
        }
        else if(userStatus.equals(UserStatus.Offline)){
            return "avatar-indicators avatar-offline";
        }
        else if(userStatus.equals(UserStatus.Away)){
            return "avatar-indicators avatar-away";
        }
        else if(userStatus.equals(UserStatus.Busy)){
            return "avatar-indicators avatar-busy";
        }else{
            return "avatar-indicators avatar-offline";
        }
    }

    @Transient
    public String getUserDisplayName(){
        if(firstName != null && lastName != null)
            if(!firstName.isBlank() && !lastName.isBlank())
                return firstName+" "+lastName;
        return user.getUsername();
    }

    @Transient
    public String getUserStatus(){
        if(userStatus != null)
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
