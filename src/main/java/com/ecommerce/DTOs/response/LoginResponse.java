package com.ecommerce.DTOs.response.userResponseDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String id;
    private String email;
    private String username;
    private String fullName;
    private String message;

}
