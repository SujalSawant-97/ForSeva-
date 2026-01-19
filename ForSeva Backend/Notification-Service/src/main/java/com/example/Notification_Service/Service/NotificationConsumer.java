package com.example.Notification_Service.Service;

import com.example.Notification_Service.DTO.JobEvent;
import com.example.Notification_Service.Model.Notification;
import com.example.Notification_Service.Repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {

    @Autowired
    private NotificationRepository repository;

    @KafkaListener(topics = "job-events", groupId = "notification-group")
    public void handleJobEvent(JobEvent event) {
        // 1. Create a Notification History record
        Notification history = new Notification();
        history.setRecipientId(determineRecipient(event));
        history.setTitle(event.getStatus());
        history.setMessage(event.getMessage());
        history.setJobId(event.getJobId());

        // 2. Save to PostgreSQL for the Bell Icon
        repository.save(history);

        // 3. (Optional) Trigger Real-time WebSocket or Push notification
    }

    private String determineRecipient(JobEvent event) {
        // If status is PENDING, notify Worker. If ACCEPTED, notify Customer.
        return "PENDING".equals(event.getStatus()) ? event.getWorkerId() : event.getCustomerId();
    }
}
