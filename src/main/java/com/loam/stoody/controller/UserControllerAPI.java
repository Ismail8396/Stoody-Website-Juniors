package com.loam.stoody.controller;

import com.loam.stoody.components.IAuthenticationFacade;
import com.loam.stoody.dto.api.response.OutdoorResponse;
import com.loam.stoody.global.constants.IndoorResponse;
import com.loam.stoody.global.constants.PRL;
import com.loam.stoody.global.logger.ConsoleColors;
import com.loam.stoody.global.logger.StoodyLogger;
import com.loam.stoody.model.user.User;
import com.loam.stoody.model.user.misc.UserStatus;
import com.loam.stoody.service.user.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserControllerAPI {
    private final CustomUserDetailsService customUserDetailsService;
    private final IAuthenticationFacade authenticationFacade;

    @Autowired
    public UserControllerAPI(CustomUserDetailsService customUserDetailsService, IAuthenticationFacade authenticationFacade) {
        this.customUserDetailsService = customUserDetailsService;
        this.authenticationFacade = authenticationFacade;
    }


    /* User Status */
    @PostMapping("/user/status/{status}")
    public OutdoorResponse<?> postUpdateUserStatus(@PathVariable("status") String status){
        User user = null;
        try {
            user = customUserDetailsService.getUserByUsername(authenticationFacade.getAuthentication().getName());
            if(status.equals("setmeonline") && !user.getUserProfile().getUserStatus().equals(UserStatus.Online)) {
                user.getUserProfile().setUserStatus(UserStatus.Online);
                customUserDetailsService.saveUser(user);
            }else if(status.equals("setmeoffline") && !user.getUserProfile().getUserStatus().equals(UserStatus.Offline)) {
                user.getUserProfile().setUserStatus(UserStatus.Offline);
                customUserDetailsService.saveUser(user);
            }else if(status.equals("setmeaway") && !user.getUserProfile().getUserStatus().equals(UserStatus.Away)) {
                user.getUserProfile().setUserStatus(UserStatus.Away);
                customUserDetailsService.saveUser(user);
            }else if(status.equals("setmebusy") && !user.getUserProfile().getUserStatus().equals(UserStatus.Busy)) {
                user.getUserProfile().setUserStatus(UserStatus.Busy);
                customUserDetailsService.saveUser(user);
            }else{
                return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST, "BAD REQUEST");
            }
        } catch (UsernameNotFoundException ignore) {
            return new OutdoorResponse<>(IndoorResponse.FAIL, "FAIL");
        }
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, "SUCCESS");
    }

    @PostMapping(PRL.apiPrefix+"/user/update/picture")
    public OutdoorResponse<?> postUpdatePicture(@RequestParam("pictureURL") String pictureURL) {
        User user = null;
        try {
            user = customUserDetailsService.getUserByUsername(authenticationFacade.getAuthentication().getName());
            user.getUserProfile().setProfilePictureURL(pictureURL);
            customUserDetailsService.saveUser(user);
        } catch (UsernameNotFoundException ignore) {
            return new OutdoorResponse<>(IndoorResponse.FAIL, "FAIL");
        }
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, "SUCCESS");
    }

    @PostMapping(PRL.apiPrefix+"/user/delete/picture")
    public OutdoorResponse<?> postDeletePicture() {
        User user = null;
        try {
            user = customUserDetailsService.getUserByUsername(authenticationFacade.getAuthentication().getName());
            user.getUserProfile().setProfilePictureURL(null);
            customUserDetailsService.saveUser(user);
        } catch (UsernameNotFoundException ignore) {
            return new OutdoorResponse<>(IndoorResponse.FAIL, "FAIL");
        }
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, "SUCCESS");
    }

    //------------------------------------------------------------------------------------------------------------------
    //--> Social Profiles
    @PostMapping(PRL.apiPrefix+"/user/social/profiles/update")
    public OutdoorResponse<?> postSocialProfiles(@RequestParam("twitterURL") String twitterURL,
                                                 @RequestParam("facebookURL") String facebookURL,
                                                 @RequestParam("githubURL")String githubURL,
                                                 @RequestParam("instagramURL") String instagramURL,
                                                 @RequestParam("linkedinURL") String linkedinURL,
                                                 @RequestParam("youtubeURL") String youtubeURL,
                                                 @RequestParam("skills") String skills,
                                                 @RequestParam("aboutUser") String aboutUser){
        User user = null;
        try {
            user = customUserDetailsService.getUserByUsername(authenticationFacade.getAuthentication().getName());

            user.getUserSocialProfiles().setTwitterURL(twitterURL);
            user.getUserSocialProfiles().setFacebookURL(facebookURL);
            user.getUserSocialProfiles().setGithubURL(githubURL);
            user.getUserSocialProfiles().setInstagramURL(instagramURL);
            user.getUserSocialProfiles().setLinkedInURL(linkedinURL);
            user.getUserSocialProfiles().setYoutubeURL(youtubeURL);
            user.getUserSocialProfiles().setSkills(skills);
            user.getUserSocialProfiles().setAboutUser(aboutUser);

            customUserDetailsService.saveUser(user);
        } catch (UsernameNotFoundException ignore) {
            return new OutdoorResponse<>(IndoorResponse.FAIL, "FAIL");
        }

        return new OutdoorResponse<>(IndoorResponse.SUCCESS, "SUCCESS");
    }

    //------------------------------------------------------------------------------------------------------------------
    //-->User Followers
    @PostMapping(PRL.apiPrefix+"/user/social/follow/request")
    @ResponseBody
    public OutdoorResponse<?> followerRequest(@RequestParam("requestType")String requestType,
                                              @RequestParam("follow")String userToFollow,
                                              @RequestParam("candidate")String followerName){
        try {
            User follower = null;
            User toFollow = null;

            if(requestType.equals("follow")) {
                follower = customUserDetailsService.getUserByUsername(followerName);
                toFollow = customUserDetailsService.getUserByUsername(userToFollow);

                if(!authenticationFacade.getAuthentication().getName().equals(follower.getUsername()))
                    throw new RuntimeException();

                if(toFollow.getUserFollowers().contains(follower))
                    throw new RuntimeException();

                toFollow.getUserFollowers().add(follower);
                customUserDetailsService.saveUser(toFollow);
            }
            else if(requestType.equals("unfollow")) {
                follower = customUserDetailsService.getUserByUsername(followerName);
                toFollow = customUserDetailsService.getUserByUsername(userToFollow);

                if(!authenticationFacade.getAuthentication().getName().equals(follower.getUsername()))
                    throw new RuntimeException();

                if(!toFollow.getUserFollowers().contains(follower))
                    throw new RuntimeException();

                toFollow.getUserFollowers().remove(follower);
                customUserDetailsService.saveUser(toFollow);
            } else throw new RuntimeException();

            return new OutdoorResponse<>(IndoorResponse.SUCCESS, "SUCCESS");
        }catch (Exception ignore){
            return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST, "BAD REQUEST");
        }
    }
}
