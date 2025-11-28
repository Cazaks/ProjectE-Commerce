package com.ecommerce.services;

import com.ecommerce.DTOs.request.LoginRequest;
import com.ecommerce.DTOs.request.RegistrationRequest;
import com.ecommerce.DTOs.response.LoginResponse;
import com.ecommerce.DTOs.response.RegistrationResponse;
import com.ecommerce.DTOs.response.ResponseUpdateProfileDtos;
import com.ecommerce.data.model.User;
import com.ecommerce.data.model.UserRole;
import com.ecommerce.data.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public RegistrationResponse registerUser(RegistrationRequest registrationRequest) {

        if (registrationRequest.getEmail() == null || registrationRequest.getEmail().trim().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
        }

        if (registrationRequest.getFullName() == null || registrationRequest.getFullName().trim().isBlank()
                || registrationRequest.getUserName() == null || registrationRequest.getUserName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Full name and username are required");
        }

        if (registrationRequest.getAddress() == null || registrationRequest.getAddress().trim().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Address is required");
        }

        if (registrationRequest.getPassword() == null || registrationRequest.getPassword().trim().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is required");
        }


        if (!registrationRequest.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email address");
        }

        if (userRepository.findByEmail(registrationRequest.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        if(userRepository.findByUserName(registrationRequest.getUserName()).isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }


        if (!registrationRequest.getPassword().matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Password is too weak. Use at least 8 characters including letters and numbers.");
        }

        User user = new User();
        user.setPassword(bCryptPasswordEncoder.encode(registrationRequest.getPassword().trim()));
        user.setRole(UserRole.CUSTOMER);
        user.setEmail(registrationRequest.getEmail().trim());
        user.setAddress(registrationRequest.getAddress().trim());
        user.setFullName(registrationRequest.getFullName().trim());
        user.setUserName(registrationRequest.getUserName().trim());

        User savedUser = userRepository.save(user);


        RegistrationResponse response = new RegistrationResponse();
        response.setId(savedUser.getId());
        response.setEmail(savedUser.getEmail());
        response.setFullName(savedUser.getFullName());
        response.setMessage("Registered Successfully");

        return response;
    }

    @Override
    public LoginResponse loginUser(LoginRequest loginRequest) {

        if (loginRequest.getEmailOrUsername() == null || loginRequest.getEmailOrUsername().trim().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email or username is required");
        }

        if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is required");
        }

        String identifier = loginRequest.getEmailOrUsername();
        Optional<User> user;

        if (identifier.contains("@")) {

            if (!identifier.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Email or username is not valid, enter valid credentials");
            }

            user = userRepository.findByEmail(identifier);

            if (!user.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Email or username is not valid, enter valid credentials");
            }

        } else {
            if (!identifier.matches("^[A-Za-z0-9]{3,}$")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Email or username is not valid, enter valid credentials");
            }

            user = userRepository.findByUserName(identifier);

            if (!user.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Email or username is not valid, enter valid credentials");
            }
        }

        if (!bCryptPasswordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect password");
        }

        LoginResponse response = new LoginResponse();
        response.setId(user.get().getId().trim());
        response.setEmail(user.get().getEmail().trim());
        response.setUsername(user.get().getUserName().trim());
        response.setFullName(user.get().getFullName().trim());
        response.setMessage("Login successful");

        return response;
    }

    @Override
    public ResponseUpdateProfileDtos updateProfile(String id, ResponseUpdateProfileDtos responseUpdateProfileDtos){
        return null;
    }


    @Override
    public void promoteToSeller(String userId) {

        if (userId == null || userId.trim().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID is required");
        }

        Optional<User> optionalUser = userRepository.findById(userId);

        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        User user = optionalUser.get();

        if (user.getRole() == UserRole.SELLER) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is already a seller");
        }

        user.setRole(UserRole.SELLER);

        userRepository.save(user);
    }

}
