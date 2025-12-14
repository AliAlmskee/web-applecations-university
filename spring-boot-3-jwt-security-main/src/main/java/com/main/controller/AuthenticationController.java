package com.main.controller;

import com.main.dto.AuthenticationRequest;
import com.main.dto.AuthenticationResponse;
import com.main.dto.RegisterRequest;
import com.main.dto.RequestOTPRequest;
import com.main.entity.Role;
import com.main.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService service;

    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request) {
        request.setRole(Role.USER);
        service.register(request);
        return ResponseEntity.ok().body(java.util.Map.of("message", "OTP sent successfully to " + request.getPhone()));
    }

    @PostMapping("/request-otp")
    public ResponseEntity<?> requestOTP(
            @Valid @RequestBody RequestOTPRequest request) {
        service.requestOTP(request);
        return ResponseEntity.ok().body(java.util.Map.of("message", "OTP sent successfully to " + request.getPhone()));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        service.refreshToken(request, response);
    }


}
