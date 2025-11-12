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
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Fail.fail;
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
        registrationRequest.setAddress("Logos, Nigeria");

    }

    @Test
    void TestThatRegistrationRequestIsSuccessful() {
        RegistrationResponse savedUser = userService.registerUser(registrationRequest);

        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals("ezak@gmail.com", savedUser.getEmail());

        User foundUser = userRepository.findByEmail("ezak@gmail.com").orElse(null);
        assertNotNull(foundUser);
        assertEquals("Caleb", foundUser.getFirstName());
        assertEquals("Ezak", foundUser.getLastName());
        assertEquals(UserRole.CUSTOMER, foundUser.getRole());
    }

    @Test
    void TestThatRegistrationThrowsInvalidErrorWithInvalidEmail() {
        registrationRequest.setEmail("Invalid-Email");

        try{
            userService.registerUser(registrationRequest);
            fail("Expected ResponseStatusException but none was thrown");
        } catch (ResponseStatusException ex) {

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Invalid email address", ex.getReason());
        }
    }

    @Test
    void TestThatRegistrationThrowsInvalidErrorWhenEmailIsEmpty() {

        registrationRequest.setEmail("");
        try{
            userService.registerUser(registrationRequest);
            fail("Expected ResponseStatusException but none was thrown");
        }catch (ResponseStatusException ex) {

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Email is required", ex.getReason());
        }
    }

    @Test
    void TestThatRegistrationThrowsEmailAlreadyExistErrorForDuplicateEmail() {
        userService.registerUser(registrationRequest);
        try{
            userService.registerUser(registrationRequest);
            fail("Expected ResponseStatusException but none was thrown");
        }catch (ResponseStatusException ex) {

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Email already exists", ex.getReason());
        }
    }

    @Test
    void TestThatRegistrationThrowsEmptyUserNaneErrorWhenUserNameIsEmpty() {
        registrationRequest.setUserName("");

        try{
            userService.registerUser(registrationRequest);
            fail("Expected ResponseStatusException but none was thrown");
        } catch (ResponseStatusException ex) {

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Full name and username are required", ex.getReason());
        }
    }

    @Test
    void TestThatRegistrationThrowsFullNameErrorWhenFullNameIsEmpty() {
       registrationRequest.setFullName("");

       try{
           userService.registerUser(registrationRequest);
           fail("Expected ResponseStatusException but none was thrown");
       }catch (ResponseStatusException ex) {

           assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
           assertEquals("Full name and username are required", ex.getReason());
       }

    }


    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }
}