package com.main.services;

import com.main.entity.User;
import com.main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Service
public class OTPService {

    private static final long OTP_EXPIRY_TIME = 5 * 60 * 1000;
    private static final int TEST_OTP = 123456; // Fixed OTP for test users
    private static final Random random = new Random();
    private final UserRepository userRepository;
    
    @Value("${application.test-mode.enabled:false}")
    private boolean testModeEnabled;
    
    @Value("${application.test-mode.test-phones:}")
    private String testPhonesString;

    public OTPService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    private Set<String> getTestPhones() {
        if (testPhonesString == null || testPhonesString.trim().isEmpty()) {
            return new HashSet<>();
        }
        return new HashSet<>(Arrays.asList(testPhonesString.split(",")));
    }

    public int generateOTP() {
        return 100000 + random.nextInt(900000);
    }

    public void storeOTP(String phone, int otp) {
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("User not found"));
        long timestamp = System.currentTimeMillis();
        String otpWithTimestamp = "OTP_" + otp + "_" + timestamp;
        user.setPassword(otpWithTimestamp);
        userRepository.save(user);
    }

    public boolean verifyOTP(String phone, int otp) {
        // Test mode: bypass OTP verification for test phone numbers
        if (testModeEnabled && getTestPhones().contains(phone) && otp == TEST_OTP) {
            User user = userRepository.findByPhone(phone).orElse(null);
            if (user != null) {
                user.setPassword("VERIFIED");
                userRepository.save(user);
                return true;
            }
        }
        
        User user = userRepository.findByPhone(phone)
                .orElse(null);
        
        if (user == null || user.getPassword() == null) {
            return false;
        }

        String password = user.getPassword();
        if (!password.startsWith("OTP_")) {
            return false;
        }

        String[] parts = password.split("_");
        if (parts.length != 3) {
            return false;
        }

        try {
            int storedOtp = Integer.parseInt(parts[1]);
            long timestamp = Long.parseLong(parts[2]);
            
            long currentTime = System.currentTimeMillis();
            if (currentTime - timestamp > OTP_EXPIRY_TIME) {
                return false;
            }

            if (storedOtp == otp) {
                user.setPassword("VERIFIED");
                userRepository.save(user);
                return true;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        return false;
    }
}

