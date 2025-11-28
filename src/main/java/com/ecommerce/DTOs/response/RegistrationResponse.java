package com.ecommerce.DTOs.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class RegistrationResponse {

    private String id;
    private String fullName;
    private String email;
    private String message;
}
