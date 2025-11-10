package com.ecommerce.services;

import com.ecommerce.DTOs.request.RegistrationRequest;
import com.ecommerce.data.model.User;
import com.ecommerce.data.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals("ezak@gmail.com", savedUser.getEmail());

        User foundUser = userRepository.findByEmail("ezak@gmail.com").orElse(null);
        assertNotNull(foundUser);
        assertEquals("Caleb Ezak", foundUser.getFirstName());
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }
}