package com.ecommerce.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    private Long productId;
    private String name;
    private String description;
    private Double price;
    private Category category;
    private int stock;
    private User seller;

}
