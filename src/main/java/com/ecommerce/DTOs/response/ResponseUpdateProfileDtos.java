package com.ecommerce.DTOs.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ResponseUpdateProfileDtos {

    private String userId;
    private String fullName;
    private String userName;
    private String address;
    private String email;
    private Date updateDate;
    private String message;

}
