package com.ecommerce.DTOs.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {

    private String fullName;
    private String userName;
    private String email;
    private String password;
    private String address;
}
