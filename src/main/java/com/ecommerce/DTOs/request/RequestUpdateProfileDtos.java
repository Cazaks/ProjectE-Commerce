package com.ecommerce.DTOs.request;

import com.ecommerce.data.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class RequestUpdateProfileDtos {

    private String fullName;
    private String userName;
    private String adddress;
}
