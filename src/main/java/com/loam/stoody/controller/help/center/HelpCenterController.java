package com.loam.stoody.controller.help.center;

import com.loam.stoody.dto.api.response.HelpCenterArticleResponseDTO;
import com.loam.stoody.global.annotations.UnderDevelopment;
import com.loam.stoody.global.constants.PRL;
import com.loam.stoody.model.help.center.HelpCenterArticle;
import com.loam.stoody.model.help.center.HelpCenterCategory;
import com.loam.stoody.model.help.center.HelpCenterSupportTicket;
import com.loam.stoody.service.help.center.HelpCenterService;
import com.loam.stoody.service.user.UserDTS;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class HelpCenterController {
    private HelpCenterService helpCenterService;
    private final UserDTS userDTS;

    @ModelAttribute("getUserDTS")
    public UserDTS getUserDTS() {
        return userDTS;
    }

    @ModelAttribute("getHelpCenterServiceLayer")
    public HelpCenterService getHelpCenterServiceLayer(){
        return helpCenterService;
    }
    //--------------------------------------------------------------------------------

    @GetMapping("/stoody/authorized/help/center/categories")
    public String getHelpCenterManageContent(Model model){
        model.addAttribute("helpCenterCategory", new HelpCenterCategory());
        model.addAttribute("helpCenterCategories", helpCenterService.getAllHelpCenterCategory());
        return "pages/dashboard/help-center-manage-category";
    }

    @PostMapping("/stoody/authorized/help/center/category/post")
    public String postHelpCenterCategory(@ModelAttribute("helpCenterCategory")HelpCenterCategory helpCenterCategory){
        helpCenterService.saveHelpCenterCategory(helpCenterCategory);
        return "redirect:/stoody/authorized/help/center/categories";
    }

    @PostMapping("/stoody/authorized/help/center/category/delete/{id}")
    public String postHelpCenterCategoryDelete(@PathVariable("id")Integer id){
        helpCenterService.deleteHelpCenterCategoryById(id);
        return "redirect:/stoody/authorized/help/center/categories";
    }

    //-------

    @GetMapping("/stoody/authorized/help/center/content/manager")
    public String getHelpCenterContentPage(Model model){
        model.addAttribute("helpCenterContentObject", new HelpCenterArticleResponseDTO());
        model.addAttribute("helpCenterCategories", helpCenterService.getAllHelpCenterCategory());
        model.addAttribute("helpCenterContents", helpCenterService.getAllHelpCenterArticle());
        return "pages/dashboard/help-center-manage-content";
    }

    @PostMapping("/stoody/authorized/help/center/content/post")
    public String postHelpCenterContentDelete(@ModelAttribute("helpCenterContentObject") HelpCenterArticleResponseDTO helpCenterArticleResponseDTO){
        HelpCenterArticle helpCenterArticle = new HelpCenterArticle();
        helpCenterArticle.setMetaAuthor(helpCenterArticleResponseDTO.getMetaAuthor());
        helpCenterArticle.setMetaKeywords(helpCenterArticleResponseDTO.getMetaKeywords());
        helpCenterArticle.setMetaDescription(helpCenterArticleResponseDTO.getMetaDescription());
        helpCenterArticle.setName(helpCenterArticleResponseDTO.getName());
        helpCenterArticle.setContent(helpCenterArticleResponseDTO.getContent());
        helpCenterArticle.setShortContent(helpCenterArticleResponseDTO.getShortContent());
        helpCenterArticle.setUrlKey(helpCenterArticleResponseDTO.getUrlKey());
        helpCenterArticle.setCategory(helpCenterService.findHelpCenterCategoryById(helpCenterArticleResponseDTO.getId()).orElse(null));

        helpCenterService.saveHelpCenterArticle(helpCenterArticle);
        return "redirect:/stoody/authorized/help/center/content/manager";
    }

    @PostMapping("/stoody/authorized/help/center/content/delete/{id}")
    public String postHelpCenterContentDelete(@PathVariable("id")Long id){
        helpCenterService.deleteHelpCenterArticleById(id);
        return "redirect:/stoody/authorized/help/center/content/manager";
    }
    //---------------------------------------------------------------------------------

    @GetMapping("/help")
    public String getHelpPage(){
        return "pages/help-center";
    }

    @GetMapping("/help/search/{key}")
    public String getHelpPageSearchResults(@PathVariable("key")String key, Model model){
        model.addAttribute("searchKey", key);
        return "pages/help-center-search-results";
    }

    @GetMapping("/help/search")
    public String getHelpPageSearchResults(@RequestParam("key") String key){
        return "redirect:/help/search/"+key;
    }

    @GetMapping("/help/guides")
    public String getHelpCenterGuidesPage(){
        return "pages/help-center-guide";
    }

    @GetMapping("/help/faq")
    public String getHelpCenterFaqPage(){
        return "pages/help-center-faq";
    }

    @GetMapping("/help/support")
    public String getHelpCenterSupportPage(Model model){
        model.addAttribute("supportTicket", new HelpCenterSupportTicket());
        return "pages/help-center-support";
    }

    @PostMapping("/help/support")
    public String postHelpCenterSupportPage(@ModelAttribute("supportTicket")HelpCenterSupportTicket helpCenterSupportTicket,
                                            RedirectAttributes redirectAttributes){
        helpCenterService.saveSupportTicket(helpCenterSupportTicket, true/*Send email to Stoody*/);

        redirectAttributes.addAttribute("header", "Success");
        redirectAttributes.addAttribute("message", "Your ticket has been submitted! We'll notify you as soon as possible!");
        redirectAttributes.addAttribute("openCode", PRL.openCode);

        return "redirect:"+PRL.redirectPageURL;
    }

    @GetMapping("/help/{category}/{article}")
    public String getHelpPageArticlePage(@PathVariable("category")String category,
                                         @PathVariable("article")String articleURL,
                                         Model model){
        List<HelpCenterArticle> articleElement = helpCenterService.getAllHelpCenterArticle().stream()
                .filter(e -> e.getCategory().getName().equals(category) && e.getUrlKey().equals(articleURL)).toList();

        if(articleElement != null){
            model.addAttribute("articleElement",articleElement.get(0));
        }else{
            return "redirect:"+ PRL.error404URL;
        }

        return "pages/help-center-guide-single";
    }

    // Like is not yet properly configured. User can like multiple times
    @UnderDevelopment
    @GetMapping("/help/like/{id}")
    public String getArticleLikedById(@PathVariable("id")Long id){
        HelpCenterArticle helpCenterArticle = helpCenterService.likeHelpCenterArticle(id);
        if(helpCenterArticle != null){
            return "redirect:/help/"+helpCenterArticle.getCategory().getName()+"/"+helpCenterArticle.getUrlKey();
        }else{
            return "redirect:"+PRL.error404URL;
        }
    }

    @UnderDevelopment
    @GetMapping("/help/dislike/{id}")
    public String getArticleDislikedById(@PathVariable("id")Long id){
        HelpCenterArticle helpCenterArticle = helpCenterService.dislikeHelpCenterArticle(id);
        if(helpCenterArticle != null){
            return "redirect:/help/"+helpCenterArticle.getCategory().getName()+"/"+helpCenterArticle.getUrlKey();
        }else{
            return "redirect:"+PRL.error404URL;
        }
    }
}
