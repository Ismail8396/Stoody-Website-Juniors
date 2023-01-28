package com.loam.stoody.controller.user;

import com.loam.stoody.components.IAuthenticationFacade;
import com.loam.stoody.controller.product.CourseController;
import com.loam.stoody.dto.api.request.SearchFilterDTO;
import com.loam.stoody.dto.api.response.OutdoorResponse;
import com.loam.stoody.enums.CourseStatus;
import com.loam.stoody.global.constants.IndoorResponse;
import com.loam.stoody.global.constants.MiscConstants;
import com.loam.stoody.global.constants.PRL;
import com.loam.stoody.global.logger.ConsoleColors;
import com.loam.stoody.global.logger.StoodyLogger;
import com.loam.stoody.model.product.course.Course;
import com.loam.stoody.model.user.User;
import com.loam.stoody.model.user.UserNotifications;
import com.loam.stoody.model.user.UserPrivacy;
import com.loam.stoody.model.user.UserProfile;
import com.loam.stoody.repository.user.UserRepository;
import com.loam.stoody.service.i18n.LanguageService;
import com.loam.stoody.service.product.CourseService;
import com.loam.stoody.service.user.CustomUserDetailsService;
import com.loam.stoody.service.user.UserDTS;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
class UserController {
    private final CustomUserDetailsService customUserDetailsService;
    private final IAuthenticationFacade authenticationFacade;
    private final LanguageService languageService;
    private final UserDTS userDTS;
    private final CourseService courseService;
    private final UserRepository userRepository;

    @ModelAttribute("getUserDTS")
    public UserDTS getUserDTS() {
        return userDTS;
    }

    @ModelAttribute("courseService")
    public CourseService getCourseService() {
        return courseService;
    }

    @ModelAttribute("languageServiceLayer")
    public LanguageService getLanguageServiceLayer() {
        return languageService;
    }

    //------------------------------------------------------------------------------------------------------------------

    @GetMapping("/users/{username}")
    public String getUserProfileDisplayPage(@PathVariable("username") String username, Model model) {
        User userToPreview = null;
        try {
            customUserDetailsService.getUserByUsername(authenticationFacade.getAuthentication().getName());
            userToPreview = customUserDetailsService.getUserByUsername(username);
        } catch (UsernameNotFoundException ignore) {
            StoodyLogger.DebugLog(ConsoleColors.RED, "User was null!");
            return "redirect:" + PRL.error404URL;
        }
        model.addAttribute("mainPreviewUserInfo", userToPreview);
        return "pages/instructor-profile";
    }

/*
 +-+-+-+-+-+-+-+-+-+-+
 |I|N|S|T|R|U|C|T|O|R|
 +-+-+-+-+-+-+-+-+-+-+
*/
    // -> Courses
    @GetMapping("/user/dashboard/instructor/courses/search")
    public String getInstructorCreatedCoursesPage(@ModelAttribute("searchFilterDTO")SearchFilterDTO searchFilterDTO,Model model){
        // Find only instructor's courses.
        if(searchFilterDTO == null)
            model.addAttribute("searchFilterDTO",new SearchFilterDTO());
        else
            model.addAttribute("searchFilterDTO",searchFilterDTO);

        model.addAttribute("mainPreviewUserInfo", userDTS.getCurrentUser());

        model.addAttribute("createdCourses",courseService.getAllCourses());
        return "pages/instructor-courses";
    }

    @GetMapping("/user/dashboard/instructor/courses/tag/deleted/{id}")
    public String postInstructorTagCourseDeleted(@PathVariable("id")Long id){
        Course course = courseService.getCourseEntityById(id);
        if(course != null && course.getAuthor() != null && customUserDetailsService.compareUsers(course.getAuthor(),customUserDetailsService.getCurrentUser()) ) {
            course.setCourseStatus(CourseStatus.Deleted);
            courseService.saveEntity(course);
            return "redirect:/user/dashboard/instructor/courses/search";
        }
        return "redirect:"+PRL.error404URL;
    }
    // -> Courses End

    @GetMapping("/user/dashboard/standard/profile/edit")
    public String getUserProfileEditPage(Model model) {
        User user = null;
        try {
            user = customUserDetailsService.getUserByUsername(authenticationFacade.getAuthentication().getName());
        } catch (UsernameNotFoundException ignore) {
            StoodyLogger.DebugLog(ConsoleColors.RED, "User was null!");
            return "redirect:" + PRL.error404URL;
        }

        UserProfile userProfile = customUserDetailsService.getUserProfile(customUserDetailsService.getUserByUsername(user.getUsername()));
        model.addAttribute("userProfileObject", userProfile);
        model.addAttribute("mainPreviewUserInfo", user);
        model.addAttribute("countries", MiscConstants.countries);
        model.addAttribute("states", MiscConstants.states);
        return "pages/profile-edit";
    }

    @PostMapping("/user/dashboard/standard/profile/edit")
    public String postUserProfileEditPage(@ModelAttribute("userProfileObject") UserProfile userProfileObject) {
        if (userProfileObject == null) {
            StoodyLogger.DebugLog(ConsoleColors.RED, "User was null!");
            return "redirect:" + PRL.error404URL;
        }
        User user = null;
        try {
            user = customUserDetailsService.getUserByUsername(authenticationFacade.getAuthentication().getName());
            userProfileObject.setUser(user);
            customUserDetailsService.saveUserProfile(userProfileObject);
        } catch (UsernameNotFoundException ignore) {
            StoodyLogger.DebugLog(ConsoleColors.RED, "User was null!");
            return "redirect:" + PRL.error404URL;
        }

        return "redirect:/user/dashboard/standard/profile/edit";
    }

