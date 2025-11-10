package com.ecommerce.services;

import com.ecommerce.DTOs.request.RegistrationRequest;
import com.ecommerce.data.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    User registerUser(RegistrationRequest registrationRequest);
}