package com.loam.stoody.controller;

import com.loam.stoody.dto.LanguageAPIResponse;
import com.loam.stoody.model.LanguageModel;
import com.loam.stoody.model.User;
import com.loam.stoody.repository.LanguageRepository;
import com.loam.stoody.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@RestController
public class LanguageController {
    @Autowired
    private LanguageService languageService;
    @Autowired
    private LanguageRepository languageRepository;

    // Access to Model View

    @GetMapping("/admin/languages")
    public ModelAndView getLanguagesPage(Map<String, Object> _model, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) auth.getPrincipal();
        model.addAttribute("userInfo",currentUser);

        model.addAttribute("newLanguageModel", new LanguageModel());
        model.addAttribute("languageModels", languageService.getAllLanguageModels());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user_panel/languages");
        return modelAndView;
    }

    // EXTERNAL ACCESS API

    @GetMapping("/api/language/search/{field}")
    public LanguageAPIResponse<List<LanguageModel>> getLanguageModelsSorted2(@PathVariable("field") String field) {
        List<LanguageModel> allLanguageModelsSorted = null;
        if(field != null)
            allLanguageModelsSorted= languageService.getAllLanguagesBySearchKey(field);
        else
            allLanguageModelsSorted = languageService.getAllLanguageModels();

        return new LanguageAPIResponse<>(allLanguageModelsSorted.size(), allLanguageModelsSorted);
    }

    @PostMapping(path = "/api/language/post/{locale}/{messagekey}/{messagecontent}")
    @ResponseStatus(HttpStatus.OK)
    public LanguageAPIResponse<List<LanguageModel>> postLanguageModelSeparate(@PathVariable("locale") String locale,
                                                                              @PathVariable("messagekey") String messagekey,
                                                                              @PathVariable("messagecontent") String messagecontent) {
        LanguageModel langModel = new LanguageModel();
        langModel.setLocale(locale);
        langModel.setKey(messagekey);
        langModel.setContent(messagecontent);

        languageService.addLanguageModel(langModel);

        return new LanguageAPIResponse<>(languageService.getAllLanguageModels().size(), languageService.getAllLanguageModels());
    }

    @PostMapping(path = "/api/language/delete/{locale}/{key}/{content}")
    @ResponseStatus(HttpStatus.OK)
    public LanguageAPIResponse<List<LanguageModel>> deleteLanguageModel(@PathVariable("locale") String locale,
                                                                        @PathVariable("key") String messagekey,
                                                                        @PathVariable("content") String messagecontent) {
        for (LanguageModel i : languageService.getAllLanguageModels())
            if (i.getLocale().equals(locale) && i.getKey().equals(messagekey) && i.getContent().equals(messagecontent)) {
                languageService.removeLanguageModel(i);
            }

        return new LanguageAPIResponse<>(languageService.getAllLanguageModels().size(), languageService.getAllLanguageModels());
    }

    @GetMapping("/api/language/get/sorted/{field}")
    public LanguageAPIResponse<List<LanguageModel>> getLanguageModelsSorted(@PathVariable("field") String field) {
        List<LanguageModel> allLanguageModelsSorted = languageService.getLanguageModelsSorted(field);

        return new LanguageAPIResponse<>(allLanguageModelsSorted.size(), allLanguageModelsSorted);
    }

    @GetMapping("/api/language/get/paginated/{offset}/{pageSize}")
    public LanguageAPIResponse<Page<LanguageModel>> getLanguageModelsPaginated(@PathVariable int offset, @PathVariable int pageSize) {
        Page<LanguageModel> allLanguageModelsPaginated = languageService.getLanguageModelsPaginated(offset, pageSize);

        return new LanguageAPIResponse<>(allLanguageModelsPaginated.getSize(), allLanguageModelsPaginated);
    }
}
