package com.example.Notification_Service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationModelDTO {
    private String title;
    private String message;
    private String timestamp;
}
