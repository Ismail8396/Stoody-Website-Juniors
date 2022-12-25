package com.loam.stoody.controller.rest.i18n;

import com.loam.stoody.dto.api.response.LanguageAPIResponse;
import com.loam.stoody.model.i18n.LanguageModel;
import com.loam.stoody.model.user.User;
import com.loam.stoody.service.i18n.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
public class LanguageController {
    private final LanguageService languageService;

    @Autowired
    public LanguageController(LanguageService languageService){
        this.languageService = languageService;
    }

    // Don't remove this method for now.
    @Deprecated(/* forRemoval = true */)
    @GetMapping("/admin/languages")
    public ModelAndView getLanguagesPage(Map<String, Object> _model, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) auth.getPrincipal();
        model.addAttribute("userInfo", currentUser);

        model.addAttribute("newLanguageModel", new LanguageModel());
        model.addAttribute("languageModels", languageService.getAllLanguageModels());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user_panel/languages");
        return modelAndView;
    }

    @GetMapping("/api/language/search/{field}")
    @ResponseBody
    public LanguageAPIResponse<List<LanguageModel>> getLanguageModelsSorted2(@PathVariable("field") String field) {
        List<LanguageModel> allLanguageModelsSorted = null;
        if (field != null) allLanguageModelsSorted = languageService.getAllLanguagesBySearchKey(field);
        else allLanguageModelsSorted = languageService.getAllLanguageModels();

        return new LanguageAPIResponse<>(allLanguageModelsSorted.size(), allLanguageModelsSorted);
    }

    @PostMapping(path = "/api/language/post/{locale}/{messagekey}/{messagecontent}")
    @ResponseBody
    public LanguageAPIResponse<List<LanguageModel>> postLanguageModelSeparate(@PathVariable("locale") String locale, @PathVariable("messagekey") String messagekey, @PathVariable("messagecontent") String messagecontent) {
        LanguageModel langModel = new LanguageModel();
        langModel.setLocale(locale);
        langModel.setKey(messagekey);
        langModel.setContent(messagecontent);

        languageService.addLanguageModel(langModel);

        return new LanguageAPIResponse<>(languageService.getAllLanguageModels().size(), languageService.getAllLanguageModels());
    }

    @PostMapping(path = "/api/language/delete/{locale}/{key}/{content}")
    @ResponseBody
    public LanguageAPIResponse<List<LanguageModel>> deleteLanguageModel(@PathVariable("locale") String locale, @PathVariable("key") String messagekey, @PathVariable("content") String messagecontent) {
        for (LanguageModel i : languageService.getAllLanguageModels())
            if (i.getLocale().equals(locale) && i.getKey().equals(messagekey) && i.getContent().equals(messagecontent)) {
                languageService.removeLanguageModel(i);
            }

        return new LanguageAPIResponse<>(languageService.getAllLanguageModels().size(), languageService.getAllLanguageModels());
    }

    @Deprecated
    @GetMapping("/api/language/get/sorted/{field}")
    @ResponseBody
    public LanguageAPIResponse<List<LanguageModel>> getLanguageModelsSorted(@PathVariable("field") String field) {
        List<LanguageModel> allLanguageModelsSorted = languageService.getLanguageModelsSorted(field);

        return new LanguageAPIResponse<>(allLanguageModelsSorted.size(), allLanguageModelsSorted);
    }

    @Deprecated
    @GetMapping("/api/language/get/paginated/{offset}/{pageSize}")
    @ResponseBody
    public LanguageAPIResponse<Page<LanguageModel>> getLanguageModelsPaginated(@PathVariable int offset, @PathVariable int pageSize) {
        Page<LanguageModel> allLanguageModelsPaginated = languageService.getLanguageModelsPaginated(offset, pageSize);

        return new LanguageAPIResponse<>(allLanguageModelsPaginated.getSize(), allLanguageModelsPaginated);
    }
}
