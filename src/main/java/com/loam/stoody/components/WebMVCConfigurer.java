package com.loam.stoody.components;

import com.loam.stoody.components.UrlLocaleInterceptor;
import com.loam.stoody.global.annotations.UnderDevelopment;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.util.WebUtils;

// The LocaleResolver helps to identify which locale is being used.
// Temporarily, we will use CookieLocaleResolver.

@Configuration
@AllArgsConstructor
public class WebMVCConfigurer implements WebMvcConfigurer {
    private final UrlLocaleInterceptor urlInterceptor;

    @Bean
    public LocaleResolver localeResolver() {
        return new CookieLocaleResolver();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(urlInterceptor);
        //--
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        registry.addInterceptor(localeChangeInterceptor);
    }

    // TODO: Configure CORS!
    @UnderDevelopment
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

}