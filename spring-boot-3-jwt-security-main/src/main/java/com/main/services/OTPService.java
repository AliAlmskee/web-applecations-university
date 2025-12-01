package com.main.services;

import com.main.entity.User;
import com.main.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OTPService {

    private static final long OTP_EXPIRY_TIME = 5 * 60 * 1000;
    private static final Random random = new Random();
    private final UserRepository userRepository;

    public OTPService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

