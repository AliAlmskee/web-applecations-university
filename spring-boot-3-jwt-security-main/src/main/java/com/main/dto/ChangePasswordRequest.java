package com.main.dto;

public class ChangePasswordRequest {

    private String currentPassword;
    private String newPassword;
    private String confirmationPassword;

    public ChangePasswordRequest() {
    }

    private ChangePasswordRequest(Builder builder) {
        this.currentPassword = builder.currentPassword;
        this.newPassword = builder.newPassword;
        this.confirmationPassword = builder.confirmationPassword;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String currentPassword;
        private String newPassword;
        private String confirmationPassword;

        public Builder currentPassword(String currentPassword) {
            this.currentPassword = currentPassword;
            return this;
        }

        public Builder newPassword(String newPassword) {
            this.newPassword = newPassword;
            return this;
        }

        public Builder confirmationPassword(String confirmationPassword) {
            this.confirmationPassword = confirmationPassword;
            return this;
        }

        public ChangePasswordRequest build() {
            return new ChangePasswordRequest(this);
        }
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmationPassword() {
        return confirmationPassword;
    }

    public void setConfirmationPassword(String confirmationPassword) {
        this.confirmationPassword = confirmationPassword;
    }
}
