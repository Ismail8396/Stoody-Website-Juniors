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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@UnderDevelopment
@RestController
@AllArgsConstructor
public class NotificationControllerAPI {
    private final CustomUserDetailsService customUserDetailsService;
    private final NotificationService notificationService;

    @GetMapping(PRL.apiPrefix+"/user/notifications/simple/get/all")
    public OutdoorResponse<?> getAllSimpleNotifications(@RequestParam("username")String username){
        try {
            User user = customUserDetailsService.getUserByUsername(username);
            User currentUser = customUserDetailsService.getUserByUsername(customUserDetailsService.getCurrentUser().getUsername());
            if(!customUserDetailsService.compareUsers(user, currentUser))
                throw new IllegalAccessException();

            return new OutdoorResponse<>(IndoorResponse.SUCCESS, notificationService.getAllSimpleNotificationsOfUser(currentUser));
        }catch (UsernameNotFoundException ignore){
            return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST, "BAD REQUEST");
        }catch(IllegalAccessException ignore){
            return new OutdoorResponse<>(IndoorResponse.ACCESS_DENIED, "ACCESS DENIED!");
        }
    }

    // TODO: authorization check
    @PostMapping(PRL.apiPrefix+"/user/notifications/simple/post")
    public OutdoorResponse<?> postSimpleNotification(@RequestParam("from")String from, @RequestParam("to")String to,
                                                     @RequestParam("title")String title, @RequestParam("content")String content){
        try {
            User user = customUserDetailsService.getUserByUsername(from);
            User currentUser = customUserDetailsService.getUserByUsername(customUserDetailsService.getCurrentUser().getUsername());
            User toUser = customUserDetailsService.getUserByUsername(to);
            if(!customUserDetailsService.compareUsers(user, currentUser))
                throw new IllegalAccessException();

            notificationService.sendSimpleNotification(currentUser,toUser,title,content, null);

            return new OutdoorResponse<>(IndoorResponse.SUCCESS, "SUCCESS");
        }catch (UsernameNotFoundException ignore){
            return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST, "BAD REQUEST");
        }catch(IllegalAccessException ignore){
            return new OutdoorResponse<>(IndoorResponse.ACCESS_DENIED, "ACCESS DENIED!");
        }
    }
}
