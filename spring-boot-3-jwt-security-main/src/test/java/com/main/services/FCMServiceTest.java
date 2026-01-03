package com.main.services;

import com.google.firebase.messaging.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FCMServiceTest {

    private FCMService fcmService;
    private FirebaseMessaging mockFirebaseMessaging;
    private BatchResponse mockBatchResponse;

    @BeforeEach
    void setUp() {
        fcmService = new FCMService();
        mockFirebaseMessaging = mock(FirebaseMessaging.class);
        mockBatchResponse = mock(BatchResponse.class);
    }

    @Test
    void testSendNotificationWithValidTokens() {
        // Arrange
        String expectedTitle = "Test Title";
        String expectedBody = "Test Body";
        Set<String> tokens = Set.of("token1", "token2", "token3");

        try (MockedStatic<FirebaseMessaging> mockedStatic = mockStatic(FirebaseMessaging.class)) {
            mockedStatic.when(FirebaseMessaging::getInstance).thenReturn(mockFirebaseMessaging);
            try {
                doReturn(mockBatchResponse).when(mockFirebaseMessaging).sendMulticast(any(MulticastMessage.class));
            } catch (FirebaseMessagingException e) {
                fail("Unexpected exception during mock setup: " + e.getMessage());
            }

            // Act
            assertDoesNotThrow(() -> fcmService.sendNotification(expectedTitle, expectedBody, tokens));

            // Assert
            try {
                verify(mockFirebaseMessaging, times(1)).sendMulticast(any(MulticastMessage.class));
            } catch (FirebaseMessagingException e) {
                // This should never happen in verify, but required for compilation
                fail("Unexpected exception during verification: " + e.getMessage());
            }
        }
    }

    @Test
    void testSendNotificationWithNullTokens() {
        // Arrange
        String title = "Test Title";
        String body = "Test Body";
        Set<String> tokens = null;

        try (MockedStatic<FirebaseMessaging> mockedStatic = mockStatic(FirebaseMessaging.class)) {
            mockedStatic.when(FirebaseMessaging::getInstance).thenReturn(mockFirebaseMessaging);

            // Act
            assertDoesNotThrow(() -> fcmService.sendNotification(title, body, tokens));

            // Assert - should return early and not call Firebase
            try {
                verify(mockFirebaseMessaging, never()).sendMulticast(any(MulticastMessage.class));
            } catch (FirebaseMessagingException e) {
                // This should never happen in verify, but required for compilation
                fail("Unexpected exception during verification: " + e.getMessage());
            }
        }
    }

    @Test
    void testSendNotificationWithEmptyTokens() {
        // Arrange
        String title = "Test Title";
        String body = "Test Body";
        Set<String> tokens = new HashSet<>();

        try (MockedStatic<FirebaseMessaging> mockedStatic = mockStatic(FirebaseMessaging.class)) {
            mockedStatic.when(FirebaseMessaging::getInstance).thenReturn(mockFirebaseMessaging);

            // Act
            assertDoesNotThrow(() -> fcmService.sendNotification(title, body, tokens));

            // Assert - should return early and not call Firebase
            try {
                verify(mockFirebaseMessaging, never()).sendMulticast(any(MulticastMessage.class));
            } catch (FirebaseMessagingException e) {
                // This should never happen in verify, but required for compilation
                fail("Unexpected exception during verification: " + e.getMessage());
            }
        }
    }

    @Test
    void testSendNotificationHandlesFirebaseException() {
        // Arrange
        String title = "Test Title";
        String body = "Test Body";
        Set<String> tokens = Set.of("invalid-token");

        try (MockedStatic<FirebaseMessaging> mockedStatic = mockStatic(FirebaseMessaging.class)) {
            mockedStatic.when(FirebaseMessaging::getInstance).thenReturn(mockFirebaseMessaging);
            FirebaseMessagingException exception = mock(FirebaseMessagingException.class);
            try {
                doThrow(exception).when(mockFirebaseMessaging).sendMulticast(any(MulticastMessage.class));
            } catch (FirebaseMessagingException e) {
                fail("Unexpected exception during mock setup: " + e.getMessage());
            }

            // Act & Assert - should not throw exception, should catch and log
            assertDoesNotThrow(() -> fcmService.sendNotification(title, body, tokens));
            try {
                verify(mockFirebaseMessaging, times(1)).sendMulticast(any(MulticastMessage.class));
            } catch (FirebaseMessagingException e) {
                // This should never happen in verify, but required for compilation
                fail("Unexpected exception during verification: " + e.getMessage());
            }
        }
    }

    @Test
    void testSendNotificationWithSingleToken() {
        // Arrange
        String title = "Single Token Test";
        String body = "Testing single token";
        Set<String> tokens = Set.of("single-token");

        try (MockedStatic<FirebaseMessaging> mockedStatic = mockStatic(FirebaseMessaging.class)) {
            mockedStatic.when(FirebaseMessaging::getInstance).thenReturn(mockFirebaseMessaging);
            try {
                doReturn(mockBatchResponse).when(mockFirebaseMessaging).sendMulticast(any(MulticastMessage.class));
            } catch (FirebaseMessagingException e) {
                fail("Unexpected exception during mock setup: " + e.getMessage());
            }

            // Act
            assertDoesNotThrow(() -> fcmService.sendNotification(title, body, tokens));

            // Assert
            try {
                verify(mockFirebaseMessaging, times(1)).sendMulticast(any(MulticastMessage.class));
            } catch (FirebaseMessagingException e) {
                // This should never happen in verify, but required for compilation
                fail("Unexpected exception during verification: " + e.getMessage());
            }
        }
    }

    @Test
    void testSendNotificationWithLargeTokenSet() {
        // Arrange
        String title = "Large Set Test";
        String body = "Testing with many tokens";
        Set<String> tokens = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            tokens.add("token-" + i);
        }

        try (MockedStatic<FirebaseMessaging> mockedStatic = mockStatic(FirebaseMessaging.class)) {
            mockedStatic.when(FirebaseMessaging::getInstance).thenReturn(mockFirebaseMessaging);
            try {
                doReturn(mockBatchResponse).when(mockFirebaseMessaging).sendMulticast(any(MulticastMessage.class));
            } catch (FirebaseMessagingException e) {
                fail("Unexpected exception during mock setup: " + e.getMessage());
            }

            // Act
            assertDoesNotThrow(() -> fcmService.sendNotification(title, body, tokens));

            // Assert
            try {
                verify(mockFirebaseMessaging, times(1)).sendMulticast(any(MulticastMessage.class));
            } catch (FirebaseMessagingException e) {
                // This should never happen in verify, but required for compilation
                fail("Unexpected exception during verification: " + e.getMessage());
            }
        }
    }

    @Test
    void testSendNotificationCallsFirebaseMessaging() {
        // Arrange
        String title = "Test Title";
        String body = "Test Body";
        Set<String> tokens = Set.of("token1", "token2");

        try (MockedStatic<FirebaseMessaging> mockedStatic = mockStatic(FirebaseMessaging.class)) {
            mockedStatic.when(FirebaseMessaging::getInstance).thenReturn(mockFirebaseMessaging);
            try {
                doReturn(mockBatchResponse).when(mockFirebaseMessaging).sendMulticast(any(MulticastMessage.class));
            } catch (FirebaseMessagingException e) {
                fail("Unexpected exception during mock setup: " + e.getMessage());
            }

            // Act
            fcmService.sendNotification(title, body, tokens);

            // Assert - verify FirebaseMessaging.getInstance() was called
            mockedStatic.verify(FirebaseMessaging::getInstance, times(1));
            // Assert - verify sendMulticast was called with a MulticastMessage
            try {
                verify(mockFirebaseMessaging, times(1)).sendMulticast(any(MulticastMessage.class));
            } catch (FirebaseMessagingException e) {
                // This should never happen in verify, but required for compilation
                fail("Unexpected exception during verification: " + e.getMessage());
            }
        }
    }
}

