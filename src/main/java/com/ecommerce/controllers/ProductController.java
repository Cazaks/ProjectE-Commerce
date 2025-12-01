package com.ecommerce.controllers;


import com.ecommerce.DTOs.request.productResponse.CreateProductRequest;
import com.ecommerce.DTOs.response.productResponse.CreateProductResponse;
import com.ecommerce.data.model.Product;
import com.ecommerce.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")

public class ProductController {
    private final ProductService productService;

    @PostMapping("/createProduct")
    public ResponseEntity<CreateProductResponse> createProduct(@RequestBody CreateProductRequest createProductRequest){
        CreateProductResponse createResponse = productService.createProduct(createProductRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createResponse);
    }

}
