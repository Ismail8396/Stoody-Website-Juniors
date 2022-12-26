package com.loam.stoody.service.user;

import com.loam.stoody.dto.api.request.RegistrationRequestDTO;
import com.loam.stoody.model.user.User;
import com.loam.stoody.model.user.requests.RegistrationRequest;
import com.loam.stoody.global.constants.IndoorResponses;
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

@Service
@AllArgsConstructor
public class RegistrationService {
    private final CustomUserDetailsService customUserDetailsService;
    private final RoleRepository roleRepository;
    private final EmailSenderService emailSenderService;
    private final PendingRegistrationRequests pendingRegistrationRequests;
    private final UserRepository userRepository;


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

    public IndoorResponses register(RegistrationRequestDTO registrationRequest, HttpServletRequest httpServletRequest){
        IndoorResponses response = doesUserExist(registrationRequest.getUsername(),registrationRequest.getEmail());

        if(response == IndoorResponses.SUCCESS) {
            // Clean Expired Requests
            cleanExpiredRegisterRequests();

            RegistrationRequest newRequest = new RegistrationRequest();

            // Set user info
            newRequest.setUsername(registrationRequest.getUsername());
            newRequest.setEmail(registrationRequest.getEmail());
            newRequest.setPassword(registrationRequest.getPassword());
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
                            + "http://"+httpServletRequest.getHeader("host")+"/stoody/api/v1/registration/user/register/verify/"+newRequest.getKey()
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

    public boolean createUserByRegistrationRequest(RegistrationRequest request){
        if(request == null)
            return false;

        User newUser = new User();
        // BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(11, new SecureRandom());
        newUser.setUsername(request.getUsername());
        newUser.setPassword(request.getPassword());
        newUser.setEmail(request.getEmail());
        newUser.setRoles(roleRepository.findBySearchKey("ROLE_USER"));

        newUser.setAccountEnabled(true);
        newUser.setAccountExpired(false);
        newUser.setAccountLocked(false);
        newUser.setCredentialsExpired(false);

        // Try to save user
        // If we get anything other than success, it'll return false
        return customUserDetailsService.createUser(newUser) == IndoorResponses.SUCCESS;
    }

    public IndoorResponses verifyAccount(String token) {
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

            if(createUserByRegistrationRequest(request)) {
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
