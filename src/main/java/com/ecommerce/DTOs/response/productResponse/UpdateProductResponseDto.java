package com.ecommerce.DTOs.response.productResponse;

import com.ecommerce.data.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductResponseDto {

    private String productId;
    private String productName;
    private int productQuantity;
    private String productDescription;
    private Double productPrice;
    private Category productCategory;
    private String message;
}
