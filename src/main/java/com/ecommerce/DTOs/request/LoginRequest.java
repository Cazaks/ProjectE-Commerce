package com.ecommerce.DTOs.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class LoginRequest {

    @NotBlank(message = "Email or username required")
    @Email(message = "Enter a valid email or user name")
    private String emailOrUsername;

    @NotBlank(message = "Enter you password")
    private String password;
}


