package com.loam.stoody.dto.api.response;

import lombok.Data;

@Data
public class SimpleNotificationResponseDTO {
    private Long id;
    private Long senderId;
    private Long receiverId;

    private String senderUsername;
    private String senderPhotoUrl;

    private String content;
    private String badge;
    private boolean read;

    private String createdAt;
}
