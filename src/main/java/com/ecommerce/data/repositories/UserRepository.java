package com.ecommerce.data.repositories;

import com.ecommerce.DTOs.response.LoginResponse;
import com.ecommerce.data.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User,String> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUserName(String userName);


//    LoginResponse findByEmail(String email);
}
