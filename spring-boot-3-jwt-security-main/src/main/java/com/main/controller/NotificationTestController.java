package com.main.controller;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.main.repository.FCMTokenRepository;
import com.main.services.FCMService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/test/notifications")
@RequiredArgsConstructor
@Profile("test")
public class NotificationTestController {

    private final FCMService fcmService;
    private final FCMTokenRepository fcmTokenRepository;

    @GetMapping("/health")
    public ResponseEntity<?> checkFirebaseHealth() {
        Map<String, Object> health = new HashMap<>();
        
        try {
            // Check if Firebase is initialized
            FirebaseApp app = FirebaseApp.getInstance();
            health.put("firebaseInitialized", true);
            health.put("firebaseAppName", app.getName());
            
            if (app.getOptions() != null) {
                health.put("projectId", app.getOptions().getProjectId());
            }
            
            // Check if FirebaseMessaging is available
            try {
                FirebaseMessaging.getInstance();
                health.put("firebaseMessagingAvailable", true);
            } catch (Exception e) {
                health.put("firebaseMessagingAvailable", false);
                health.put("firebaseMessagingError", e.getMessage());
            }
            
            // Count tokens
            long tokenCount = fcmTokenRepository.count();
            health.put("registeredTokens", tokenCount);
            
            health.put("status", "healthy");
            return ResponseEntity.ok(health);
        } catch (IllegalStateException e) {
            health.put("firebaseInitialized", false);
            health.put("status", "unhealthy");
            health.put("error", "Firebase not initialized: " + e.getMessage());
            health.put("suggestion", "Check if serviceAccountKey.json exists in src/main/resources");
            return ResponseEntity.status(500).body(health);
        } catch (Exception e) {
            health.put("status", "error");
            health.put("error", e.getMessage());
            return ResponseEntity.status(500).body(health);
        }
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendTestNotification(
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "Test Notification") String title,
            @RequestParam(defaultValue = "This is a test notification") String body) {
        
        try {
            Set<String> tokens;
            
            if (userId != null) {
                // Get tokens for specific user
                tokens = fcmTokenRepository.findAllTokensByUser(userId)
                        .stream()
                        .map(token -> token.getToken())
                        .collect(Collectors.toSet());
                
                if (tokens.isEmpty()) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("message", "No FCM tokens found for user ID: " + userId);
                    response.put("userId", userId);
                    return ResponseEntity.badRequest().body(response);
                }
            } else {
                // Get all tokens (for testing)
                tokens = fcmTokenRepository.findAll()
                        .stream()
                        .map(token -> token.getToken())
                        .collect(Collectors.toSet());
                
                if (tokens.isEmpty()) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("message", "No FCM tokens found in the system");
                    return ResponseEntity.badRequest().body(response);
                }
            }
            
            com.google.firebase.messaging.BatchResponse response = 
                    fcmService.sendNotificationWithResponse(title, body, tokens);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "Notification sent successfully");
            result.put("totalTokens", tokens.size());
            
            if (response != null) {
                result.put("successCount", response.getSuccessCount());
                result.put("failureCount", response.getFailureCount());
            } else {
                result.put("successCount", 0);
                result.put("failureCount", 0);
            }
            
            if (userId != null) {
                result.put("userId", userId);
            }
            
            result.put("title", title);
            result.put("body", body);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error sending notification: " + e.getMessage());
            if (userId != null) {
                errorResponse.put("userId", userId);
            }
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}

