package com.ecommerce.services;

import com.ecommerce.DTOs.request.RegistrationRequest;
import com.ecommerce.DTOs.response.RegistrationResponse;
import com.ecommerce.data.model.User;
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

        if(registrationRequest.getEmail() == null || registrationRequest.getEmail().trim().isBlank()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
        }
        if(!registrationRequest.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email address");
        }

        if(userRepository.findByEmail(registrationRequest.getEmail()).isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        if(registrationRequest.getFullName() == null || registrationRequest.getFullName().trim().isBlank()
                || registrationRequest.getUserName() == null || registrationRequest.getUserName().trim().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Full name and user names are required");
        }

        if(registrationRequest.getPassword() == null || registrationRequest.getPassword().trim().isBlank()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is required");
        }

        if(registrationRequest.getAddress() == null || registrationRequest.getAddress().trim().isBlank()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Address is required");
        }

        User user = new User();
        user.setPassword(bCryptPasswordEncoder.encode(registrationRequest.getPassword()));
        user.setFirstName(registrationRequest.getFullName().trim());
        user.setLastName(registrationRequest.getFullName().trim());
        user.setEmail(registrationRequest.getEmail().trim());
        user.setAddress(registrationRequest.getAddress().trim());

        User savedUser = userRepository.save(user);

        RegistrationResponse registrationResponse = new RegistrationResponse();
        registrationResponse.setId(savedUser.getId());
        registrationResponse.setEmail(savedUser.getEmail());
        registrationResponse.setFullName(savedUser.getFirstName() + " " + savedUser.getLastName());
        registrationResponse.setMessage("Registered Successfully");

        return registrationResponse;









    }


}
