package com.ecommerce.DTOs.response.productResponse;

import com.ecommerce.data.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductResponse {

    private String productId;
    private String productName;
    private String productDescription;
    private Double productPrice;
    private Integer productQuantity;
    private Category productCategory;
    private String sellerId;

}
