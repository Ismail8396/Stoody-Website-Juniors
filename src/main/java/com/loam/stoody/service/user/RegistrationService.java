package com.loam.stoody.service.user;

import com.loam.stoody.dto.api.request.RegistrationRequestDTO;
import com.loam.stoody.global.annotations.UnderDevelopment;
import com.loam.stoody.global.constants.PRL;
import com.loam.stoody.global.logger.ConsoleColors;
import com.loam.stoody.global.logger.StoodyLogger;
import com.loam.stoody.model.user.User;
import com.loam.stoody.model.user.requests.RegistrationRequest;
import com.loam.stoody.global.constants.IndoorResponses;
import com.loam.stoody.repository.user.PendingRegistrationRequests;
import com.loam.stoody.repository.user.RoleRepository;
import com.loam.stoody.repository.user.UserRepository;
import com.loam.stoody.service.communication.email.EmailSenderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@UnderDevelopment
@Service
@AllArgsConstructor
public class RegistrationService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final CustomUserDetailsService customUserDetailsService;
    private final RoleRepository roleRepository;
    private final EmailSenderService emailSenderService;
    private final PendingRegistrationRequests pendingRegistrationRequests;

    private IndoorResponses isUserPending(String username, String email){
        IndoorResponses response = IndoorResponses.SUCCESS;

        for(var i : pendingRegistrationRequests.findAll()){
            if (i.getUsername().equals(username)) {
                response = IndoorResponses.USERNAME_EXIST;
                break;
            }
        }

        for(var i : pendingRegistrationRequests.findAll()){
            if (i.getEmail().equals(email)) {
                response = response == IndoorResponses.SUCCESS ? IndoorResponses.EMAIL_EXIST : IndoorResponses.USERNAME_EMAIL_EXIST;
                break;
            }
        }

        return response;
    }

    public IndoorResponses doesUserExist(String username, String email){
        IndoorResponses response = isUserPending(username, email);
        if(response != IndoorResponses.SUCCESS)
            return response;

        return customUserDetailsService.doesUserExist(username,email);
    }

    public IndoorResponses sendTokenToEmail(RegistrationRequestDTO registrationRequest, HttpServletRequest httpServletRequest){
        IndoorResponses response = doesUserExist(registrationRequest.getUsername(),registrationRequest.getEmail());

        if(response == IndoorResponses.SUCCESS) {
            // Clean Expired Requests
            cleanExpiredRegisterRequests();

            RegistrationRequest newRequest = new RegistrationRequest();

            // Set user info
            newRequest.setUsername(registrationRequest.getUsername());
            newRequest.setEmail(registrationRequest.getEmail());
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(11, new SecureRandom());
            newRequest.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            // Create Unique Key
            newRequest.setKey(createUniqueRegisterKey());
            // Set createdAt time
            newRequest.setCreatedAt(LocalDateTime.now());
            // Set expiration date
            newRequest.setExpiresAt(LocalDateTime.now().plusMinutes(1));
            // Save registration request
            pendingRegistrationRequests.save(newRequest);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            emailSenderService.sendEmail(registrationRequest.getEmail(),
                    "Stoody Account Verification",
                    "Hi! Someone with the username "+registrationRequest.getUsername()+
                            " wanted to register on Stoody.org. If it was you, please click the link below to activate your account:\n\n"
                            + "http://"+httpServletRequest.getHeader("host")+PRL.signUpURL+PRL.apiVerifySuffixURL+"/"+newRequest.getKey()
                            +
                            "\n\nIf this request was not fulfilled by you, you can skip this e-mail. " +
                            "\n\nLink expires at:\t"+newRequest.getExpiresAt().format(formatter));
        }

        return response;
    }

    private String createUniqueRegisterKey(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    // Cleans all expired register requests
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

        User newUser = new User();

        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        newUser.setRoles(roleRepository.findBySearchKey("ROLE_USER"));

        newUser.setAccountEnabled(true);
        newUser.setAccountExpired(false);
        newUser.setAccountLocked(false);
        newUser.setCredentialsExpired(false);

        // Try to save user
        // If we get anything other than success, it'll return false
        boolean response = customUserDetailsService.createUser(newUser) == IndoorResponses.SUCCESS;
        if(response)
            try {
                servletRequest.login(newUser.getEmail(), newUser.getPassword());
            } catch (ServletException e) {
                StoodyLogger.DebugLog(ConsoleColors.YELLOW,"Servlet exception is thrown while auto login! Message: "+e.getMessage());
            }

        return response;
    }

    public IndoorResponses verifyAccount(String token, HttpServletRequest servletRequest) {
        RegistrationRequest request = null;
        for(var i : pendingRegistrationRequests.findAll())
            if(i.getKey().equals(token)){
                request = i;
                break;
            }

        if(request != null){
            if(request.getExpiresAt().isBefore(LocalDateTime.now())) {
                cleanExpiredRegisterRequests();
                return IndoorResponses.TOKEN_EXPIRED;
            }

            if(createUserByRegistrationRequest(request, servletRequest)) {
                cleanExpiredRegisterRequests();
                return IndoorResponses.SUCCESS;
            }
            else {
                cleanExpiredRegisterRequests();
                return IndoorResponses.FAIL;
            }
        }

        cleanExpiredRegisterRequests();
        return IndoorResponses.TOKEN_ABSENT;
    }
}
