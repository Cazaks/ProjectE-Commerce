package com.ecommerce.services;

import com.ecommerce.DTOs.request.LoginRequest;
import com.ecommerce.DTOs.request.RegistrationRequest;
import com.ecommerce.DTOs.request.RequestUpdateProfileDtos;
import com.ecommerce.DTOs.response.userResponseDtos.LoginResponse;
import com.ecommerce.DTOs.response.RegistrationResponse;
import com.ecommerce.DTOs.response.ResponseUpdateProfileDtos;
import com.ecommerce.data.model.User;
import com.ecommerce.data.model.UserRole;
import com.ecommerce.data.repositories.UserRepository;
import org.assertj.core.api.AssertionsForClassTypes;
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
    private LoginRequest loginRequestByEmail;
    private LoginRequest loginRequestByUsername;
    private RequestUpdateProfileDtos requestUpdateProfile;
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

        loginRequestByEmail = new LoginRequest();
        loginRequestByEmail.setEmailOrUsername("ezak@gmail.com");
        loginRequestByEmail.setPassword("Valid123");

        loginRequestByUsername = new LoginRequest();
        loginRequestByUsername.setEmailOrUsername("Cazak");
        loginRequestByUsername.setPassword("Valid123");

        requestUpdateProfile = new RequestUpdateProfileDtos();
        requestUpdateProfile.setFullName("Caleb Osowoatuobim");
        requestUpdateProfile.setUserName("Cazswo");
        requestUpdateProfile.setAddress("Lawanson Road, Surulere, Lagos");
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

        assertEquals("Caleb Ezak", foundUser.get().getFullName());
        assertEquals("Cazak", foundUser.get().getUserName());
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
    void TestThatRegistrationThrowsUsernameAlreadyExistError() {

        userService.registerUser(registrationRequest);

        RegistrationRequest newRequest = new RegistrationRequest();
        newRequest.setFullName("Another User");
        newRequest.setUserName("Cazak");
        newRequest.setEmail("anotheremail@gmail.com");
        newRequest.setPassword("Valid123");
        newRequest.setAddress("Lagos, Nigeria");

        try {
            userService.registerUser(newRequest);
            fail("Expected ResponseStatusException but none was thrown");
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Username already exists", ex.getReason());
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
        registrationRequest.setEmail("uniqueemail@gmail.com");

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
        registrationRequest.setAddress("");

        try {
            userService.registerUser(registrationRequest);
            fail("Expected ResponseStatusException but none was thrown");
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Address is required", ex.getReason());
        }
    }

    @Test
    void TestThatLoginIsSuccessful() {
        userService.registerUser(registrationRequest);

        LoginResponse response = userService.loginUser(loginRequestByEmail);

        assertNotNull(response);
        assertEquals("Login successful", response.getMessage());
        assertEquals("ezak@gmail.com", response.getEmail());
    }

    @Test
    void TestThatLoginThrowsUnsuccessfulLoginErrorWhenEmailIsEmpty() {

        loginRequestByEmail.setEmailOrUsername("");

        try {
            userService.loginUser(loginRequestByEmail);
            fail("Expected ResponseStatusException but none was thrown");
        }catch (ResponseStatusException ex) {

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Email or username is required", ex.getReason());
        }
    }

    @Test
    void TestThatLoginThrowsUnsuccessfulLoginErrorWhenPasswordIsEmpty() {
        loginRequestByEmail.setPassword("");

        try{
            userService.loginUser(loginRequestByEmail);
            fail("Expected ResponseStatusException but none was thrown");
        }catch (ResponseStatusException ex) {

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Password is required", ex.getReason());
        }
    }

    @Test
    void TestThatLoginThrowsUnsuccessfulLoginErrorWhenPasswordIsIncorrect() {

        userService.registerUser(registrationRequest);
        loginRequestByEmail.setPassword("IncorrectPassword");

        try{
            userService.loginUser(loginRequestByEmail);
            fail("Expected ResponseStatusException but none was thrown");
        }catch (ResponseStatusException ex) {

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Incorrect password", ex.getReason());
        }
    }

    @Test
    void TestThatLoginThrowsUnsuccessfulLoginErrorWhenUsernameIsEmpty() {

        loginRequestByUsername.setEmailOrUsername("");

        try {
            userService.loginUser(loginRequestByUsername);
            fail("Expected ResponseStatusException but none was thrown");
        }catch (ResponseStatusException ex) {

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Email or username is required", ex.getReason());
        }
    }

    @Test
    void TestThatLoginThrowsUserNotFoundErrorWhenUsernameDoesNotExist() {

        loginRequestByUsername.setEmailOrUsername("UnknownUser123");

        try {
            userService.loginUser(loginRequestByUsername);
            fail("Expected ResponseStatusException but none was thrown");
        } catch (ResponseStatusException ex) {

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Email or username is not valid, enter valid credentials", ex.getReason());
        }
    }

    @Test
    void TestLoginThrowInvalidEmailErrorWhenEmailIsNotValid() {

        loginRequestByEmail.setEmailOrUsername("invalidEmail");
        try {
            userService.loginUser(loginRequestByEmail);
            fail("Expected ResponseStatusException but none was thrown");
        }catch (ResponseStatusException ex) {

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Email or username is not valid, enter valid credentials", ex.getReason());
        }
    }

    @Test
    void TestThatLoginThrowsInvalidUserNameErrorWhenUserNameIsNotValid() {

        loginRequestByUsername.setEmailOrUsername("ab!");
        try {
            userService.loginUser(loginRequestByUsername);
            fail("Expected ResponseStatusException but none was thrown");
        }catch (ResponseStatusException ex) {

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Email or username is not valid, enter valid credentials", ex.getReason());
        }
    }

    @Test
    void TestThatPromoteToSellerIsSuccessful() {

        RegistrationResponse response = userService.registerUser(registrationRequest);


        User user = userRepository.findById(response.getId()).get();
        assertEquals(UserRole.CUSTOMER, user.getRole());


        userService.promoteToSeller(response.getId());

        User updatedUser = userRepository.findById(response.getId()).get();
        assertEquals(UserRole.SELLER, updatedUser.getRole());
    }

    @Test
    void TestThatPromoteToSellerThrowsErrorWhenUserIdIsNull() {
        try {
            userService.promoteToSeller(null);
            fail("Expected ResponseStatusException but none was thrown");
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("User ID is required", ex.getReason());
        }
    }

    @Test
    void TestThatPromoteToSellerThrowsErrorWhenUserIdIsBlank() {
        try {
            userService.promoteToSeller("   ");
            fail("Expected ResponseStatusException but none was thrown");
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("User ID is required", ex.getReason());
        }
    }

    @Test
    void TestThatPromoteToSellerThrowsErrorWhenUserIsNotFound() {

        try {
            userService.promoteToSeller("unknown-id-123");
            fail("Expected ResponseStatusException but none was thrown");
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
            assertEquals("User not found", ex.getReason());
        }
    }

    @Test
    void TestThatPromoteToSellerThrowsErrorWhenUserIsAlreadySeller() {

        RegistrationResponse response = userService.registerUser(registrationRequest);

        User user = userRepository.findById(response.getId()).get();
        user.setRole(UserRole.SELLER);
        userRepository.save(user);

        try {
            userService.promoteToSeller(response.getId());
            fail("Expected ResponseStatusException but none was thrown");
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("User is already a seller", ex.getReason());
        }
    }

    @Test
    void TestThatUpdatedUserProfileIsSuccessful() {
        RegistrationResponse registerResponse = userService.registerUser(registrationRequest);

        ResponseUpdateProfileDtos responseUpdate = userService.updateProfile(
                registerResponse.getId(),
                requestUpdateProfile
        );

        assertNotNull(responseUpdate);
        assertEquals(registerResponse.getId(), responseUpdate.getUserId());
        assertEquals("Caleb Osowoatuobim", responseUpdate.getFullName());
        assertEquals("Cazswo", responseUpdate.getUserName());
        assertEquals("Lawanson Road, Surulere, Lagos", responseUpdate.getAddress());
        assertEquals("profile update successful", responseUpdate.getMessage());

    }

    @Test
    void TestThatUpdateProfileThrowsErrorWhenUserUserIdIsBlank() {

        try {
            userService.updateProfile("", requestUpdateProfile);
            fail("Expected ResponseStatusException but none was thrown");
        }catch (ResponseStatusException ex){

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("User ID must not be empty", ex.getReason());
        }
    }

    @Test
    void TestThatUpdateProfileThrowsErrorWhenUserNameIsEmpty() {

        requestUpdateProfile.setFullName("");
        try {
            userService.updateProfile("", requestUpdateProfile);
            fail("Expected ResponseStatusException but none was thrown");
        }catch (ResponseStatusException ex){

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Full name is required", ex.getReason());
        }
    }

    @Test
    void TestThatUpdateThrowsErrorWhenUserIsNotFound() {

        try {
            userService.updateProfile("123-id-invalid", requestUpdateProfile);
            fail("Expected ResponseStatusException but none was thrown");
        } catch (ResponseStatusException ex) {

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("User not found", ex.getReason());
        }
    }

    @Test
    void TestThatUpdateProfileThrowsErrorWhenUserNameIsBlank() {
        requestUpdateProfile.setUserName(" ");

        try {
            userService.updateProfile("123-id-invalid", requestUpdateProfile);
            fail("Expected ResponseStatusException but none was thrown");
        }catch (ResponseStatusException ex){

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Username is required", ex.getReason());
        }
    }

    @Test
    void TestThatUpdateProfileThrowsErrorWhenUserWhenAddressIsBlank() {
        requestUpdateProfile.setAddress(null);
        try {
            userService.updateProfile("123-id-invalid", requestUpdateProfile);
            fail("Expected ResponseStatusException but none was thrown");
        }catch (ResponseStatusException ex){

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Address is required", ex.getReason());
        }
        }

    }



