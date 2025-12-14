package com.main.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class AuthenticationRequest {

    @NotEmpty
    private String phone;

    @NotNull
    private Integer otp;

    private String fcmToken;

    public AuthenticationRequest() {
    }

    public AuthenticationRequest(String phone, Integer otp, String fcmToken) {
        this.phone = phone;
        this.otp = otp;
        this.fcmToken = fcmToken;
    }

    private AuthenticationRequest(Builder builder) {
        this.phone = builder.phone;
        this.otp = builder.otp;
        this.fcmToken = builder.fcmToken;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String phone;
        private Integer otp;
        private String fcmToken;

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder otp(Integer otp) {
            this.otp = otp;
            return this;
        }

        public Builder fcmToken(String fcmToken) {
            this.fcmToken = fcmToken;
            return this;
        }

        public AuthenticationRequest build() {
            return new AuthenticationRequest(this);
        }
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
