package com.main.dto;

import com.main.entity.Role;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RegisterRequest {
    @NotEmpty
    private String firstname;
    private String lastname;
    @NotEmpty
    private String phone;
    private String fcmToken;
    private Role role ;
}
