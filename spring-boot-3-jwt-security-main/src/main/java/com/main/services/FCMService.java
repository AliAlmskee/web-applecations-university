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
}
