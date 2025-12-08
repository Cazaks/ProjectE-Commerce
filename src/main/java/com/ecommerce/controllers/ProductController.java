package com.ecommerce.controllers;


import com.ecommerce.DTOs.request.productRequest.CreateProductRequest;
import com.ecommerce.DTOs.request.productRequest.UpdateProductRequestDto;
import com.ecommerce.DTOs.response.productResponse.CreateProductResponse;
import com.ecommerce.DTOs.response.productResponse.UpdateProductResponseDto;
import com.ecommerce.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/{productId}")
    public ResponseEntity<UpdateProductResponseDto> updateProduct(@PathVariable String productId,
            @RequestBody UpdateProductRequestDto updateRequestDto) {

        updateRequestDto.setProductId(productId);

        UpdateProductResponseDto updateResponse = productService.updateProduct(updateRequestDto);
        return ResponseEntity.ok(updateResponse);
    }

}
