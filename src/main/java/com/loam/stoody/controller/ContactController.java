package com.loam.stoody.controller;

import com.loam.stoody.global.constants.PRL;
import com.loam.stoody.model.communication.contact.Contact;
import com.loam.stoody.service.communication.contact.ContactService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
public class ContactController {
    private ContactService contactService;

    @GetMapping("/contact")
    public String getContactPage(Model model){
        model.addAttribute("contactEntity",new Contact());
        return "pages/contact";
    }

    @PostMapping("/contact")
    public String postContactPage(@ModelAttribute("contactEntity")Contact contact, RedirectAttributes redirectAttributes){
        contactService.saveContact(contact,true);

        redirectAttributes.addAttribute("header", "Success");
        redirectAttributes.addAttribute("message", "Your ticket has been submitted! We'll notify you as soon as possible!");
        redirectAttributes.addAttribute("openCode", PRL.openCode);

        return "redirect:"+PRL.redirectPageURL;
    }
}
