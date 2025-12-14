package com.main.services;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FCMServiceTest {

    private FCMService fcmService;

    @BeforeEach
    void setUp() {
        fcmService = new FCMService();
        // Initialize Firebase for testing if not already initialized
        try {
            FirebaseApp.getInstance();
        } catch (IllegalStateException e) {
            // Firebase not initialized, try to initialize it
            try {
                GoogleCredentials credentials = GoogleCredentials.fromStream(
                        new FileInputStream("src/main/resources/keys/serviceAccountKey.json"));
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(credentials)
                        .setProjectId("programminglangs-bf5d1")
                        .build();
                FirebaseApp.initializeApp(options);
            } catch (IOException | IllegalStateException ex) {
                // If initialization fails, tests will handle it
            }
        }
    }

    @Test
    void testFCMServiceConfiguration() {
        // Test that FCMService can be instantiated
        assertNotNull(fcmService, "FCMService should be instantiated");

        // Test that Firebase is initialized
        try {
            FirebaseApp app = FirebaseApp.getInstance();
            assertNotNull(app, "FirebaseApp should be initialized");
            assertEquals("programminglangs-bf5d1", app.getOptions().getProjectId(),
                    "Firebase project ID should match configuration");
        } catch (IllegalStateException e) {
            // If Firebase is not initialized, this test will fail
            fail("Firebase should be initialized: " + e.getMessage());
        }
    }

    @Test
    void testFirebaseMessagingInstance() {
        // Test that FirebaseMessaging can be accessed
        try {
            FirebaseMessaging messaging = FirebaseMessaging.getInstance();
            assertNotNull(messaging, "FirebaseMessaging instance should be available");
        } catch (IllegalStateException e) {
            fail("FirebaseMessaging should be available: " + e.getMessage());
        }
    }

    @Test
    void testFirebaseAppOptions() {
        // Test Firebase configuration options
        try {
            FirebaseApp app = FirebaseApp.getInstance();
            assertNotNull(app.getOptions(), "Firebase options should not be null");
            assertNotNull(app.getOptions().getProjectId(), "Firebase project ID should not be null");
            assertFalse(app.getOptions().getProjectId().isEmpty(), "Firebase project ID should not be empty");
        } catch (IllegalStateException e) {
            fail("Firebase should be properly configured: " + e.getMessage());
        }
    }
}

