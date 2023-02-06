package com.loam.stoody.service.i18n;

import com.loam.stoody.global.constants.MiscConstants;
import com.loam.stoody.model.i18n.LanguageModel;
import com.loam.stoody.repository.i18n.LanguageRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.util.WebUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class LanguageService {
    private final LanguageRepository languageRepository;

    @Autowired
    public LanguageService(LanguageRepository languageRepository){
        this.languageRepository = languageRepository;
    }

    public void addLanguageModel(LanguageModel languageModel){
        languageRepository.save(languageModel);
    }

    public void removeLanguageModel(LanguageModel languageModel){
        languageRepository.delete(languageModel);
    }

    public void removeLanguageModelById(Integer id){
        languageRepository.deleteById(id);
    }

    public List<LanguageModel> getAllLanguageModels(){
        return languageRepository.findAll();
    }

    public List<String> getAllLocaleLanguages(){
        List<String> localeLanguages =
                languageRepository.findAll().stream().map(LanguageModel::getLocale).distinct().toList();
        if(localeLanguages.size() == 0) {
            return new ArrayList<>(List.of(MiscConstants.defaultLocale));
        }
        return localeLanguages;
    }

    public String getCurrentLocaleLanguage(HttpServletRequest request){
        // Try to get cookie
        final Cookie localeCookie = WebUtils.getCookie(request,"Locale");
        String response;
        try{
            if(localeCookie == null)throw new RuntimeException();
            // Get cookie value as String
            final String localeCookieValue = localeCookie.getValue();
            if(localeCookieValue == null || localeCookieValue.isBlank())
                throw new RuntimeException();
            // Get all existing locales
            List<String> localeLanguages = getAllLocaleLanguages();
            if(localeLanguages == null)throw new RuntimeException();
            if(localeLanguages.size() == 0) throw new RuntimeException();
            // Set locale if exists
            if(localeLanguages.stream().anyMatch(n -> n.equals(localeCookieValue))){
                response = localeCookieValue;
            }else throw new RuntimeException();
        }catch (Exception ignore){
            response = MiscConstants.defaultLocale;
        }
        return response;
    }

    // Returns current locale depending on the 'Locale' cookie
    public String getCurrentLocaleLanguage(HttpServletRequest request,
                                   HttpServletResponse sResponse){
        // Try to get cookie
        Cookie localeCookie = WebUtils.getCookie(request,"Locale");
        String response;
        try{
            if(localeCookie == null){
                localeCookie = new Cookie("Locale",MiscConstants.defaultLocale);
                sResponse.addCookie(localeCookie);
            }
            // Get cookie value as String
            final String localeCookieValue = localeCookie.getValue();
            if(localeCookieValue == null || localeCookieValue.isBlank())
                throw new RuntimeException();
            // Get all existing locales
            List<String> languageModels = getAllLocaleLanguages();
            if(languageModels == null)throw new RuntimeException();
            if(languageModels.size() == 0) throw new RuntimeException();
            // Set locale if exists
            if(languageModels.stream().anyMatch(n -> n.equals(localeCookieValue))){
                response = localeCookieValue;
            }else throw new RuntimeException();
        }catch (Exception ignore){
            response = MiscConstants.defaultLocale;
        }
        return response;
    }

    // Get content with key and locale
    public String getContent(String key, String locale){
        if(key == null)return "global.key.empty";
        if(locale == null)locale = MiscConstants.defaultLocale;
        final LanguageModel queryResult = languageRepository.findByKeyAndLocale(key, locale);
        if(queryResult == null) throw new RuntimeException();
        final String content = queryResult.getContent();
        return content == null ? key : content;
    }

    // Get content with key and locale that is taken from HttpServletRequest
    public String getContent(String key, HttpServletRequest request){
        if(key == null || request == null)return "global.key.empty";
        String content = null;
        try{
            LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
            if(localeResolver == null) throw new RuntimeException();
            Locale locale = localeResolver.resolveLocale(request);
            if(locale == null) throw new RuntimeException();// remove this statement
            final String localeLanguage = locale.getLanguage();
            if(localeLanguage == null)throw new RuntimeException();
            final LanguageModel searchResult = languageRepository.findByKeyAndLocale(key,localeLanguage);
            if(searchResult == null) throw new RuntimeException();
            content = searchResult.getContent();
        }catch(Exception ignore){
            // skip
        }
        return content == null ? key : content;
    }

    // Get content of the key depending on current locale
    public String getContent(String key){
        if(key == null)return "global.key.empty";
        String content = null;
        try{
            Locale locale = LocaleContextHolder.getLocale();
            if(locale == null) throw new RuntimeException();// remove this statement
            final String localeLanguage = locale.getLanguage();
            if(localeLanguage == null)throw new RuntimeException();
            final LanguageModel searchResult = languageRepository.findByKeyAndLocale(key,localeLanguage);
            if(searchResult == null) throw new RuntimeException();
            content = searchResult.getContent();
        }catch(Exception ignore){
            // skip
        }
        return content == null ? key : content;
    }

    @Deprecated
    public List<LanguageModel> getAllLanguagesBySearchKey(String filter){
        return languageRepository.findBySearchKey(filter);
    }

    @Deprecated
    public List<LanguageModel> getLanguageModelsSorted(String field){
        return languageRepository.findAll(Sort.by(Sort.Direction.ASC, field));
    }

    @Deprecated
    public Page<LanguageModel> getLanguageModelsPaginated(int offset, int pageSize){
        return languageRepository.findAll(PageRequest.of(offset,pageSize));
    }
}
