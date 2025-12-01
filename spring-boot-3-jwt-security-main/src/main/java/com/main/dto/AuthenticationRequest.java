package com.main.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class AuthenticationRequest {

    @NotEmpty
    private String phone;

    @NotNull
    private Integer otp;

    public AuthenticationRequest() {
    }

    public AuthenticationRequest(String phone, Integer otp) {
        this.phone = phone;
        this.otp = otp;
    }

    private AuthenticationRequest(Builder builder) {
        this.phone = builder.phone;
        this.otp = builder.otp;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String phone;
        private Integer otp;

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder otp(Integer otp) {
            this.otp = otp;
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
}
