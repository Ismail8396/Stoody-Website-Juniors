package com.loam.stoody.controller.communication;

import com.loam.stoody.dto.api.response.OutdoorResponse;
import com.loam.stoody.enums.IndoorResponse;
import com.loam.stoody.global.constants.PRL;
import com.loam.stoody.global.logger.ConsoleColors;
import com.loam.stoody.global.logger.StoodyLogger;
import com.loam.stoody.model.communication.report.ReportedUser;
import com.loam.stoody.model.i18n.LanguageModel;
import com.loam.stoody.model.user.User;
import com.loam.stoody.service.communication.report.ReportService;
import com.loam.stoody.service.i18n.LanguageService;
import com.loam.stoody.service.user.CustomUserDetailsService;
import com.loam.stoody.service.user.UserDTS;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
public class ReportController {
    private final CustomUserDetailsService customUserDetailsService;
    private final ReportService reportService;
    private final LanguageService languageService;
    private final UserDTS userDTS;

    @ModelAttribute("getUserDTS")
    public UserDTS getUserDTS() {
        return userDTS;
    }
    //------------------------------------------------------------------------------------------------------------------

    @GetMapping("/stoody/authorized/tables/reported/users")
    public String getReportedUsersPage(Model model) {
        try {
            customUserDetailsService.getUserByUsername(customUserDetailsService.getCurrentUser().getUsername());
        } catch (RuntimeException ignore) {
            StoodyLogger.DebugLog(ConsoleColors.RED, "User was either null or not authorized!");
            return "redirect:" + PRL.error404URL;
        }

        model.addAttribute("reportedUsersList", reportService.getAllReportedUser());
        model.addAttribute("languageModel", new LanguageModel());// Remove this later!

        return "pages/dashboard/user-report-table";
    }

    @GetMapping("/stoody/authorized/tables/reported/users/remove/{id}")
    public String getReportedUsersPage(@PathVariable("id") long id) {
        try {
            customUserDetailsService.getUserByUsername(customUserDetailsService.getCurrentUser().getUsername());
        } catch (RuntimeException ignore) {
            StoodyLogger.DebugLog(ConsoleColors.RED, "User was either null or not authorized!");
            return "redirect:" + PRL.error404URL;
        }

        reportService.removeReportedUserById(id);

        return "redirect:/stoody/authorized/tables/reported/users";
    }

    @PostMapping("/users/preview/report")
    @ResponseBody
    public OutdoorResponse<?> postReportUser(@RequestParam("reportedBy") String reportedByUsername,
                                             @RequestParam("reportedUser") String reportedUserUsername,
                                             @RequestParam("reportReason") String reportReason) {

        try {
            User reportedBy = customUserDetailsService.getUserByUsername(reportedByUsername);
            User reported = customUserDetailsService.getUserByUsername(reportedUserUsername);

            if (!customUserDetailsService.compareUsers(reportedBy, customUserDetailsService.getCurrentUser()))
                return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST, "global.you_are_not_authorized");

            ReportedUser reportedUser = new ReportedUser();
            reportedUser.setReportedBy(reportedBy);
            reportedUser.setReportedUser(reported);
            reportedUser.setReason(reportReason);

            if (reportService.doesReportedUserExist(reportedUser))
                return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST, languageService.getContent("global.user_already_reported_message"));

            reportService.saveReportedUser(reportedUser);
        } catch (UsernameNotFoundException usernameNotFoundException) {
            return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST, languageService.getContent("global.failed_user_report_message"));
        }
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, languageService.getContent("global.success_user_report_message"));
    }
}