    @GetMapping("/user/dashboard/standard/profile/security")
    public String getUserSecuritySettingsPage(Model model) {
        try {
            User user = customUserDetailsService.getUserByUsername(authenticationFacade.getAuthentication().getName());
            model.addAttribute("mainPreviewUserInfo", user);
        } catch (UsernameNotFoundException ignore) {
            StoodyLogger.DebugLog(ConsoleColors.RED, "User was null!");
            return "redirect:" + PRL.error404URL;
        }
        return "pages/security";
    }

    @GetMapping("/user/dashboard/standard/profile/social")
    public String getUserSocialProfilesSettingsPage(Model model) {
        try {
            User user = customUserDetailsService.getUserByUsername(authenticationFacade.getAuthentication().getName());
            model.addAttribute("mainPreviewUserInfo", user);
        } catch (UsernameNotFoundException ignore) {
            StoodyLogger.DebugLog(ConsoleColors.RED, "User was null!");
            return "redirect:" + PRL.error404URL;
        }
        return "pages/social-profile";
    }

    @GetMapping("/user/dashboard/standard/profile/notifications")
    public String getUserNotificationSettings(Model model) {
        try {
            User user = customUserDetailsService.getUserByUsername(authenticationFacade.getAuthentication().getName());
            model.addAttribute("notificationsObject", customUserDetailsService.getUserNotifications(user));
            model.addAttribute("mainPreviewUserInfo", user);
        } catch (UsernameNotFoundException ignore) {
            StoodyLogger.DebugLog(ConsoleColors.RED, "User was null!");
            return "redirect:" + PRL.error404URL;
        }

        return "pages/notifications";
    }

    @PostMapping("/user/dashboard/standard/profile/notifications")
    public String postUserNotificationSettings(@ModelAttribute("notificationsObject") UserNotifications userNotifications,
                                               Model model) {
        try {
            User user = customUserDetailsService.getUserByUsername(authenticationFacade.getAuthentication().getName());
            userNotifications.setUser(user);
            customUserDetailsService.saveUserNotifications(userNotifications);
        } catch (UsernameNotFoundException ignore) {
            StoodyLogger.DebugLog(ConsoleColors.RED, "User was null!");
            return "redirect:" + PRL.error404URL;
        }

        return "redirect:/user/dashboard/standard/profile/notifications";
    }

    @GetMapping("/user/dashboard/standard/profile/privacy")
    public String getUserSettings(Model model) {
        try {
            User user = customUserDetailsService.getUserByUsername(authenticationFacade.getAuthentication().getName());

            model.addAttribute("profilePrivacyObject", customUserDetailsService.getUserPrivacy(user));
            model.addAttribute("mainPreviewUserInfo", user);
        } catch (UsernameNotFoundException ignore) {
            StoodyLogger.DebugLog(ConsoleColors.RED, "User was null!");
            return "redirect:" + PRL.error404URL;
        }
        return "pages/profile-privacy";
    }

    @PostMapping("/user/dashboard/standard/profile/privacy")
    public String postUserSettings(@ModelAttribute("profilePrivacyObject") UserPrivacy userPrivacy, Model model) {
        try {
            User user = customUserDetailsService.getUserByUsername(authenticationFacade.getAuthentication().getName());
            userPrivacy.setUser(user);
            customUserDetailsService.saveUserPrivacy(userPrivacy);
            model.addAttribute("mainPreviewUserInfo", user);
        } catch (UsernameNotFoundException ignore) {
            StoodyLogger.DebugLog(ConsoleColors.RED, "User was null!");
            return "redirect:" + PRL.error404URL;
        }
        return "redirect:/user/dashboard/standard/profile/privacy";
    }

    @GetMapping("/user/dashboard/standard/profile/delete")
    public String getUserDeleteProfile(Model model) {
        try {
            User user = customUserDetailsService.getUserByUsername(authenticationFacade.getAuthentication().getName());
            model.addAttribute("mainPreviewUserInfo", user);
        } catch (UsernameNotFoundException ignore) {
            StoodyLogger.DebugLog(ConsoleColors.RED, "User was null!");
            return "redirect:" + PRL.error404URL;
        }
        return "pages/delete-profile";
    }

    @GetMapping("/user/dashboard/standard/profile/accounts")
    public String getUserLinkedAccounts(Model model) {
        try {
            User user = customUserDetailsService.getUserByUsername(authenticationFacade.getAuthentication().getName());
            model.addAttribute("mainPreviewUserInfo", user);
        } catch (UsernameNotFoundException ignore) {
            StoodyLogger.DebugLog(ConsoleColors.RED, "User was null!");
            return "redirect:" + PRL.error404URL;
        }
        return "pages/linked-accounts";
    }

    @PostMapping("/c/l/o/s/e/m/y/a/c/c/o/u/n/t/p/l/e/a/s/e")
    public String postDeleteMyAccount(){
        return "redirect:/logout";
    }
}
