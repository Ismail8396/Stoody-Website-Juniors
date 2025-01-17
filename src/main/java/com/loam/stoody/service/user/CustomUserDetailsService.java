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

import com.loam.stoody.components.IAuthenticationFacade;
import com.loam.stoody.enums.IndoorResponse;
import com.loam.stoody.model.user.*;
import com.loam.stoody.model.user.misc.Role;
import com.loam.stoody.model.user.misc.UserStatus;
import com.loam.stoody.model.user.requests.LoginRequest;
import com.loam.stoody.model.user.statistics.UserStatistics;
import com.loam.stoody.repository.user.LoginRequestRepository;
import com.loam.stoody.repository.user.RoleRepository;
import com.loam.stoody.repository.user.UserRepository;
import com.loam.stoody.repository.user.attributes.*;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    // -> Services
    private final UserRoleService userRoleService;
    // -> Repositories
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final LoginRequestRepository loginRequestRepository;
    private final IAuthenticationFacade authenticationFacade;

    private final UserStatisticsRepository userStatisticsRepository;
    private final UserNotificationSettingsRepository userNotificationSettingsRepository;
    private final UserPrivacySettingsRepository userPrivacySettingsRepository;
    private final UserProfileSettingsRepository userProfileSettingsRepository;
    private final UserSocialProfilesSettingsRepository userSocialProfilesSettingsRepository;
    private final UserFollowersRepository userFollowersRepository;

    // --> Repositories end

    // Returns simple user without an email, username nor password.
    public User getDefaultUser() {
        User defaultUser = new User();

        defaultUser.setUsername(null);
        defaultUser.setPassword(null);
        defaultUser.setEmail(null);

        defaultUser.setRoles(roleRepository.findBySearchKey("ROLE_USER"));

        // Misc
        defaultUser.setAccountEnabled(true);
        defaultUser.setAccountExpired(false);
        defaultUser.setAccountLocked(false);
        defaultUser.setCredentialsExpired(false);
        defaultUser.setMultiFactorAuth(false);

        return defaultUser;
    }

    public Collection<? extends GrantedAuthority> getAuthorities(List<Role> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (CollectionUtils.isEmpty(roles)) throw new RuntimeException("No roles available");
        roles.stream().map(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return authorities;
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

    public IndoorResponse userExist(String username, String email) {
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
        } catch (Exception ignored) {
            return IndoorResponse.FAIL;
        }
        return IndoorResponse.SUCCESS;
    }

    public User getCurrentUser() {
        User user = null;
        try {
            user = getUserByUsername(authenticationFacade.getAuthentication().getName());
        } catch (UsernameNotFoundException ignore) {
            return null;
        }
        return user;
    }

    public Boolean compareUsers(User user, User toCompare) {
        if (user == null || toCompare == null) return false;
        return user.getUsername().equals(toCompare.getUsername());
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
        // OTP expires
        return otpRequestedTimeInMillis + OTP_VALID_DURATION >= currentTimeInMillis;
    }

    // -> Roles
    public boolean hasRole(String username, String role){
        try{
            User user = getUserByUsername(username);
            if(!role.contains("ROLE_"))
                role = "ROLE_"+role;

            String finalRole = role;
            return user.getRoles().stream().anyMatch(e->e.getName().equals(finalRole));

        }catch(UsernameNotFoundException ignore){}
        return false;
    }

    public boolean addRole(String username, String role){
        if(hasRole(username,role))
            return false;

        if(!role.contains("ROLE_"))
            role = "ROLE_"+role;

        String finalRole = role;
        if(roleRepository.findAll().stream().noneMatch(e->e.getName().equals(finalRole)))
            return false;

        try{
            User user = getUserByUsername(username);
            user.getRoles().add(roleRepository.findAll().stream().filter(e->e.getName().equals(finalRole)).toList().get(0));
            saveUser(user);
            return true;
        }catch(UsernameNotFoundException ignore){}

        return false;
    }

    // -> Followers
    public List<User> getUserFollowers(User user) {
        return userFollowersRepository.findAll().stream()
                .filter(e -> e.getTo().getUsername().equals(user.getUsername()))
                .map(UserFollowers::getFrom).collect(Collectors.toList());
    }

    public List<User> getUserFollowings(User user) {
        return userFollowersRepository.findAll().stream()
                .filter(e -> e.getFrom().getUsername().equals(user.getUsername()))
                .map(UserFollowers::getTo).collect(Collectors.toList());
    }

    public boolean addUserFollower(User userFrom, User userTo) {
        try {
            if (userFrom == null || userTo == null)
                throw new RuntimeException();

            final String userFromUsername = userFrom.getUsername();
            final String userToUsername = userTo.getUsername();

            // Check whether they exist or not. If not, throw exception
            getUserByUsername(userFromUsername);
            getUserByUsername(userToUsername);

            if (userFollowersRepository.findAll().stream()
                    .anyMatch(e -> e.getFrom().getUsername().equals(userFromUsername) &&
                            e.getTo().getUsername().equals(userToUsername)))
                throw new RuntimeException();

            UserFollowers userFollowers = new UserFollowers();
            userFollowers.setFrom(userFrom);
            userFollowers.setTo(userTo);// userFrom wants to follow userTo
            userFollowersRepository.save(userFollowers);
        } catch (RuntimeException ignore) {
            return false;
        }
        return true;
    }

    public boolean removeUserFollower(User userFrom, User userTo) {
        try {
            if (userFrom == null || userTo == null)
                throw new RuntimeException();

            final String userFromUsername = userFrom.getUsername();
            final String userToUsername = userTo.getUsername();

            // Check whether they exist or not. If not, throw exception
            getUserByUsername(userFromUsername);
            getUserByUsername(userToUsername);

            UserFollowers unfollow = userFollowersRepository.findAll().stream().filter(e -> e.getFrom().getUsername().equals(userFromUsername) &&
                    e.getTo().getUsername().equals(userToUsername)).findFirst().orElse(null);

            if (unfollow == null) throw new RuntimeException();

            userFollowersRepository.delete(unfollow);
        } catch (RuntimeException ignore) {
            return false;
        }
        return true;
    }

    public boolean isUserFollowedBy(User userFrom, User userTo) {
        try {
            if (userFrom == null || userTo == null) throw new RuntimeException();

            final String userFromUsername = userFrom.getUsername();
            final String userToUsername = userTo.getUsername();

            // Check whether they exist or not. If not, throw exception
            getUserByUsername(userFromUsername);
            getUserByUsername(userToUsername);

            if (userFollowersRepository.findAll().stream().filter(e -> e.getFrom().getUsername().equals(userFromUsername) &&
                    e.getTo().getUsername().equals(userToUsername)).findFirst().orElse(null)
                    == null) throw new RuntimeException();
        } catch (RuntimeException ignore) {
            return false;
        }
        return true;
    }

    // -> User Security
    public boolean isPhoneNumberInUse(String phoneNumber) {
        return userRepository.findAll().stream().anyMatch(e-> e.getPhoneNumber() != null && e.getPhoneNumber().equals(phoneNumber));
    }

    public boolean isEmailInUse(String email) {
        return userRepository.findAll().stream().anyMatch(e-> e.getEmail() != null && e.getEmail().equals(email));
    }

    // -> User Statistics
    public void saveUserStatistics(UserStatistics userStatistics) {
        userStatisticsRepository.save(userStatistics);
    }

    public UserStatistics getUserStatistics(User user) {
        List<UserStatistics> userStatistics = userStatisticsRepository.findAll().stream().filter(e -> e.getUser().getUsername().equals(user.getUsername())).collect(Collectors.toList());
        if (userStatistics.isEmpty()) {
            return new UserStatistics();
        }

        return userStatistics.get(0);
    }

    public String getUserRoleBadgeName(String username){
        return userRoleService.getUserMostPrioritizedRole(getUserByUsername(username)).toString().replace("ROLE_","");
    }

    // -> User Notifications
    public void saveUserNotifications(UserNotifications userNotifications){
        userNotificationSettingsRepository.save(userNotifications);
    }

    public UserNotifications getUserNotifications(User user) {
        if(userNotificationSettingsRepository.findAll().stream().noneMatch(e->e.getUser().getUsername().equals(user.getUsername()))) {
            UserNotifications userNotifications = new UserNotifications(null,user,true,true,true,true,true,true,false,true,false,true,false,false);
            saveUserNotifications(userNotifications);
        }
        List<UserNotifications> userNotifications = userNotificationSettingsRepository.findAll().stream().filter(e -> e.getUser().getUsername().equals(user.getUsername())).toList();
        return userNotifications.get(0);
    }

    // -> User Privacy
    public void saveUserPrivacy(UserPrivacy userPrivacy){
        userPrivacySettingsRepository.save(userPrivacy);
    }

    public UserPrivacy getUserPrivacy(User user){
        // If it does not exist, create one for the user.
        if(userPrivacySettingsRepository.findAll().stream().noneMatch(e->e.getUser().getUsername().equals(user.getUsername()))) {
            UserPrivacy userPrivacy = new UserPrivacy(null,user,true,false,true,true,true);
            saveUserPrivacy(userPrivacy);
        }
        List<UserPrivacy> userPrivacies = userPrivacySettingsRepository.findAll().stream().filter(e -> e.getUser().getUsername().equals(user.getUsername())).toList();
        return userPrivacies.get(0);
    }

    // -> User Profile
    public void saveUserProfile(UserProfile userProfile){
        userProfileSettingsRepository.save(userProfile);
    }

    // TODO: Create this logic for all of separated user models
    public UserProfile getUserProfile(User user) {
        // If it does not exist, create one for the user.
        if(userProfileSettingsRepository.findAll().stream().noneMatch(e->e.getUser().getUsername().equals(user.getUsername()))) {
            UserProfile userProfile = new UserProfile(null,user,null,Strings.EMPTY,Strings.EMPTY,Strings.EMPTY,Strings.EMPTY,Strings.EMPTY,Strings.EMPTY,Strings.EMPTY, UserStatus.Online);
            saveUserProfile(userProfile);
        }

        List<UserProfile> userProfiles = userProfileSettingsRepository.findAll().stream().filter(e -> e.getUser().getUsername().equals(user.getUsername())).toList();

        return userProfiles.get(0);
    }

    // -> User Social Profiles
    public void saveUserSocialProfiles(UserSocialProfiles userSocialProfiles){
        userSocialProfilesSettingsRepository.save(userSocialProfiles);
    }

    public UserSocialProfiles getUserSocialProfiles(User user) {
        // If it does not exist, create one for the user.
        if(userSocialProfilesSettingsRepository.findAll().stream().noneMatch(e->e.getUser().getUsername().equals(user.getUsername()))) {
            UserSocialProfiles userSocialProfiles = new UserSocialProfiles(null,user,Strings.EMPTY,Strings.EMPTY,Strings.EMPTY,Strings.EMPTY,Strings.EMPTY,Strings.EMPTY,Strings.EMPTY,Strings.EMPTY);
            saveUserSocialProfiles(userSocialProfiles);
        }

        List<UserSocialProfiles> userSocialProfiles = userSocialProfilesSettingsRepository.findAll().stream().filter(e -> e.getUser().getUsername().equals(user.getUsername())).toList();

        return userSocialProfiles.get(0);
    }
}
