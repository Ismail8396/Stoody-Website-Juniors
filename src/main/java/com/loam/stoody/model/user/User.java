/*
@fileName:  User

@aka:       User Model

@purpose:   Contains the data (that's either transient or non-transient) of a User.

@hint:      Once a user created, be sure that all related models are appropriately created/initialized.
            (See: UserNotificationSettings, UserSocialLooking, UserVideoHistory)

@author:    OrkhanGG

@created:   01.12.2022
*/

package com.loam.stoody.model.user;

import com.loam.stoody.model.user.misc.Role;
import com.loam.stoody.model.user.statistics.UserStatistics;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Technical
    private boolean isAccountExpired;
    private boolean isAccountLocked;
    private boolean isCredentialsExpired;
    private boolean isAccountEnabled;

    @Column(nullable = false, unique = true)
    private String username;
    // Security
    @Column(nullable = false, unique = true)
    @Email(message = "{errors.invalid_email}")
    private String email;
    @Column(nullable = false)
    private String password;
    private String phoneNumber;
    @Column(name = "multi_factor_auth")
    private boolean multiFactorAuth;


    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")})
    private List<Role> roles;

    // Misc
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_statistics_id", referencedColumnName = "id")
    private UserStatistics userStatistics;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_notifications_id", referencedColumnName = "id")
    private UserNotifications userNotifications;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_privacy_id", referencedColumnName = "id")
    private UserPrivacy userPrivacy;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_profile_settings_id", referencedColumnName = "id")
    private UserProfile userProfile;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_social_profiles_id", referencedColumnName = "id")
    private UserSocialProfiles userSocialProfiles;

    //__________________________________________________________________________________________________________________
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
