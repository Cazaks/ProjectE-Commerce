package com.ecommerce.services;


import com.ecommerce.DTOs.request.productRequest.CreateProductRequest;
import com.ecommerce.DTOs.response.productResponse.CreateProductResponse;
import com.ecommerce.data.model.Category;
import com.ecommerce.data.model.Product;
import com.ecommerce.data.model.User;
import com.ecommerce.data.repositories.ProductRepository;
import com.ecommerce.data.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Document(collection = "products")
@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {


    private final UserRepository userRepository;
    private ProductRepository productRepository;

    @Override
    public CreateProductResponse createProduct(CreateProductRequest createRequest){


       if(createRequest.getProductName() == null || createRequest.getProductName().trim().isBlank()){
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product name cannot be empty");
      }

       if(createRequest.getProductDescription() == null || createRequest.getProductDescription().trim().isBlank()){
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product description cannot be empty");
       }


        if(createRequest.getProductQuantity() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product quantity is required");
        }

       if(createRequest.getProductQuantity() <= 0){
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product quantity must be greater than zero");
       }

       if(createRequest.getProductPrice() == null){
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product price is required");
       }

       if(createRequest.getProductPrice() <= 0){
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product price must be greater than zero");
       }

       if(createRequest.getProductCategory() == null){
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product category is required");
       }

       if(createRequest.getSellerId() == null || createRequest.getSellerId().trim().isBlank()){
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seller id is required");
       }

        Optional<User> seller = userRepository.findById(createRequest.getSellerId());
       if(!seller.isPresent()){
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

        CreateProductResponse createResponse = mapToProductResponse(productCreated);

        return createResponse;

    }

    private static CreateProductResponse mapToProductResponse(Product productCreated) {
        CreateProductResponse createResponse = new CreateProductResponse();
        createResponse.setProductId(productCreated.getProductId());
        createResponse.setProductName(productCreated.getProductName());
        createResponse.setProductDescription(productCreated.getProductDescription());
        createResponse.setProductPrice(productCreated.getProductPrice());
        createResponse.setProductQuantity(productCreated.getProductQuantity());
        createResponse.setProductCategory(productCreated.getProductCategory());
        createResponse.setSellerId(productCreated.getSellerId());
        return createResponse;
    }
}
