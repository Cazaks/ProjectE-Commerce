package com.ecommerce.DTOs.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ResponseUpdateProfileDtos {

    private String id;
    private String fullName;
    private String userName;
    private String address;
    private String email;
    private LocalDateTime updateDate;
    private String message;

}
