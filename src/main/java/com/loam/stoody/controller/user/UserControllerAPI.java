package com.loam.stoody.controller.user;

import com.loam.stoody.components.IAuthenticationFacade;
import com.loam.stoody.dto.api.response.OutdoorResponse;
import com.loam.stoody.global.constants.IndoorResponse;
import com.loam.stoody.global.constants.PRL;
import com.loam.stoody.model.user.User;
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

    /* User Status */
    @PostMapping("/user/status/{status}")
    public OutdoorResponse<?> postUpdateUserStatus(@PathVariable("status") String status) {
        User user = null;
        try {
//            user = customUserDetailsService.getUserByUsername(authenticationFacade.getAuthentication().getName());
//            if (status.equals("setmeonline") && !user.getUserProfile().getUserStatus().equals(UserStatus.Online)) {
//                user.getUserProfile().setUserStatus(UserStatus.Online);
//                customUserDetailsService.saveUser(user);
//            } else if (status.equals("setmeoffline") && !user.getUserProfile().getUserStatus().equals(UserStatus.Offline)) {
//                user.getUserProfile().setUserStatus(UserStatus.Offline);
//                customUserDetailsService.saveUser(user);
//            } else if (status.equals("setmeaway") && !user.getUserProfile().getUserStatus().equals(UserStatus.Away)) {
//                user.getUserProfile().setUserStatus(UserStatus.Away);
//                customUserDetailsService.saveUser(user);
//            } else if (status.equals("setmebusy") && !user.getUserProfile().getUserStatus().equals(UserStatus.Busy)) {
//                user.getUserProfile().setUserStatus(UserStatus.Busy);
//                customUserDetailsService.saveUser(user);
//            } else {
//                return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST, "BAD REQUEST");
//            }
        } catch (UsernameNotFoundException ignore) {
            return new OutdoorResponse<>(IndoorResponse.FAIL, "FAIL");
        }
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, "SUCCESS");
    }

    @PostMapping(PRL.apiPrefix + "/user/update/picture")
    public OutdoorResponse<?> postUpdatePicture(@RequestParam("pictureURL") String pictureURL) {
        User user = null;
        try {
            user = customUserDetailsService.getUserByUsername(authenticationFacade.getAuthentication().getName());
//            user.getUserProfile().setProfilePictureURL(pictureURL);
            customUserDetailsService.saveUser(user);
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
//            user.getUserProfile().setProfilePictureURL(null);
            customUserDetailsService.saveUser(user);
        } catch (UsernameNotFoundException ignore) {
            return new OutdoorResponse<>(IndoorResponse.FAIL, "FAIL");
        }
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, "SUCCESS");
    }

    //------------------------------------------------------------------------------------------------------------------
    //--> Social Profiles
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

//            user.getUserSocialProfiles().setTwitterURL(twitterURL);
//            user.getUserSocialProfiles().setFacebookURL(facebookURL);
//            user.getUserSocialProfiles().setGithubURL(githubURL);
//            user.getUserSocialProfiles().setInstagramURL(instagramURL);
//            user.getUserSocialProfiles().setLinkedInURL(linkedinURL);
//            user.getUserSocialProfiles().setYoutubeURL(youtubeURL);
//            user.getUserSocialProfiles().setSkills(skills);
//            user.getUserSocialProfiles().setAboutUser(aboutUser);

            customUserDetailsService.saveUser(user);
        } catch (UsernameNotFoundException ignore) {
            return new OutdoorResponse<>(IndoorResponse.FAIL, "FAIL");
        }

        return new OutdoorResponse<>(IndoorResponse.SUCCESS, "SUCCESS");
    }

    // Follower
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
