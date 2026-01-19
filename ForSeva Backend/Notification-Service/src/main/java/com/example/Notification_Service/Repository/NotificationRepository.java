package com.example.Notification_Service.Repository;

import com.example.Notification_Service.Model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,String> {
    List<Notification> findByRecipientIdOrderByCreatedAtDesc(String recipientId);
    //Long countByUserIdAndIsReadFalse(Long userId);
    int countByRecipientIdAndReadFalse(String recipientId);
}
