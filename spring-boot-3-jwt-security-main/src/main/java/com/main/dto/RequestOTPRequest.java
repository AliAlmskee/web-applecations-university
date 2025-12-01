package com.main.dto;

import jakarta.validation.constraints.NotEmpty;

public class RequestOTPRequest {

    @NotEmpty
    private String phone;

    public RequestOTPRequest() {
    }

    public RequestOTPRequest(String phone) {
        this.phone = phone;
    }

    private RequestOTPRequest(Builder builder) {
        this.phone = builder.phone;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String phone;

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public RequestOTPRequest build() {
            return new RequestOTPRequest(this);
        }
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

