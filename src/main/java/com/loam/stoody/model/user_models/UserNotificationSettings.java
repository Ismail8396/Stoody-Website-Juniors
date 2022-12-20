/*
@fileName:  UserNotificationSettings

@aka:       User Notification Settings model

@purpose:   Contains the data of a user notifications preferences.

@author:    aleemkhowaja

@created:   01.12.2022
*/

package com.loam.stoody.model.user_models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class UserNotificationSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // You will get only notification what have enabled.
    private Boolean isNotificationsOn = true;

    // You will get only those email notification what you want.
    private Boolean isUnusualActivityAlert = true;
    private Boolean isNewBrowserUsedAccountAlert = true;

    // You will get only those email notification what you want.
    private Boolean isSalesAndLatestNewsAlert = true;
    private Boolean isNewFeatureAlert = true;
    private Boolean isNewTipsAlert  = true;

    //You will get only those email notification what you want.
    private Boolean isAnnouncementAlert = false;
    private Boolean isDiscountAlert = true;
    private Boolean isUpdateTeacherDiscussionAlert = false;
    private Boolean isWeeklyRecommendationAlert = true;
    private Boolean isFeatureContentAlert = false;
    private Boolean isNewsletterAlert = false;
    private Boolean isUpComingEventAlert = true;

    //Ui popup always listen the backend service
    //is there is any notification
    // then show notification

    // firebase
    // one signal
    //
}
