package com.main.services;

import com.google.firebase.messaging.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class FCMService {
    private static final Logger logger = LoggerFactory.getLogger(FCMService.class);

    public void sendNotification(String title, String body, Set<String> tokens) {
        logger.info("sending notification to " + tokens);
        try {
            if (tokens == null || tokens.isEmpty()) {
                return;
            }
            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build();
            FirebaseMessaging.getInstance().sendMulticast(MulticastMessage.builder()
                    .setNotification(notification)
                    .addAllTokens(tokens)
                    .build());
        } catch (FirebaseMessagingException e) {
            logger.error("firebase error sending notification" , e);
        }
    }

    public BatchResponse sendNotificationWithResponse(String title, String body, Set<String> tokens) {
        logger.info("Sending notification to {} tokens", tokens != null ? tokens.size() : 0);
        
        if (tokens == null || tokens.isEmpty()) {
            logger.warn("No tokens provided, skipping notification");
            return null;
        }

        try {
            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build();
            
            MulticastMessage message = MulticastMessage.builder()
                    .setNotification(notification)
                    .addAllTokens(tokens)
                    .build();
            
            BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
            
            // Log delivery results
            logDeliveryResults(response, tokens);
            
            return response;
        } catch (FirebaseMessagingException e) {
            String errorCode = e.getErrorCode() != null ? e.getErrorCode().toString() : null;
            String errorMessage = e.getMessage();
            
            logger.error("Firebase error sending notification - Error Code: {}, Message: {}", 
                    errorCode, errorMessage, e);
            
            // Provide helpful error messages for common issues
            if ((errorCode != null && errorCode.contains("404")) || 
                (errorMessage != null && errorMessage.contains("404"))) {
                String helpfulMessage = "Firebase Cloud Messaging API returned 404. " +
                        "Please verify:\n" +
                        "1. FCM API is enabled in Google Cloud Console\n" +
                        "2. Service account has 'Firebase Cloud Messaging API Admin' role\n" +
                        "3. Project ID in serviceAccountKey.json matches your Firebase project\n" +
                        "4. Firebase project has Cloud Messaging enabled";
                logger.error(helpfulMessage);
                throw new RuntimeException(helpfulMessage + "\nOriginal error: " + errorMessage, e);
            }
            
            throw new RuntimeException("Failed to send notification: " + errorMessage, e);
        }
    }

    private void logDeliveryResults(BatchResponse response, Set<String> tokens) {
        int successCount = response.getSuccessCount();
        int failureCount = response.getFailureCount();
        
        logger.info("Notification delivery results - Success: {}, Failed: {}, Total: {}", 
                successCount, failureCount, tokens.size());
        
        if (failureCount > 0) {
            logger.warn("Some notifications failed to deliver:");
            int index = 0;
            for (String token : tokens) {
                if (index < response.getResponses().size()) {
                    SendResponse sendResponse = response.getResponses().get(index);
                    if (!sendResponse.isSuccessful()) {
                        logger.error("Token {} failed: {}", 
                                token, 
                                sendResponse.getException().getMessage());
                    }
                }
                index++;
            }
        }
    }
}
