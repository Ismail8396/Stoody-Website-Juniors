/*
@fileName:  UserNotificationSettings

@aka:       User Notification Settings model

@purpose:   Contains the data of a user notifications preferences.

@author:    aleemkhowaja

@created:   01.12.2022
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
public class UserNotifications {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // You will get only notification what have enabled.
    private Boolean isNotificationsOn = true;

    // You will get only those email notification what you want.
    private Boolean emailUnusualActivity = true;
    private Boolean emailNewBrowserSignIn = true;

    // You will get only those email notification what you want.
    private Boolean emailLatestSalesAndNews = true;
    private Boolean emailNewFeaturesAndUpdates = true;
    private Boolean emailAboutTips  = true;

    //You will get only those email notification what you want.
    private Boolean notifyClassEvents = false;
    private Boolean notifyTeacherDiscussions = true;
    private Boolean notifyPersonalizedClassRecommendations = false;
    private Boolean notifyFeaturedContent = true;
    private Boolean notifyProductUpdates = false;
    private Boolean notifyEventsAndOffers = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserNotifications that = (UserNotifications) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
