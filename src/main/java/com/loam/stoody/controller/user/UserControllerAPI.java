package com.loam.stoody.controller.user;

import com.loam.stoody.components.IAuthenticationFacade;
import com.loam.stoody.dto.api.response.OutdoorResponse;
import com.loam.stoody.global.constants.IndoorResponse;
import com.loam.stoody.global.constants.PRL;
import com.loam.stoody.model.user.User;
import com.loam.stoody.model.user.UserProfile;
import com.loam.stoody.model.user.UserSocialProfiles;
import com.loam.stoody.model.user.misc.UserStatus;
import com.loam.stoody.repository.user.attributes.UserFollowersRepository;
import com.loam.stoody.service.user.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class UserControllerAPI {
    private final CustomUserDetailsService customUserDetailsService;
    private final IAuthenticationFacade authenticationFacade;

    // -> User Status API
    @PostMapping("/user/status/{status}")
    public OutdoorResponse<?> postUpdateUserStatus(@PathVariable("status") String status) {
        User user = null;
        try {
            user = customUserDetailsService.getUserByUsername(authenticationFacade.getAuthentication().getName());

            UserProfile userProfile = customUserDetailsService.getUserProfile(user);
            if (status.equals("setmeonline") &&
                    !userProfile.getUserStatus().equals(UserStatus.Online.toString())) {
                userProfile.setUserStatus(UserStatus.Online);
            } else if (status.equals("setmeoffline") &&
                    !userProfile.getUserStatus().equals(UserStatus.Offline.toString())) {
                userProfile.setUserStatus(UserStatus.Offline);
            } else if (status.equals("setmeaway") &&
                    !userProfile.getUserStatus().equals(UserStatus.Away.toString())) {
                userProfile.setUserStatus(UserStatus.Away);
            } else if (status.equals("setmebusy") &&
                    !userProfile.getUserStatus().equals(UserStatus.Busy.toString())) {
                userProfile.setUserStatus(UserStatus.Busy);
            } else {
                throw new RuntimeException();
            }

            customUserDetailsService.saveUserProfile(userProfile);
        } catch (UsernameNotFoundException ignore) {
            return new OutdoorResponse<>(IndoorResponse.FAIL, "FAIL");
        }catch(RuntimeException ignore){
            return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST, "BAD REQUEST");
        }
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, "SUCCESS");
    }

    // -> User Profile API
    @PostMapping(PRL.apiPrefix + "/user/update/picture")
    public OutdoorResponse<?> postUpdatePicture(@RequestParam("pictureURL") String pictureURL) {
        User user = null;
        try {
            user = customUserDetailsService.getUserByUsername(authenticationFacade.getAuthentication().getName());
            UserProfile userProfile = customUserDetailsService.getUserProfile(customUserDetailsService.getUserByUsername(user.getUsername()));
            userProfile.setProfilePictureURL(pictureURL);
            customUserDetailsService.saveUserProfile(userProfile);
        } catch (UsernameNotFoundException ignore) {
            return new OutdoorResponse<>(IndoorResponse.FAIL, "FAIL");
        }
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, "SUCCESS");
    }

    @PostMapping(PRL.apiPrefix + "/user/delete/picture")
    public OutdoorResponse<?> postDeletePicture() {
        User user = null;
        try {
            user = customUserDetailsService.getUserByUsername(authenticationFacade.getAuthentication().getName());
            UserProfile userProfile = customUserDetailsService.getUserProfile(customUserDetailsService.getUserByUsername(user.getUsername()));
            userProfile.setProfilePictureURL(null);
            customUserDetailsService.saveUserProfile(userProfile);
        } catch (UsernameNotFoundException ignore) {
            return new OutdoorResponse<>(IndoorResponse.FAIL, "FAIL");
        }
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, "SUCCESS");
    }

    //------------------------------------------------------------------------------------------------------------------
    //-> Social Profiles API
    @PostMapping(PRL.apiPrefix + "/user/social/profiles/update")
    public OutdoorResponse<?> postSocialProfiles(@RequestParam("twitterURL") String twitterURL,
                                                 @RequestParam("facebookURL") String facebookURL,
                                                 @RequestParam("githubURL") String githubURL,
                                                 @RequestParam("instagramURL") String instagramURL,
                                                 @RequestParam("linkedinURL") String linkedinURL,
                                                 @RequestParam("youtubeURL") String youtubeURL,
                                                 @RequestParam("skills") String skills,
                                                 @RequestParam("aboutUser") String aboutUser) {
        User user = null;
        try {
            user = customUserDetailsService.getUserByUsername(authenticationFacade.getAuthentication().getName());

            UserSocialProfiles userSocialProfiles = customUserDetailsService.getUserSocialProfiles(user);
            userSocialProfiles.setTwitterURL(twitterURL);
            userSocialProfiles.setFacebookURL(facebookURL);
            userSocialProfiles.setGithubURL(githubURL);
            userSocialProfiles.setInstagramURL(instagramURL);
            userSocialProfiles.setLinkedInURL(linkedinURL);
            userSocialProfiles.setYoutubeURL(youtubeURL);
            userSocialProfiles.setSkills(skills);
            userSocialProfiles.setAboutUser(aboutUser);

            // Save changes
            customUserDetailsService.saveUserSocialProfiles(userSocialProfiles);
        } catch (UsernameNotFoundException ignore) {
            return new OutdoorResponse<>(IndoorResponse.FAIL, "FAIL");
        }

        return new OutdoorResponse<>(IndoorResponse.SUCCESS, "SUCCESS");
    }

    // -> Follower API
    @PostMapping(PRL.apiPrefix + "/user/follow/add")
    public OutdoorResponse<?> postUserFollow(@RequestParam("userFrom")String userFrom,
                                             @RequestParam("userTo")String userTo) {
        User from = null;
        User to = null;
        try {
            from = customUserDetailsService.getUserByUsername(authenticationFacade.getAuthentication().getName());
            to = customUserDetailsService.getUserByUsername(userTo);

            if(!from.getUsername().equals(userFrom) || !to.getUsername().equals(userTo))
                throw new RuntimeException();

            if(customUserDetailsService.addUserFollower(from,to))
                throw new RuntimeException();
        } catch (UsernameNotFoundException ignore) {
            return new OutdoorResponse<>(IndoorResponse.FAIL, "FAIL");
        }
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, "SUCCESS");
    }

    @PostMapping(PRL.apiPrefix + "/user/follow/remove")
    public OutdoorResponse<?> postUserUnfollow(@RequestParam("userFrom")String userFrom,
                                             @RequestParam("userTo")String userTo) {
        User from = null;
        User to = null;
        try {
            from = customUserDetailsService.getUserByUsername(authenticationFacade.getAuthentication().getName());
            to = customUserDetailsService.getUserByUsername(userTo);

            if(!from.getUsername().equals(userFrom) || !to.getUsername().equals(userTo))
                throw new RuntimeException();

            if(customUserDetailsService.removeUserFollower(from,to))
                throw new RuntimeException();
        } catch (UsernameNotFoundException ignore) {
            return new OutdoorResponse<>(IndoorResponse.FAIL, "FAIL");
        }
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, "SUCCESS");
    }
}
