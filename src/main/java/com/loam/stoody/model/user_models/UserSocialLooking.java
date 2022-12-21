/*
@fileName:  UserSocialLooking

@aka:       User Social Looking model

@purpose:   Contains the data of a user like social links, city, country, DOB and etc.

@author:    OrkhanGG

@created:   20.12.2022
*/

package com.loam.stoody.model.user_models;

import lombok.Data;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class UserSocialLooking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String city;
    private String country;
    private LocalDateTime dateOfBirth;

    private String profilePictureURL;
    private String aboutUser;
    private String socialFacebook;
    private String socialInstagram;
    private String socialYoutube;
    private String socialGithub;
    private String socialLinkedIn;
    private String socialTwitter;
}
