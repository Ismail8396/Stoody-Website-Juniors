package com.loam.stoody.components;

import com.blueconic.browscap.BrowsCapField;
import com.blueconic.browscap.Capabilities;
import com.blueconic.browscap.UserAgentParser;
import com.blueconic.browscap.UserAgentService;
import com.loam.stoody.service.i18n.LanguageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.Arrays;
import java.util.Locale;

@Component
@AllArgsConstructor
public class UrlLocaleInterceptor implements HandlerInterceptor {
    private final LanguageService languageService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {

//            {// or create a parser with a custom defined field list
//// the list of available fields can be seen inthe BrowsCapField enum
//                final UserAgentParser parser = new UserAgentService().loadParser(Arrays.asList(BrowsCapField.BROWSER, BrowsCapField.BROWSER_TYPE, BrowsCapField.BROWSER_MAJOR_VERSION, BrowsCapField.DEVICE_TYPE, BrowsCapField.PLATFORM, BrowsCapField.PLATFORM_VERSION, BrowsCapField.RENDERING_ENGINE_VERSION, BrowsCapField.RENDERING_ENGINE_NAME, BrowsCapField.PLATFORM_MAKER, BrowsCapField.RENDERING_ENGINE_MAKER));
//
//// It's also possible to supply your own ZIP file by supplying a correct path to a ZIP file in the constructor.
//// This can be used when a new BrowsCap version is released which is not yet bundled in this package.
//// final UserAgentParser parser = new UserAgentService("E:\\anil\\browscap.zip").loadParser();
//
//// parser can be re-used for multiple lookup calls
//                final String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36";
//                final Capabilities capabilities = parser.parse(userAgent);
//
//// the default fields have getters
//                final String browser = capabilities.getBrowser();
//                final String browserType = capabilities.getBrowserType();
//                final String browserMajorVersion = capabilities.getBrowserMajorVersion();
//                final String deviceType = capabilities.getDeviceType();
//                final String platform = capabilities.getPlatform();
//                final String platformVersion = capabilities.getPlatformVersion();
//
//// the custom defined fields are available
//                final String renderingEngineMaker = capabilities.getValue(BrowsCapField.RENDERING_ENGINE_MAKER);
//
//                System.out.println(browser);
//                System.out.println(browserType);
//                System.out.println(browserMajorVersion);
//                System.out.println(deviceType);
//                System.out.println(platform);
//                System.out.println(platformVersion);
//                //}

            final String localeLanguage = languageService.getCurrentLocaleLanguage(request, response);
            LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);

            if (localeResolver == null) throw new RuntimeException();

            if (!RequestContextUtils.getLocale(request).getLanguage().equals(localeLanguage)) {
                localeResolver.setLocale(request, response, parseLocaleValue(localeLanguage));
            }
        } catch (Exception ignore) {
            // skipped
            System.out.println("UrlLocaleInterceptor threw an exception!");
        }

        return true;
    }

    protected Locale parseLocaleValue(String localeValue) {
        return StringUtils.parseLocale(localeValue);
    }
}