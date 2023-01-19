package com.loam.stoody.service.communication.contact;

import com.loam.stoody.global.constants.About;
import com.loam.stoody.model.communication.contact.Contact;
import com.loam.stoody.repository.communication.contact.ContactRepository;
import com.loam.stoody.service.communication.email.EmailSenderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ContactService {
    private final EmailSenderService emailSenderService;
    private final ContactRepository contactRepository;

    public void saveContact(Contact contact, Boolean sendEmailToStoody) {
        if (sendEmailToStoody) {
            emailSenderService.sendEmail(About.ContactEmail, contact.getMessage(),
                    contact.getFirstName() + " " + contact.getLastName() + " "
                            + contact.getEmail() + " " + contact.getPhoneNumber()
                            + " \n" + " " + contact.getContactReason() + " " + contact.getMessage());
        }

        contactRepository.save(contact);
    }
}

