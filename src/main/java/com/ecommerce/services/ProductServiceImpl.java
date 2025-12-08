package com.ecommerce.services;

import com.ecommerce.DTOs.request.productRequest.CreateProductRequest;
import com.ecommerce.DTOs.request.productRequest.UpdateProductRequestDto;
import com.ecommerce.DTOs.response.productResponse.CreateProductResponse;
import com.ecommerce.DTOs.response.productResponse.UpdateProductResponseDto;
import com.ecommerce.data.model.Product;
import com.ecommerce.data.model.User;
import com.ecommerce.data.repositories.ProductRepository;
import com.ecommerce.data.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public CreateProductResponse createProduct(CreateProductRequest createRequest) {

        if (createRequest.getProductName() == null || createRequest.getProductName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product name cannot be empty");
        }

        if (createRequest.getProductDescription() == null || createRequest.getProductDescription().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product description cannot be empty");
        }

        if (createRequest.getProductQuantity() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product quantity is required");
        }

        if (createRequest.getProductQuantity() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product quantity must be greater than zero");
        }

        if (createRequest.getProductPrice() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product price is required");
        }

        if (createRequest.getProductPrice() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product price must be greater than zero");
        }

        if (createRequest.getProductCategory() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product category is required");
        }

        if (createRequest.getSellerId() == null || createRequest.getSellerId().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seller id is required");
        }

        Optional<User> seller = userRepository.findById(createRequest.getSellerId());
        if (!seller.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seller not found");
        }


        Product productCreated = new Product();
        productCreated.setProductName(createRequest.getProductName());
        productCreated.setProductDescription(createRequest.getProductDescription());
        productCreated.setProductPrice(createRequest.getProductPrice());
        productCreated.setProductQuantity(createRequest.getProductQuantity());
        productCreated.setProductCategory(createRequest.getProductCategory());
        productCreated.setSellerId(createRequest.getSellerId());

        productCreated = productRepository.save(productCreated);

        return mapToProductResponse(productCreated);
    }

    private static CreateProductResponse mapToProductResponse(Product productCreated) {
        CreateProductResponse response = new CreateProductResponse();
        response.setProductId(productCreated.getProductId());
        response.setProductName(productCreated.getProductName());
        response.setProductDescription(productCreated.getProductDescription());
        response.setProductPrice(productCreated.getProductPrice());
        response.setProductQuantity(productCreated.getProductQuantity());
        response.setProductCategory(productCreated.getProductCategory());
        response.setSellerId(productCreated.getSellerId());
        return response;
    }

    @Override
    public UpdateProductResponseDto updateProduct(UpdateProductRequestDto updateProductRequest) {

        if(updateProductRequest.getProductId() == null || updateProductRequest.getProductId().trim().isBlank()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product id not found");
        }

        if(updateProductRequest.getProductName() == null || updateProductRequest.getProductName().trim().isBlank()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product name not found");
        }

        if(updateProductRequest.getProductDescription() == null || updateProductRequest.getProductDescription().trim().isBlank()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product description not found");
        }

        if(updateProductRequest.getProductPrice() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product price not found");
        }

        if(updateProductRequest.getProductPrice() <= 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product price must be greater than 0");
        }

        if(updateProductRequest.getProductQuantity() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product quantity is required");
        }

        if(updateProductRequest.getProductQuantity() <= 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product quantity must be greater than 0");
        }

        if(updateProductRequest.getProductCategory() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product category is required");
        }


        Product updateProduct = productRepository.findById(updateProductRequest.getProductId()).orElse(null);

        if(updateProduct == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product not found");
        }


        updateProduct.setProductName(updateProductRequest.getProductName());
        updateProduct.setProductQuantity(updateProductRequest.getProductQuantity());
        updateProduct.setProductDescription(updateProductRequest.getProductDescription());
        updateProduct.setProductPrice(updateProductRequest.getProductPrice());
        updateProduct.setProductCategory(updateProductRequest.getProductCategory());

        Product savedProduct = productRepository.save(updateProduct);

        return getUpdateProductResponseDto(savedProduct);

    }

    private static UpdateProductResponseDto getUpdateProductResponseDto(Product savedProduct) {
        UpdateProductResponseDto updateResponse = new UpdateProductResponseDto();
        updateResponse.setProductId(savedProduct.getProductId());
        updateResponse.setProductName(savedProduct.getProductName());
        updateResponse.setProductQuantity(savedProduct.getProductQuantity());
        updateResponse.setProductDescription(savedProduct.getProductDescription());
        updateResponse.setProductPrice(savedProduct.getProductPrice());
        updateResponse.setProductCategory(savedProduct.getProductCategory());
        updateResponse.setMessage("Product Updated successful");
        return updateResponse;
    }
}
