/*
@fileName:  PRL

@aka:  Page Resource Locator

@purpose:   To obtain all Page-Resource related information in one location.
            As adjustments were made, it was aimed to make things simpler.

@author:    OrkhanGG

@createdAt:   20.12.2022
*/

package com.loam.stoody.global.constants;


public class PRL {
    public static final int openCode = 65565;

    // URLs ------------------------------------------------------------------------------------------------------------
    // Controller
    public static final String homeURL = "/";
    public static final String signInURL = "/sign-in";
    public static final String signUpURL = "/sign-up";
    public static final String logoutURL = "/logout";
    public static final String forgetPasswordURL = "/forget-password";
    public static final String error404URL = "/error";
    public static final String redirectPageURL = "/redirect-page";
    public static final String verifyPasswordPageURL = "/verify-password";

    // API (Rest Controller)
    // Prefixes
    public static final String apiPrefix = "/" + About.AppNameLC + "/api/" + About.AppVersion;
    public static final String apiRegistrationPrefixURL = apiPrefix + "/registration";
    public static final String apiLoginPrefixURL = apiPrefix + "/user";
    public static final String apiVerifySuffixURL = "/verify";

    // HTML NAMES ------------------------------------------------------------------------------------------------------
    // Public Pages
    public static final String visitorHomePage = "pages/landings/home-academy";
    public static final String signInPage = "pages/sign-in";
    public static final String signUpPage = "pages/sign-up";
    public static final String forgetPasswordPage = "pages/forget-password";
    public static final String error404Page = "pages/404-error";
    public static final String contactPage = "pages/contact";
    public static final String redirectPage = "pages/redirect-page";
    public static final String verifyPasswordPage = "pages/verify-password";

        // Only for any authorized one
    public static final String userHomePage = "index";
}
