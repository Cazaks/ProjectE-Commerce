package com.ecommerce.DTOs.request.productResponse;

import com.ecommerce.data.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRequest {

    private String productName;
    private String productDescription;
    private Double productPrice;
    private int productQuantity;
    private Category productCategory;
    private String sellerId;
}
