package com.loam.stoody.model.user;

import org.hibernate.Hibernate;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
public class UserSocialProfiles {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Social Profiles
    private String socialFacebook;
    private String socialInstagram;
    private String socialYoutube;
    private String socialGithub;
    private String socialLinkedIn;
    private String socialTwitter;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserSocialProfiles that = (UserSocialProfiles) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
