package com.main.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.main.config.JwtService;
import com.main.dto.AuthenticationRequest;
import com.main.dto.AuthenticationResponse;
import com.main.dto.RegisterRequest;
import com.main.dto.RequestOTPRequest;
import com.main.entity.Role;
import com.main.entity.Token;
import com.main.entity.TokenType;
import com.main.entity.User;
import com.main.repository.TokenRepository;
import com.main.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Service
public class AuthenticationService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final OTPService otpService;
    private final SMSService smsService;

    public AuthenticationService(UserRepository repository, TokenRepository tokenRepository, JwtService jwtService, OTPService otpService, SMSService smsService) {
        this.repository = repository;
        this.tokenRepository = tokenRepository;
        this.jwtService = jwtService;
        this.otpService = otpService;
        this.smsService = smsService;
    }

    public void register(RegisterRequest request) {
        if (repository.findByPhone(request.getPhone()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with this phone number already exists");
        }

        int otp = otpService.generateOTP();

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .phone(request.getPhone())
                .password("")
                .role(Role.USER)
                .build();
        var savedUser = repository.save(user);

        otpService.storeOTP(request.getPhone(), otp);

        sendOTPToUser(savedUser, otp, "Your registration OTP is:");
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        if (!otpService.verifyOTP(request.getPhone(), request.getOtp())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired OTP");
        }

        var user = repository.findByPhone(request.getPhone())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void requestOTP(RequestOTPRequest request) {
        var user = repository.findByPhone(request.getPhone())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        int otp = otpService.generateOTP();
        otpService.storeOTP(request.getPhone(), otp);

        sendOTPToUser(user, otp, "Your login OTP is:");
    }

    private void sendOTPToUser(User user, int otp, String message) {
        try {
            smsService.sendSMS(user, otp, message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userPhone;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userPhone = jwtService.extractUsername(refreshToken);
        if (userPhone != null) {
            var user = this.repository.findByPhone(userPhone)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
