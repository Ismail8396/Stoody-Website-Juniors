package com.loam.stoody.service.help.center;

import com.loam.stoody.global.annotations.UnderDevelopment;
import com.loam.stoody.global.constants.About;
import com.loam.stoody.model.help.center.HelpCenterArticle;
import com.loam.stoody.model.help.center.HelpCenterCategory;
import com.loam.stoody.model.help.center.HelpCenterSupportTicket;
import com.loam.stoody.repository.help.center.HelpCenterArticleRepository;
import com.loam.stoody.repository.help.center.HelpCenterCategoryRepository;
import com.loam.stoody.repository.help.center.HelpCenterSupportTicketRepository;
import com.loam.stoody.service.communication.email.EmailSenderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HelpCenterService {
    private HelpCenterArticleRepository helpCenterArticleRepository;
    private HelpCenterCategoryRepository helpCenterCategoryRepository;
    private HelpCenterSupportTicketRepository helpCenterSupportTicketRepository;
    private EmailSenderService emailSenderService;

    // Category
    public List<HelpCenterCategory> getAllHelpCenterCategory(){
        return helpCenterCategoryRepository.findAll();
    }

    public List<HelpCenterCategory> getNHelpCenterCategory(int maxSize){
        return helpCenterCategoryRepository.findAll().stream().limit(maxSize).collect(Collectors.toList());
    }

    public void saveHelpCenterCategory(HelpCenterCategory helpCenterCategory){
        helpCenterCategoryRepository.save(helpCenterCategory);
    }

    public Optional<HelpCenterCategory> findHelpCenterCategoryById(Integer id){
        return helpCenterCategoryRepository.findById(id);
    }

    public void deleteHelpCenterCategory(HelpCenterCategory helpCenterCategory){
        helpCenterCategoryRepository.delete(helpCenterCategory);
    }

    public void deleteHelpCenterCategoryById(Integer id){
        helpCenterCategoryRepository.deleteById(id);
    }

    // Article

    public List<HelpCenterArticle> getAllHelpCenterArticle(){
        return helpCenterArticleRepository.findAll();
    }

    public List<HelpCenterArticle> getAllHelpCenterArticleByCategoryId(Integer categoryId){
        return helpCenterArticleRepository.findAll().stream().filter(e->e.getCategory().getId()==categoryId).collect(Collectors.toList());
    }

    public List<HelpCenterArticle> getAllHelpCenterArticleByCategoryIdLimited(Integer categoryId, int maxSize){
        return helpCenterArticleRepository.findAll().stream().filter(e->e.getCategory().getId()==categoryId).limit(maxSize).collect(Collectors.toList());
    }

    public List<HelpCenterArticle> getAllHelpCenterArticleByCategoryIdWithSearchFilter(Integer categoryId, String searchFilter){
        return helpCenterArticleRepository.findAll().stream().filter(
                e->(e.getCategory().getId()==categoryId || e.getCategory().getName().toLowerCase().contains(searchFilter.toLowerCase()))
                && (e.getName().toLowerCase().contains(searchFilter.toLowerCase()) || e.getShortContent().toLowerCase().contains(searchFilter.toLowerCase())))
                .collect(Collectors.toList());
    }

    public List<HelpCenterArticle> getAllHelpCenterArticleWithSearchFilter(String searchFilter){
        return helpCenterArticleRepository.findAll().stream().filter(
                        e->e.getCategory().getName().toLowerCase().contains(searchFilter.toLowerCase())|| e.getName().toLowerCase().contains(searchFilter.toLowerCase()) || e.getShortContent().toLowerCase().contains(searchFilter.toLowerCase()))
                .collect(Collectors.toList());
    }


    public List<HelpCenterArticle> getNHelpCenterArticle(int maxSize){
        return helpCenterArticleRepository.findAll().stream().limit(maxSize).collect(Collectors.toList());
    }

    public HelpCenterArticle likeHelpCenterArticle(Long id){
        Optional<HelpCenterArticle> helpCenterArticle = helpCenterArticleRepository.findById(id);
        if(helpCenterArticle.isPresent()) {
            helpCenterArticle.get().setLikes(helpCenterArticle.get().getLikes() + 1);
            helpCenterArticleRepository.save(helpCenterArticle.get());
            return helpCenterArticle.get();
        }
        return null;
    }

    public HelpCenterArticle dislikeHelpCenterArticle(Long id){
            Optional<HelpCenterArticle> helpCenterArticle = helpCenterArticleRepository.findById(id);
            if(helpCenterArticle.isPresent()) {
                helpCenterArticle.get().setDislikes(helpCenterArticle.get().getDislikes() + 1);
                helpCenterArticleRepository.save(helpCenterArticle.get());
                return helpCenterArticle.get();
            }
            return null;
    }

    @UnderDevelopment// TODO: Add relation filter
    public List<HelpCenterArticle> getRelatedHelpCenterArticle(String articleUrl, int maxSize){
        return helpCenterArticleRepository.findAll().stream().limit(maxSize).collect(Collectors.toList());
    }

    public void saveHelpCenterArticle(HelpCenterArticle helpCenterArticle){
        helpCenterArticle.setModifiedDate(LocalDateTime.now());
        helpCenterArticleRepository.save(helpCenterArticle);
    }

    public void deleteHelpCenterArticle(HelpCenterArticle helpCenterArticle){
        helpCenterArticleRepository.save(helpCenterArticle);
    }

    public void deleteHelpCenterArticleById(Long id){
        helpCenterArticleRepository.deleteById(id);
    }

    public Optional<HelpCenterArticle> findHelpCenterArticleById(Long id){
        return helpCenterArticleRepository.findById(id);
    }

    // Support Ticket
    public void saveSupportTicket(HelpCenterSupportTicket helpCenterSupportTicket, Boolean sendEmailToStoody){
        if(sendEmailToStoody) {
            emailSenderService.sendEmail(About.ContactEmail,helpCenterSupportTicket.getSubject(),
                    helpCenterSupportTicket.getFullName()+ " "+ helpCenterSupportTicket.getEmail()+" \n"
                    + helpCenterSupportTicket.getDescription());
        }
        helpCenterSupportTicketRepository.save(helpCenterSupportTicket);
    }
}
