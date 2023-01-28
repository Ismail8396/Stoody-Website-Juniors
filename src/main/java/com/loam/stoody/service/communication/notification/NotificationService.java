package com.loam.stoody.service.communication.notification;

import com.loam.stoody.dto.api.response.SimpleNotificationResponseDTO;
import com.loam.stoody.enums.SimpleNotificationBadge;
import com.loam.stoody.global.annotations.UnderDevelopment;
import com.loam.stoody.model.communication.notification.SimpleNotification;
import com.loam.stoody.model.user.User;
import com.loam.stoody.repository.communication.notification.SimpleNotificationRepository;
import com.loam.stoody.service.user.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@UnderDevelopment
@Service
@AllArgsConstructor
public class NotificationService {
    private final CustomUserDetailsService customUserDetailsService;
    private final SimpleNotificationRepository simpleNotificationRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public List<SimpleNotification> getAllSimpleNotifications() {
        return simpleNotificationRepository.findAll();
    }

    public List<SimpleNotificationResponseDTO> getAllSimpleNotificationsOfUser(User user) {
        List<SimpleNotificationResponseDTO> simpleNotifications = new ArrayList<>();
        simpleNotificationRepository.findAll().stream()
                .filter(e -> customUserDetailsService.compareUsers(e.getReceiver(), user))
                .forEach(e -> simpleNotifications.add(convertSimpleNotificationEntityToDTO(e)));
        return simpleNotifications;
    }

    public void sendSimpleNotification(User from, User to, String content, SimpleNotificationBadge badge) {
        if (content.length() > 254) content = content.substring(0, 254);
        SimpleNotification simpleNotification = new SimpleNotification();
        simpleNotification.setSender(from);
        simpleNotification.setReceiver(to);
        simpleNotification.setContent(content);
        simpleNotification.setCreatedDate(LocalDateTime.now());

        if (badge != null) simpleNotification.setBadge(badge);

        simpleNotificationRepository.save(simpleNotification);
    }

    public boolean setReadSimpleNotification(Long id, boolean read) {
        Optional<SimpleNotification> simpleNotification = simpleNotificationRepository.findById(id);
        if(simpleNotification.isPresent()){
            simpleNotification.get().setRead(read);
            simpleNotificationRepository.save(simpleNotification.get());
            return true;
        }
        return false;
    }

    public boolean removeSimpleNotification(Long id){
        Optional<SimpleNotification> simpleNotification = simpleNotificationRepository.findById(id);
        if(simpleNotification.isPresent()){
            simpleNotificationRepository.deleteById(simpleNotification.get().getId());
            return true;
        }
        return false;
    }

    public SimpleNotificationResponseDTO convertSimpleNotificationEntityToDTO(SimpleNotification simpleNotification) {
        if (simpleNotification == null)
            return new SimpleNotificationResponseDTO();

        SimpleNotificationResponseDTO simpleNotificationResponseDTO = new SimpleNotificationResponseDTO();
        simpleNotificationResponseDTO.setId(simpleNotification.getId());
        if (simpleNotification.getSender() != null) {
            simpleNotificationResponseDTO.setSenderId(simpleNotification.getSender().getId());
            simpleNotificationResponseDTO.setSenderUsername(simpleNotification.getSender().getUsername());
            simpleNotificationResponseDTO.setSenderPhotoUrl(customUserDetailsService.getUserProfile(simpleNotification.getSender()).getProfilePictureURL());
        }
        if (simpleNotification.getReceiver() != null)
            simpleNotificationResponseDTO.setReceiverId(simpleNotification.getReceiver().getId());
        simpleNotificationResponseDTO.setContent(simpleNotification.getContent());
        if (simpleNotification.getBadge() != null)
            simpleNotificationResponseDTO.setBadge(simpleNotification.getBadge().toString());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        simpleNotificationResponseDTO.setCreatedAt(simpleNotification.getCreatedDate().format(formatter));

        simpleNotificationResponseDTO.setRead(simpleNotification.isRead());

        return simpleNotificationResponseDTO;
    }

    //-> Push notifications
    public void sendPushNotification(String to, String message) {
        simpMessagingTemplate.convertAndSendToUser(to, "/specific", message);
    }

    public void sendPushNotificationToAll(String message) {
        simpMessagingTemplate.convertAndSend("/all/messages", message);
    }
}