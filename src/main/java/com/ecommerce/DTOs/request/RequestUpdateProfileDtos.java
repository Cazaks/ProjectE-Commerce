package com.ecommerce.DTOs.request.userRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class RequestUpdateProfileDtos {

    private String fullName;
    private String userName;
    private String address;
}
