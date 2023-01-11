package com.loam.stoody.service.communication.notification;

import com.loam.stoody.dto.api.response.OutdoorResponse;
import com.loam.stoody.global.annotations.UnderDevelopment;
import com.loam.stoody.global.constants.IndoorResponse;
import com.loam.stoody.model.communication.notification.SimpleNotification;
import com.loam.stoody.model.communication.notification.SimpleNotificationBadge;
import com.loam.stoody.model.user.User;
import com.loam.stoody.repository.communication.notification.SimpleNotificationRepository;
import com.loam.stoody.service.user.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@UnderDevelopment
@Service
@AllArgsConstructor
public class NotificationService {
    private final CustomUserDetailsService customUserDetailsService;
    private final SimpleNotificationRepository simpleNotificationRepository;

    public List<SimpleNotification> getAllSimpleNotifications(){
        return simpleNotificationRepository.findAll();
    }

    public List<SimpleNotification> getAllSimpleNotificationsOfUser(User user){
        return simpleNotificationRepository.findAll().stream().filter(e->customUserDetailsService.compareUsers(e.getReceiver(),user))
                .collect(Collectors.toList());
    }

    public void sendSimpleNotification(User from, User to, String title, String content, SimpleNotificationBadge badge){
        if(title.length() > 254)
            title = title.substring(0,254);
        if(content.length() > 254)
            content = content.substring(0,254);
        SimpleNotification simpleNotification = new SimpleNotification();
        simpleNotification.setSender(from);
        simpleNotification.setReceiver(to);
        simpleNotification.setTitle(title);
        simpleNotification.setContent(content);

        if(badge != null)
            simpleNotification.setBadge(badge);

        simpleNotificationRepository.save(simpleNotification);
    }

    public OutdoorResponse<?> setSimpleNotificationReadById(Long id, Boolean read){
        SimpleNotification simpleNotification = simpleNotificationRepository.findById(id).orElse(null);
        if(simpleNotification == null)
            return new OutdoorResponse<>(IndoorResponse.BAD_REQUEST, "BAD REQUEST");

        simpleNotification.setRead(read);
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, "SUCCESS");
    }
}
