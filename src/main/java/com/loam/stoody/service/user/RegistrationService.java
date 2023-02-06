package com.loam.stoody.service.user;

import com.loam.stoody.dto.api.request.RegistrationRequestDTO;
import com.loam.stoody.global.annotations.UnderDevelopment;
import com.loam.stoody.global.constants.MiscConstants;
import com.loam.stoody.global.constants.PRL;
import com.loam.stoody.model.user.User;
import com.loam.stoody.model.user.requests.RegistrationRequest;
import com.loam.stoody.enums.IndoorResponse;
import com.loam.stoody.repository.user.PendingRegistrationRequests;
import com.loam.stoody.repository.user.RoleRepository;
import com.loam.stoody.repository.user.UserRepository;
import com.loam.stoody.service.communication.email.EmailSenderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@UnderDevelopment
@Service
@AllArgsConstructor
public class RegistrationService {
    private final CustomUserDetailsService customUserDetailsService;
    private final RoleRepository roleRepository;
    private final EmailSenderService emailSenderService;
    private final PendingRegistrationRequests pendingRegistrationRequests;
    private final UserRepository userRepository;


    private IndoorResponse isUserPending(String username, String email){
        IndoorResponse response = IndoorResponse.SUCCESS;

        for(var i : pendingRegistrationRequests.findAll()){
            if (i.getUsername().equals(username)) {
                response = IndoorResponse.USERNAME_EXIST;
                break;
            }
        }

        for(var i : pendingRegistrationRequests.findAll()){
            if (i.getEmail().equals(email)) {
                response = response == IndoorResponse.SUCCESS ? IndoorResponse.EMAIL_EXIST : IndoorResponse.USERNAME_EMAIL_EXIST;
                break;
            }
        }

        return response;
    }

    public IndoorResponse doesUserExist(String username, String email){
        IndoorResponse response = isUserPending(username, email);
        if(response != IndoorResponse.SUCCESS)
            return response;

        return customUserDetailsService.userExist(username,email);
    }

    public IndoorResponse sendTokenToEmail(RegistrationRequestDTO registrationRequest, HttpServletRequest httpServletRequest){
        IndoorResponse response = doesUserExist(registrationRequest.getUsername(),registrationRequest.getEmail());

        if(response == IndoorResponse.SUCCESS) {
            // Clean Expired Requests
            cleanExpiredRegisterRequests();

            RegistrationRequest newRequest = new RegistrationRequest();

            // Set user info
            newRequest.setUsername(registrationRequest.getUsername());
            newRequest.setEmail(registrationRequest.getEmail());
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(11, new SecureRandom());
            newRequest.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            // Create Unique Key
            newRequest.setKey(createUniqueRegisterKeyUrlSuffix());
            // Set createdAt time
            newRequest.setCreatedAt(LocalDateTime.now());
            // Set expiration date
            final LocalDateTime expirationDate = LocalDateTime.now().plusMinutes(MiscConstants.verificationTimeoutMinute);
            newRequest.setExpiresAt(expirationDate);
            // Save registration request
            pendingRegistrationRequests.save(newRequest);
            // Date time formatter
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            // Send the email
            emailSenderService.sendEmail(registrationRequest.getEmail(),
                    "Stoody Account Verification",
                    "Hi! Someone with the username "+registrationRequest.getUsername()+
                            " wanted to register on Stoody.org. If it was you, please click the link below to activate your account:\n\n"
                            + "http://"+httpServletRequest.getHeader("host")+ PRL.signUpURL+PRL.apiVerifySuffixURL+"/"+newRequest.getKey()
                            +
                            "\n\nIf this request was not fulfilled by you, you can skip this e-mail. " +
                            "\n\nLink expires at:\t"+newRequest.getExpiresAt().format(formatter));
        }

        return response;
    }

    private String createUniqueRegisterKeyUrlSuffix(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    // Cleans all expired register requests
    @UnderDevelopment
    private void cleanExpiredRegisterRequests(){
        List<RegistrationRequest> requestsToDelete = new ArrayList<>();
        for(var i : pendingRegistrationRequests.findAll())
            if(i.getExpiresAt().isBefore(LocalDateTime.now()))
                requestsToDelete.add(i);

        pendingRegistrationRequests.deleteAll(requestsToDelete);
    }

    // Creates a user in the database according to the information received with the request.
    public boolean createUserByRegistrationRequest(RegistrationRequest request,
                                                   HttpServletRequest servletRequest){
        if(request == null)
            return false;

        User newUser = customUserDetailsService.getDefaultUser();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(request.getPassword());
        newUser.setEmail(request.getEmail());

        return customUserDetailsService.saveUser(newUser) == IndoorResponse.SUCCESS;
    }

    public IndoorResponse verifyAccount(String token, HttpServletRequest servletRequest) {
        RegistrationRequest request = null;
        for(var i : pendingRegistrationRequests.findAll())
            if(i.getKey().equals(token)){
                request = i;
                break;
            }

        if(request != null){
            if(request.getExpiresAt().isBefore(LocalDateTime.now())) {
                cleanExpiredRegisterRequests();
                return IndoorResponse.TOKEN_EXPIRED;
            }

            if(createUserByRegistrationRequest(request, servletRequest)) {
                cleanExpiredRegisterRequests();
                return IndoorResponse.SUCCESS;
            }
            else {
                cleanExpiredRegisterRequests();
                return IndoorResponse.FAIL;
            }
        }

        cleanExpiredRegisterRequests();
        return IndoorResponse.TOKEN_ABSENT;
    }
}
