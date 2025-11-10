package com.ecommerce.services;

import com.ecommerce.DTOs.request.RegistrationRequest;
import com.ecommerce.data.model.User;
import com.ecommerce.data.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private RegistrationRequest registrationRequest;

    @BeforeEach
    void setUp() {
        registrationRequest = new RegistrationRequest();
        registrationRequest.setFullName("Caleb Ezak");
        registrationRequest.setUserName("Cazak");
        registrationRequest.setEmail("ezak@gmail.com");
        registrationRequest.setPassword("12345");
    }

    @Test
    void TestThatRegistrationRequestIsSuccessful() {
        User savedUser = userService.registerUser(registrationRequest);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }
}