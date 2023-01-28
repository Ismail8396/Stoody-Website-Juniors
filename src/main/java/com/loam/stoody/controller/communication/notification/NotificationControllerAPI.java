package com.loam.stoody.controller.communication.notification;

import com.loam.stoody.dto.api.response.OutdoorResponse;
import com.loam.stoody.global.annotations.UnderDevelopment;
import com.loam.stoody.global.constants.IndoorResponse;
import com.loam.stoody.global.constants.PRL;
import com.loam.stoody.model.user.User;
import com.loam.stoody.service.communication.notification.NotificationService;
import com.loam.stoody.service.user.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@UnderDevelopment
@RestController
@AllArgsConstructor
public class NotificationControllerAPI {
    private final CustomUserDetailsService customUserDetailsService;
    private final NotificationService notificationService;

    @GetMapping(PRL.apiPrefix+"/user/notifications/simple/get/all")
    public OutdoorResponse<?> getAllSimpleNotificationsRenameThisLaterBecauseTheMethodBelowIsTheSame(){
        try {
            User user = customUserDetailsService.getCurrentUser();
            return new OutdoorResponse<>(IndoorResponse.SUCCESS, notificationService.getAllSimpleNotificationsOfUser(user));
        }catch (UsernameNotFoundException ignore){
            return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST, "BAD REQUEST");
        }
    }

    @GetMapping(PRL.apiPrefix+"/user/current/notifications/simple/get/all")
    public OutdoorResponse<?> getAllSimpleNotifications(){
        try {
            User user = customUserDetailsService.getCurrentUser();
            return new OutdoorResponse<>(IndoorResponse.SUCCESS, notificationService.getAllSimpleNotificationsOfUser(user));
        }catch (UsernameNotFoundException ignore){
            return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST, "BAD REQUEST");
        }
    }

    @PostMapping(PRL.apiPrefix+"/user/notifications/simple/post")
    public OutdoorResponse<?> postSimpleNotification(@RequestParam("from")String from, @RequestParam("to")String to,
                                                     @RequestParam("content")String content){
        try {
            User user = customUserDetailsService.getUserByUsername(from);
            User toUser = customUserDetailsService.getUserByUsername(to);

            notificationService.sendSimpleNotification(user ,toUser,content, null);
            notificationService.sendPushNotification(to,"n");

            return new OutdoorResponse<>(IndoorResponse.SUCCESS, "SUCCESS");
        }catch (UsernameNotFoundException ignore){
            return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST, "BAD REQUEST");
        }
    }

    @PostMapping(PRL.apiPrefix+"/user/notifications/simple/read")
    public OutdoorResponse<?> postSimpleNotificationRead(@RequestParam("id")Long id){
        try {
            User user = customUserDetailsService.getCurrentUser();

            if(!notificationService.setReadSimpleNotification(id,true))
                throw new RuntimeException();

            return new OutdoorResponse<>(IndoorResponse.SUCCESS, "SUCCESS");
        }catch (RuntimeException ignore){
            return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST, "BAD REQUEST");
        }
    }

    @PostMapping(PRL.apiPrefix+"/user/notifications/simple/remove")
    public OutdoorResponse<?> postSimpleNotificationRemove(@RequestParam("id")Long id){
        try {
            User user = customUserDetailsService.getCurrentUser();

            if(!notificationService.removeSimpleNotification(id))
                throw new RuntimeException();

            return new OutdoorResponse<>(IndoorResponse.SUCCESS, "SUCCESS");
        }catch (RuntimeException ignore){
            return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST, "BAD REQUEST");
        }
    }

    // -> Push notification
    @GetMapping("/sendNotification/{username}/{message}")
    public void sendNotification(@PathVariable("username") String username,
                                 @PathVariable("message") String message) {
        notificationService.sendPushNotification(username, message);
    }

    @GetMapping("/sendNotificationToAll/{message}")
    public void sendNotificationToAll(@PathVariable("message") String message) {
        notificationService.sendPushNotificationToAll(message);
    }
}