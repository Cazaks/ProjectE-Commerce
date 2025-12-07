package com.ecommerce.services;


import com.ecommerce.DTOs.request.productRequest.CreateProductRequest;
import com.ecommerce.DTOs.response.productResponse.CreateProductResponse;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {
    CreateProductResponse createProduct(CreateProductRequest createRequest);
}
