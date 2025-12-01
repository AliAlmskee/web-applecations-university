package com.main.dto;

import jakarta.validation.constraints.NotEmpty;

public class RegisterRequest {

    @NotEmpty
    private String firstname;

    private String lastname;

    @NotEmpty
    private String phone;

    public RegisterRequest() {
    }

    public RegisterRequest(String firstname, String lastname, String phone) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
    }

    private RegisterRequest(Builder builder) {
        this.firstname = builder.firstname;
        this.lastname = builder.lastname;
        this.phone = builder.phone;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String firstname;
        private String lastname;
        private String phone;

        public Builder firstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public Builder lastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public RegisterRequest build() {
            return new RegisterRequest(this);
        }
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
