/*
@fileName:  CustomUserDetailsService

@aka:       CustomUserDetailsService

@purpose:   The UserDetailsService is a core interface in Spring Security framework,
            which is used to retrieve the user's authentication and authorization information.
            This interface has only one method named loadUserByUsername()
            which we can implement to feed the customer information to the Spring security API.

@hint:      The class that extends the WebSecurityConfigurerAdapter will have this class as a field, so it may be
            used in security configuration.

@author:    OrkhanGG

@created:   01.12.2022
*/

package com.loam.stoody.service.user;

import com.loam.stoody.global.constants.IndoorResponse;
import com.loam.stoody.model.user.*;
import com.loam.stoody.model.user.misc.Role;
import com.loam.stoody.model.user.requests.LoginRequest;
import com.loam.stoody.model.user.statistics.UserStatistics;
import com.loam.stoody.repository.user.LoginRequestRepository;
import com.loam.stoody.repository.user.RoleRepository;
import com.loam.stoody.repository.user.UserRepository;
import com.loam.stoody.repository.user.attributes.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.ZoneId;
import java.util.*;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    // -> Repositories
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final LoginRequestRepository loginRequestRepository;

    private final UserStatisticsRepository userStatisticsRepository;
    private final UserNotificationSettingsRepository userNotificationSettingsRepository;
    private final UserPrivacySettingsRepository userPrivacySettingsRepository;
    private final UserProfileSettingsRepository userProfileSettingsRepository;
    private final UserSocialProfilesSettingsRepository userSocialProfilesSettingsRepository;
    // --> Repositories end

    // Returns simple user without an email, username nor password.
    public User getDefaultUser() {
        User defaultUser = new User();

        defaultUser.setUsername(null);
        defaultUser.setPassword(null);
        defaultUser.setEmail(null);

        //--------------------------------
        // TODO: REMOVE LATER
        if(roleRepository.count() <= 0)
        {
            Role testRole = new Role();
            testRole.setName("ROLE_USER");
            roleRepository.save(testRole);
        }
        System.out.println(roleRepository.findAll());
        //--------------------------------

        defaultUser.setRoles(roleRepository.findBySearchKey("ROLE_USER"));

        //--------------------------------
        // TODO: REMOVE LATER
        System.out.println("USER HAS THESE ROLES: "+defaultUser.getRoles());
        //--------------------------------

        // Linked Models
        defaultUser.setUserStatistics(new UserStatistics());
        defaultUser.setUserNotifications(new UserNotifications());
        defaultUser.setUserPrivacy(new UserPrivacy());
        defaultUser.setUserProfile(new UserProfile());
        defaultUser.setUserSocialProfiles(new UserSocialProfiles());
        defaultUser.setUserFollowers(new ArrayList<>());

        // Misc
        defaultUser.setAccountEnabled(true);
        defaultUser.setAccountExpired(false);
        defaultUser.setAccountLocked(false);
        defaultUser.setCredentialsExpired(false);
        defaultUser.setMultiFactorAuth(false);

        return defaultUser;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByUsername(username);
        user.orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        /**
         * Add Roles for Grant  Permissions
         */
        //@todo implement late
//        List<GrantedAuthority> grantList = Optional.ofNullable(user.get().getRoles()).orElse(Collections.emptyList())
//                .stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
        List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
        grantList.add(grantedAuthority);

        return new org.springframework.security.core.userdetails.User(user.get().getUsername(), user.get().getPassword(), grantList);
    }

    public User getUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findUserByUsername(username);
        userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        User user = userOptional.get();
        return user;
    }

    public Collection<? extends GrantedAuthority> getAuthorities(List<Role> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (CollectionUtils.isEmpty(roles)) throw new RuntimeException("No roles available");
        roles.stream().map(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return authorities;
    }

    public boolean isOTPRequired(String username) {
        final long OTP_VALID_DURATION = 5 * 60 * 1000;   // 5 minutes
        LoginRequest loginRequest = loginRequestRepository.findLoginRequestByUsername(username);
        if (loginRequest.getOneTimePassword() == null) {
            return false;
        }

        long currentTimeInMillis = System.currentTimeMillis();
        long otpRequestedTimeInMillis = loginRequest.getOtpRequestedTime()
                .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        if (otpRequestedTimeInMillis + OTP_VALID_DURATION < currentTimeInMillis) {
            // OTP expires
            return false;
        }
        return true;
    }

    public IndoorResponse doesUserExist(String username, String email) {
        IndoorResponse response = IndoorResponse.SUCCESS;

        if (userRepository.findUserByEmail(email).isPresent())
            response = IndoorResponse.EMAIL_EXIST;

        if (userRepository.findUserByUsername(username).isPresent())
            response = (response == IndoorResponse.EMAIL_EXIST) ? IndoorResponse.USERNAME_EMAIL_EXIST : IndoorResponse.USERNAME_EXIST;

        return response;
    }

    public IndoorResponse saveUser(User user) {
        try {
            userRepository.save(user);
            saveUserStatistics(user.getUserStatistics());
            saveUserNotificationSettings(user.getUserNotifications());
            saveUserPrivacySettings(user.getUserPrivacy());
            saveUserProfileSettings(user.getUserProfile());
            saveUserSocialProfilesSettings(user.getUserSocialProfiles());
        } catch (Exception ignored) {
            return IndoorResponse.FAIL;
        }

        return IndoorResponse.SUCCESS;
    }

    //------------------------------------------------------------------------------------------------------------------
    // --> User related data (Misc)
    public UserStatistics getUserStatisticsById(int id){
        return userStatisticsRepository.findById(id).orElse(null);
    }
    public void saveUserStatistics(UserStatistics userStatistics){
        userStatisticsRepository.save(userStatistics);
    }

    public UserNotifications getUserNotificationSettingsById(int id){
        return userNotificationSettingsRepository.findById(id).orElse(null);
    }
    public void saveUserNotificationSettings(UserNotifications userNotifications){
        userNotificationSettingsRepository.save(userNotifications);
    }

    public UserPrivacy getUserPrivacySettingsById(int id){
        return userPrivacySettingsRepository.findById(id).orElse(null);
    }

    public void saveUserPrivacySettings(UserPrivacy userPrivacy){
        userPrivacySettingsRepository.save(userPrivacy);
    }

    public UserProfile getUserProfileSettingsById(int id){
        return userProfileSettingsRepository.findById(id).orElse(null);
    }

    public void saveUserProfileSettings(UserProfile userProfile){
        userProfileSettingsRepository.save(userProfile);
    }

    public UserSocialProfiles getUserSocialProfilesSettingsById(int id){
        return userSocialProfilesSettingsRepository.findById(id).orElse(null);
    }

    public void saveUserSocialProfilesSettings(UserSocialProfiles userSocialProfiles){
        userSocialProfilesSettingsRepository.save(userSocialProfiles);
    }
}