package com.example.Notification_Service.Controller;

import com.example.Notification_Service.DTO.ApiResponse;
import com.example.Notification_Service.DTO.NotificationModelDTO;
import com.example.Notification_Service.Model.Notification;
import com.example.Notification_Service.Repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationRepository repository;

    // Get notifications for the logged-in user
    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationModelDTO>>> getMyNotifications(@RequestHeader("X-User-Id") String userId) {
        List<Notification> entities = repository.findByRecipientIdOrderByCreatedAtDesc(userId);
        List<NotificationModelDTO> dtos = entities.stream()
                .map(notification -> new NotificationModelDTO(
                        notification.getTitle(),
                        notification.getMessage(),
                        notification.getCreatedAt().toString() // Converts LocalDateTime to String
                ))
                .collect(Collectors.toList());
        // C. MARK AS READ LOGIC (The "Side Effect")
        // Filter only the ones that are currently unread to avoid unnecessary updates
        List<Notification> unreadNotifications = entities.stream()
                .filter(n -> !n.isRead()) // only if read is false
                .peek(n -> n.setRead(true)) // set to true
                .collect(Collectors.toList());

        // D. Save changes to Database
        if (!unreadNotifications.isEmpty()) {
            repository.saveAll(unreadNotifications);
        }
        return ResponseEntity.ok(ApiResponse.success(dtos));
    }
    @GetMapping("/unread-count")
    public ResponseEntity<ApiResponse<Integer>> getUnreadCount(@RequestHeader("X-User-Id") String userId) {
        int count = repository.countByRecipientIdAndReadFalse(userId);
        return ResponseEntity.ok( ApiResponse.success(count));
    }

    // Mark a notification as read (removes the red dot)
    @PatchMapping("/{id}/read")
    public ResponseEntity<ApiResponse<String>> markAsRead(@PathVariable String id) {
        Notification n = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        n.setRead(true); // Matches 'private boolean read'
        repository.save(n);
        return ResponseEntity.ok(ApiResponse.success("Marked as read"));
    }
}