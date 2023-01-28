package com.loam.stoody.service.communication.sms;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.loam.stoody.global.constants.ProjectConfigurationVariables;
import com.loam.stoody.global.logger.ConsoleColors;
import com.loam.stoody.global.logger.StoodyLogger;
import com.loam.stoody.service.utils.CacheService;
import com.twilio.Twilio;
import com.twilio.base.ResourceSet;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class SmsSenderService {

    @Value("${twilio.account_sid}")
    private String ACCOUNT_SID;

    @Value("${twilio.auth_token}")
    private String AUTH_TOKEN;

    @Value("${twilio.trial_number}")
    private String TWILIO_NUMBER;


    public void sendOTP(String username, String toNumber) {
        final Integer otp = CacheService.generateRandomOTP();

        CacheService.put(username, otp);

        // If you want OTP to be sent to User's number, you have to change stoodyEnvironment value!
        if (ProjectConfigurationVariables.stoodyEnvironment.equals(ProjectConfigurationVariables.developmentMode)) {
            StoodyLogger.DebugLog(ConsoleColors.CYAN, "Use this OTP to sign in:" + otp);
        } else {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            Message message = Message.creator(
                            new PhoneNumber(toNumber),
                            new PhoneNumber(TWILIO_NUMBER),
                            "Your Stoody OTP is:" + otp)
                    .create();
        }
    }

    public boolean validateOTP(String token, String username) {
        return CacheService.isValid(token,username);
    }

    @Deprecated // This is not used yet.
    public void isSmsDelivered() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        ListenableFuture<ResourceSet<Message>> future = Message.reader().readAsync();
        Futures.addCallback(
                future,
                new FutureCallback<ResourceSet<Message>>() {
                    public void onSuccess(ResourceSet<Message> messages) {
                        for (Message message : messages) {
                            System.out.println(message.getSid() + " : " + message.getStatus());
                        }
                    }

                    public void onFailure(Throwable t) {
                        System.out.println("Failed to get message status: " + t.getMessage());
                    }
                });
    }
}
