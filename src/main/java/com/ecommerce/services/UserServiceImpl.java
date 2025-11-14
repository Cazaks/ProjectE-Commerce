package com.ecommerce.services;

import com.ecommerce.DTOs.request.RegistrationRequest;
import com.ecommerce.DTOs.response.RegistrationResponse;
import com.ecommerce.data.model.User;
import com.ecommerce.data.model.UserRole;
import com.ecommerce.data.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

        if (!registrationRequest.getPassword().matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Password is too weak. Use at least 8 characters including letters and numbers.");
        }


        User user = new User();
        user.setPassword(bCryptPasswordEncoder.encode(registrationRequest.getPassword()));
        user.setRole(UserRole.CUSTOMER);


        String[] names = registrationRequest.getFullName().trim().split(" ", 2);
        user.setFirstName(names[0]);
        user.setLastName(names.length > 1 ? names[1] : "");

        user.setEmail(registrationRequest.getEmail().trim());
        user.setAddress(registrationRequest.getAddress().trim());

        User savedUser = userRepository.save(user);


        RegistrationResponse response = new RegistrationResponse();
        response.setId(savedUser.getId());
        response.setEmail(savedUser.getEmail());
        response.setFullName(savedUser.getFirstName() + " " + savedUser.getLastName());
        response.setMessage("Registered Successfully");

        return response;
    }
}
