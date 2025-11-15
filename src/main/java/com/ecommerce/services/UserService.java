package com.ecommerce.services;

import com.ecommerce.DTOs.request.LoginRequest;
import com.ecommerce.DTOs.request.RegistrationRequest;
import com.ecommerce.DTOs.response.LoginResponse;
import com.ecommerce.DTOs.response.RegistrationResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    RegistrationResponse registerUser(RegistrationRequest registrationRequest);

    LoginResponse loginUser(LoginRequest loginRequest);
}