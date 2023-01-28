/*
@fileName:  PRL

@aka:  Page Resource Locator

@purpose:   To obtain all Page-Resource related information in one location.
            As adjustments were made, it was aimed to make things simpler.

@author:    OrkhanGG

@createdAt:   20.12.2022
*/

package com.loam.stoody.global.constants;


public interface PRL {
    int openCode = 65565;

    // Misc
    String JWTTokenPrefix = "Bearer";

    // URLs ------------------------------------------------------------------------------------------------------------
    // Controller
    String homeURL = "/";
    String userHomeURL = "/home";
    String signInURL = "/sign-in";
    String signUpURL = "/sign-up";
    String logoutURL = "/logout";
    String forgetPasswordURL = "/forget-password";
    String error404URL = "/error";
    String redirectPageURL = "/redirect-page";
    String verifyPasswordPageURL = "/verify-password";

    // API (Rest Controller)
    // Prefixes
    String apiPrefix = "/" + About.AppNameLC + "/api/" + About.AppVersion;
    String apiRegistrationPrefixURL = apiPrefix + "/registration";
    String apiLoginPrefixURL = apiPrefix + "/user";
    String apiVerifySuffixURL = "/verify";
    // Course
    String apiCourseSuffixURL = "/course";

    // HTML NAMES ------------------------------------------------------------------------------------------------------
    // Public Pages
    String visitorHomePage = "pages/landings/home-academy";
    String signInPage = "pages/sign-in";
    String signUpPage = "pages/sign-up";
    String forgetPasswordPage = "pages/forget-password";
    String error404Page = "pages/404-error";
    String contactPage = "pages/contact";
    String redirectPage = "pages/redirect-page";
    String verifyPasswordPage = "pages/verify-password";

        // Only for any authorized one
    String userHomePage = "index";
}
