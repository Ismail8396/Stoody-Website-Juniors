package com.loam.stoody.controller.user;

import com.loam.stoody.components.IAuthenticationFacade;
import com.loam.stoody.dto.api.request.SearchFilterDTO;
import com.loam.stoody.dto.api.request.course.pending.PendingCourseDTO;
import com.loam.stoody.enums.CourseStatus;
import com.loam.stoody.global.constants.MiscConstants;
import com.loam.stoody.global.constants.PRL;
import com.loam.stoody.global.logger.ConsoleColors;
import com.loam.stoody.global.logger.StoodyLogger;
import com.loam.stoody.model.product.course.pending.PendingCourse;
import com.loam.stoody.model.user.User;
import com.loam.stoody.model.user.UserNotifications;
import com.loam.stoody.model.user.UserPrivacy;
import com.loam.stoody.model.user.UserProfile;
import com.loam.stoody.repository.user.UserRepository;
import com.loam.stoody.service.i18n.LanguageService;
import com.loam.stoody.service.product.CategoryService;
import com.loam.stoody.service.product.PendingCourseService;
import com.loam.stoody.service.user.CustomUserDetailsService;
import com.loam.stoody.service.user.UserDTS;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
class UserController {
    private final IAuthenticationFacade authenticationFacade;
    private final CustomUserDetailsService customUserDetailsService;
    private final CategoryService categoryService;
    private final LanguageService languageService;
    private final UserDTS userDTS;
    private final PendingCourseService pendingCourseService;
    private final UserRepository userRepository;

    @ModelAttribute("getUserDTS")
    public UserDTS getUserDTS() {
        return userDTS;
    }

    @ModelAttribute("pendingCourseService")
    public PendingCourseService getPendingCourseService() {
        return pendingCourseService;
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
    @GetMapping("/user/dashboard/instructor/courses/create/new")
    public String getNewCourseRequest(){
        User user = customUserDetailsService.getCurrentUser();
        if(user == null)
            return "redirect:"+PRL.error404URL;

        PendingCourse pendingCourse = new PendingCourse();
        pendingCourse = pendingCourseService.savePendingCourse(pendingCourse);

        return "redirect:/user/dashboard/instructor/courses/course/"+ pendingCourse.getId() +"/editor";
    }

    @GetMapping("/user/dashboard/instructor/courses/course/{id}/editor")
    public String getCourseEditorPage(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        PendingCourse pendingCourse = pendingCourseService.getPendingCourseById(id, PendingCourse.class);
        if (pendingCourse != null) {
            if(!customUserDetailsService.compareUsers(pendingCourse.getAuthor(),customUserDetailsService.getCurrentUser()))
                return "redirect:"+PRL.error404URL;

            if(pendingCourse.getCourseStatus() != null){
                if(!pendingCourse.getCourseStatus().equals(CourseStatus.Draft)) {
                    redirectAttributes.addAttribute("header", languageService.getContent("global.you_cannot_edit_pending_course"));
                    redirectAttributes.addAttribute("message", languageService.getContent("global.course_pending_cannot_be_edited"));
                    redirectAttributes.addAttribute("openCode", PRL.openCode);
                    return "redirect:"+PRL.redirectPageURL;
                }
            }

            model.addAttribute("courseDTO", pendingCourseService.mapCourseEntityToRequest(pendingCourse));
            model.addAttribute("subCategoryElements", categoryService.getAllCategories());
            return "pages/add-course";
        }

        return "redirect:"+PRL.error404URL;
    }

    @GetMapping("/user/dashboard/instructor/courses/search")
    public String getInstructorCreatedCoursesPage(@ModelAttribute("searchFilterDTO")SearchFilterDTO searchFilterDTO,Model model){
        // Find only instructor's courses.
        if(searchFilterDTO == null)
            model.addAttribute("searchFilterDTO",new SearchFilterDTO());
        else
            model.addAttribute("searchFilterDTO",searchFilterDTO);

        model.addAttribute("mainPreviewUserInfo", userDTS.getCurrentUser());

        model.addAttribute("currentUserCourses", pendingCourseService.getCurrentUserPendingCourses(PendingCourseDTO.class));
        return "pages/instructor-courses";
    }

    @GetMapping("/user/dashboard/instructor/courses/tag/deleted/{id}")
    public String postInstructorTagCourseDeleted(@PathVariable("id")Long id){
//        PendingCourse pendingCourse = courseService.getCourseEntityById(id);
//        if(pendingCourse != null && pendingCourse.getAuthor() != null && customUserDetailsService.compareUsers(pendingCourse.getAuthor(),customUserDetailsService.getCurrentUser()) ) {
//            pendingCourse.setCourseStatus(CourseStatus.Deleted);
//            courseService.saveEntity(pendingCourse);
//            return "redirect:/user/dashboard/instructor/courses/search";
//        }
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
