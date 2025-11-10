package com.ecommerce.services;

import com.ecommerce.DTOs.request.RegistrationRequest;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    void registerUser(RegistrationRequest registrationRequest);
}