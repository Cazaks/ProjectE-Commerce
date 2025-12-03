package com.ecommerce.services;


import com.ecommerce.DTOs.request.productRequest.CreateProductRequest;
import com.ecommerce.DTOs.response.productResponse.CreateProductResponse;
import com.ecommerce.data.model.Product;
import com.ecommerce.data.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Document(collection = "products")
@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {


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
