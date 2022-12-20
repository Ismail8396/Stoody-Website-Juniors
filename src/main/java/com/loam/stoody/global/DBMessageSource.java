/*
@fileName:  DBMessageSource

@aka:       Database Message Source

@purpose:   Resolves the language code to get the appropriate text content according to current locale.

@author:    OrkhanGG

@created:   05.12.2022
*/

package com.loam.stoody.global;

import com.loam.stoody.model.i18n_models.LanguageModel;
import com.loam.stoody.repository.i18n_repo.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Locale;

@Component("messageSource")
public class DBMessageSource extends AbstractMessageSource {
    private final LanguageRepository languageRepository;

    @Autowired
    public DBMessageSource(LanguageRepository languageRepository){
        this.languageRepository = languageRepository;
    }

    private static final String DEFAULT_LOCALE_CODE = "en";
    @Override
    protected MessageFormat resolveCode(String key, Locale locale) {
        LanguageModel message = languageRepository.findByKeyAndLocale(key,locale.getLanguage());

        // One more check not to get any null in any case
        if (message == null) {
            LanguageModel original = new LanguageModel();
            original.setKey(key);
            original.setLocale(locale.getLanguage());
            original.setContent(key);

            message = languageRepository.findByKeyAndLocale(key,DEFAULT_LOCALE_CODE) != null
            ? languageRepository.findByKeyAndLocale(key,DEFAULT_LOCALE_CODE)
            : original;
        }

        return new MessageFormat(message.getContent(), locale);
    }
}