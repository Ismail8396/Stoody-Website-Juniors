package com.loam.stoody.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.List;

import static org.checkerframework.checker.units.UnitsTools.min;

@Entity
@Data// We may do this manually
@NoArgsConstructor
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    @Email(message="{errors.invalid_email}")
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    private String password;

    private String profilePictureURL;

    private String aboutUser;

    private String socialFacebook;
    private String socialInstagram;
    private String socialYoutube;
    private String socialGithub;
    private String socialLinkedIn;
    private String socialTwitter;
    private String city;
    private String country;
    private String phoneNumber;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = {@JoinColumn(name="USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns={@JoinColumn(name="ROLE_ID",referencedColumnName = "ID")}
    )
    private List<Role> roles;

    public User(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.profilePictureURL = user.getProfilePictureURL();
        this.roles = user.getRoles();

        this.aboutUser = user.getAboutUser();
        this.socialFacebook = user.getSocialFacebook();
        this.socialInstagram = user.getSocialInstagram();
        this.socialYoutube = user.getSocialYoutube();
        this.socialGithub = user.getSocialGithub();
        this.socialLinkedIn = user.getSocialLinkedIn();
        this.socialTwitter = user.getSocialTwitter();
        this.city = user.getCity();
        this.country = user.getCountry();
        this.phoneNumber = user.getPhoneNumber();
    }
}
