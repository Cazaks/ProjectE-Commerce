package com.ecommerce.services;

import com.ecommerce.DTOs.request.RegistrationRequest;
import com.ecommerce.DTOs.response.RegistrationResponse;
import com.ecommerce.data.model.User;
import com.ecommerce.data.model.UserRole;
import com.ecommerce.data.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private RegistrationRequest registrationRequest;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {

        userRepository.deleteAll();

        registrationRequest = new RegistrationRequest();
        registrationRequest.setFullName("Caleb Ezak");
        registrationRequest.setUserName("Cazak");
        registrationRequest.setEmail("ezak@gmail.com");
        registrationRequest.setPassword("Valid123");
        registrationRequest.setAddress("Lagos, Nigeria");
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void TestThatRegistrationRequestIsSuccessful() {
        RegistrationResponse response = userService.registerUser(registrationRequest);

        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals("ezak@gmail.com", response.getEmail());

        Optional<User> foundUser = userRepository.findByEmail("ezak@gmail.com");
        if (!foundUser.isPresent()) {
            fail("User should exist after registration");
        }

        assertEquals("Caleb", foundUser.get().getFirstName());
        assertEquals("Ezak", foundUser.get().getLastName());
        assertEquals(UserRole.CUSTOMER, foundUser.get().getRole());
        assertTrue(bCryptPasswordEncoder.matches("Valid123", foundUser.get().getPassword()));
    }

    @Test
    void TestThatRegistrationThrowsInvalidEmailError() {
        registrationRequest.setEmail("Invalid-Email");

        try {
            userService.registerUser(registrationRequest);
            fail("Expected ResponseStatusException but none was thrown");
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Invalid email address", ex.getReason());
        }
    }

    @Test
    void TestThatRegistrationThrowsEmptyEmailError() {
        registrationRequest.setEmail("");

        try {
            userService.registerUser(registrationRequest);
            fail("Expected ResponseStatusException but none was thrown");
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Email is required", ex.getReason());
        }
    }

    @Test
    void TestThatRegistrationThrowsEmailAlreadyExistError() {
        userService.registerUser(registrationRequest);

        try {
            userService.registerUser(registrationRequest);
            fail("Expected ResponseStatusException but none was thrown");
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Email already exists", ex.getReason());
        }
    }

    @Test
    void TestThatRegistrationThrowsEmptyUserNameError() {
        registrationRequest.setUserName("");

        try {
            userService.registerUser(registrationRequest);
            fail("Expected ResponseStatusException but none was thrown");
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Full name and username are required", ex.getReason());
        }
    }

    @Test
    void TestThatRegistrationThrowsFullNameError() {
        registrationRequest.setFullName("");

        try {
            userService.registerUser(registrationRequest);
            fail("Expected ResponseStatusException but none was thrown");
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Full name and username are required", ex.getReason());
        }
    }

    @Test
    void TestThatRegistrationThrowsEmptyPasswordError() {
        registrationRequest.setPassword("");

        try {
            userService.registerUser(registrationRequest);
            fail("Expected ResponseStatusException but none was thrown");
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Password is required", ex.getReason());
        }
    }

    @Test
    void TestThatPasswordThrowsWeakPasswordError() {
        registrationRequest.setPassword("12345");

        try {
            userService.registerUser(registrationRequest);
            fail("Expected ResponseStatusException but none was thrown");
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Password is too weak. Use at least 8 characters including letters and numbers.",
                    ex.getReason());
        }
    }

    @Test
    void TestThatRegistrationThrowsEmptyAddressError() {
        registrationRequest.setPassword("Valid123");
        registrationRequest.setAddress("");

        try {
            userService.registerUser(registrationRequest);
            fail("Expected ResponseStatusException but none was thrown");
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Address is required", ex.getReason());
        }
    }
}
