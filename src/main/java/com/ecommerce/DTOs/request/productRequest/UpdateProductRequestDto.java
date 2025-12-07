package com.ecommerce.DTOs.request.productRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductRequestDto {

    private String productId;
    private String productName;
    private Double productQuantity;
    private String productDescription;
    private String productPrice;
    private String productCategory;
}
