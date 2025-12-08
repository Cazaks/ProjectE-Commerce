package com.ecommerce.services;

import com.ecommerce.DTOs.request.productRequest.CreateProductRequest;
import com.ecommerce.DTOs.request.productRequest.UpdateProductRequestDto;
import com.ecommerce.DTOs.response.productResponse.CreateProductResponse;
import com.ecommerce.DTOs.response.productResponse.UpdateProductResponseDto;
import com.ecommerce.data.model.Category;
import com.ecommerce.data.model.Product;
import com.ecommerce.data.model.User;
import com.ecommerce.data.repositories.ProductRepository;
import com.ecommerce.data.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.AssertionsForClassTypes.fail;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ProductServiceImplTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductService productService;

    private CreateProductRequest createRequest;
    private UpdateProductRequestDto updateRequest;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();

        User seller = new User();
        seller.setUserId("SELLER2a4");
        seller.setFullName("Kelvin Ifeanyi");
        userRepository.save(seller);

        createRequest = new CreateProductRequest();
        createRequest.setProductName("iPhone 17 Promax");
        createRequest.setProductDescription("The phone is faster than speed of light");
        createRequest.setProductPrice(1_2000_000.00);
        createRequest.setProductQuantity(15);
        createRequest.setProductCategory(Category.ELECTRONICS);
        createRequest.setSellerId("SELLER2a4");

        updateRequest = new UpdateProductRequestDto();
        updateRequest.setProductId("Checking_Id");
        updateRequest.setProductName("iPhone 17 ultra");
        updateRequest.setProductQuantity(7);
        updateRequest.setProductDescription("This phone can snap your inner intestine");
        updateRequest.setProductPrice(1_550_000.00);
        updateRequest.setProductCategory(Category.ELECTRONICS);

    }

    @Test
    void TestThatProductIsCreatedSuccessfully() {

        CreateProductResponse productResponse = productService.createProduct(createRequest);

        assertNotNull(productResponse.getProductId(), "Product ID cannot be null");
        assertEquals(createRequest.getProductName(), productResponse.getProductName());
        assertEquals(createRequest.getProductDescription(), productResponse.getProductDescription());
        assertEquals(createRequest.getProductPrice(), productResponse.getProductPrice());
        assertEquals(createRequest.getProductQuantity(), productResponse.getProductQuantity());
        assertEquals(createRequest.getProductCategory(), productResponse.getProductCategory());
        assertEquals(createRequest.getSellerId(), productResponse.getSellerId());

        Product saved = productRepository.findById(productResponse.getProductId()).orElse(null);
        assertNotNull(saved, "Product must exist in DB");

        assertEquals("SELLER2a4", saved.getSellerId());
        assertEquals("iPhone 17 Promax",  saved.getProductName());

    }
    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }


    @Test
    void TestThatProductCreationThrowInvalidErrorWhenProductNameIsEmpty() {
        createRequest.setProductName("");

        try {
            productService.createProduct(createRequest);
            fail("Product name expected");
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Product name cannot be empty", ex.getReason());
        }
    }

    @Test
    void TestThatProductCreationThrowsInvalidErrorWhenProductDescriptionIsEmpty() {
        createRequest.setProductDescription("");
        try {
            productService.createProduct(createRequest);
            fail("Product description expected");
        }catch (ResponseStatusException ex) {

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Product description cannot be empty", ex.getReason());
        }
    }

    @Test
    void TestThatCreateProductThrowsInvalidErrorWhenProductQuantityIsZero() {
        createRequest.setProductQuantity(0);

        try {
            productService.createProduct(createRequest);
            fail("Product quantity expected");
        }catch (ResponseStatusException ex) {

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Product quantity must be greater than zero", ex.getReason());
        }
    }


    @Test
    void TestThatCreateProductThrowsInvalidErrorWhenProductQuantityIsNegative() {
        createRequest.setProductQuantity(-1);
        try {
            productService.createProduct(createRequest);
            fail("Expect product quantity to be greater than 0");
        }catch (ResponseStatusException ex) {

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Product quantity must be greater than zero", ex.getReason());
        }
    }

    @Test
    void TestThatCreateProductThrowsInvalidErrorWhenProductQuantityIsNull() {
        createRequest.setProductQuantity(null);

        try {
            productService.createProduct(createRequest);
            fail("Expected product quantity");
        }catch (ResponseStatusException ex) {

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Product quantity is required", ex.getReason());
        }
    }

    @Test
    void TestThatCreateProductThrowsInvalidErrorWhenProductPriceIsNull() {
        createRequest.setProductPrice(null);

        try {
            productService.createProduct(createRequest);
            fail("Expected product price");
        }catch (ResponseStatusException ex) {

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Product price is required", ex.getReason());
        }
    }

    @Test
    void TestThatCreateProductThrowsInvalidErrorWhenProductPriceIsZero() {
        createRequest.setProductPrice(00.00);

        try {
            productService.createProduct(createRequest);
            fail("Expected product price greater than 0");
        }catch (ResponseStatusException ex) {

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Product price must be greater than zero", ex.getReason());
        }
    }

    @Test
    void TestThatCreateProductThrowsInvalidErrorWhenProductPriceIsNegative() {
        createRequest.setProductPrice(-50.00);

        try {
            productService.createProduct(createRequest);
            fail("Expected product price");
        }catch (ResponseStatusException ex) {

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Product price must be greater than zero", ex.getReason());
        }
    }

    @Test
    void TestThatCreateProductIsSuccessfullyCreatedWhenProductPriceIsPositive() {
        createRequest.setProductPrice(1_2000_000.00);

        productService.createProduct(createRequest);
    }

    @Test
    void TestThatCreateProductThrowsInvalidErrorWhenProductCategoryIsNull() {
        createRequest.setProductCategory(null);

        try {
            productService.createProduct(createRequest);
            fail("Expected product category");
        }catch (ResponseStatusException ex) {

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Product category is required", ex.getReason());
        }
    }

    @Test
    void TestThatCreateProductThrowsInvalidErrorWhenSellerIdIsNull() {
        createRequest.setSellerId(null);

        try {
            productService.createProduct(createRequest);
            fail("Expected SellerId");
        }catch (ResponseStatusException ex) {

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Seller id is required", ex.getReason());
        }
    }

    @Test
    void TestThatCreateProductThrowsInvalidErrorWhenSellerIdIsEmpty() {
        createRequest.setSellerId("");

        try {
            productService.createProduct(createRequest);
            fail("Expected SellerId");
        }catch (ResponseStatusException ex) {

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Seller id is required", ex.getReason());
        }
    }

    @Test
    void TestThatCreateProductThrowsInvalidErrorWhenSellerDoesNotExist() {
        createRequest.setSellerId("Seller_Not_Existing");

        try {
            productService.createProduct(createRequest);
            fail("Expected SellerId");
        }catch (ResponseStatusException ex) {

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Seller not found", ex.getReason());
        }
    }


    @Test
    void TestThatUpdateProductThrowsInvalidErrorWhenProductIdIsNull() {
        updateRequest.setProductId(null);

        try {
            productService.updateProduct(updateRequest);
            fail("Expected product id not found");
        }catch (ResponseStatusException ex) {

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Product id not found", ex.getReason());
        }
    }

    @Test
    void TestThatUpdateProductThrowsInvalidErrorWhenProductIdIsEmpty() {
        updateRequest.setProductId("");

        try {
            productService.updateProduct(updateRequest);
            fail("Expected product id not found");
        }catch (ResponseStatusException ex) {

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Product id not found", ex.getReason());
        }
    }

    @Test
    void TestThatUpdateProductThrowsInvalidErrorWhenProductNameIsNull() {
        updateRequest.setProductName(null);

        try {
            productService.updateProduct(updateRequest);
            fail("Expected product name not found");
        }catch (ResponseStatusException ex) {

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Product name not found", ex.getReason());
        }
    }

    @Test
    void TestThatUpdateProductThrowsInvalidErrorWhenProductNameIsEmpty() {
        updateRequest.setProductName(" ");

        try {
            productService.updateProduct(updateRequest);
            fail("Expected product name not found");
        }catch (ResponseStatusException ex) {

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Product name not found", ex.getReason());
        }
    }

    @Test
    void TestThatUpdateProductThrowsInvalidErrorWhenProductDescriptionIsNull() {
        updateRequest.setProductDescription(null);

        try {
            productService.updateProduct(updateRequest);
            fail("Expected product description not found");
        }catch (ResponseStatusException ex) {

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Product description not found", ex.getReason());
        }
    }

    @Test
    void TestThatUpdateProductThrowsInvalidErrorWhenProductDescriptionIsEmpty() {
        updateRequest.setProductDescription(" ");

        try {
            productService.updateProduct(updateRequest);
            fail("Expected product description not found");
        }catch (ResponseStatusException ex) {

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Product description not found", ex.getReason());
        }
    }

    @Test
    void testThatUpdateProductThrowsInvalidErrorWhenProductPriceNull() {
        updateRequest.setProductPrice(null);

        try {
            productService.updateProduct(updateRequest);
            fail("Expected product price not found");
        }catch (ResponseStatusException ex) {

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Product price not found", ex.getReason());
        }
    }

    @Test
    void TestThatUpdateProductThrowsInvalidErrorWhenProductPriceIsNegative() {
        updateRequest.setProductPrice(-8.00);

        try {
            productService.updateProduct(updateRequest);
            fail("Expected product price greater than 0");
        }catch (ResponseStatusException ex) {

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Product price must be greater than 0", ex.getReason());
        }
    }

    @Test
    void TestThatUpdateProductThrowsInvalidErrorWhenProductPriceIsZero() {
        updateRequest.setProductPrice(0.0);

        try {
            productService.updateProduct(updateRequest);
            fail("Expected product price greater than 0");
        }catch (ResponseStatusException ex) {

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Product price must be greater than 0", ex.getReason());
        }
    }

    @Test
    void TestThatUpdateProductIsSuccessfulWhenProductIdIsGreaterThanZero() {

        Product oldPrice = new Product();
        oldPrice.setProductId("Checking_Id");
        oldPrice.setProductName("Old Name");
        oldPrice.setProductDescription("Old Desc");
        oldPrice.setProductPrice(1000.00);
        oldPrice.setProductQuantity(5);
        oldPrice.setProductCategory(Category.ELECTRONICS);

        productRepository.save(oldPrice);
        updateRequest.setProductPrice(2.00);

        UpdateProductResponseDto updateResponse = productService.updateProduct(updateRequest);
        assertEquals(2.00, updateResponse.getProductPrice());
    }

    @Test
    void TestThatUpdateProductThrowsInvalidErrorWhenProductQuantityIsNull(){
        updateRequest.setProductQuantity(null);

        try {
            productService.updateProduct(updateRequest);
            fail("Expected product quantity not null");
        }catch (ResponseStatusException ex) {

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Product quantity is required", ex.getReason());
        }
    }

    @Test
    void TestThatUpdateProductThrowsInvalidErrorWhenProductQuantityIsZero(){
        updateRequest.setProductQuantity(0);

        try {
            productService.updateProduct(updateRequest);
            fail("Expected product quantity greater than 0");
        }catch (ResponseStatusException ex) {

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Product quantity must be greater than 0", ex.getReason());
        }
    }

    @Test
    void TestThatUpdateProductThrowsInvalidErrorWhenProductQuantityIsNegative(){
        updateRequest.setProductQuantity(-1);

        try {
            productService.updateProduct(updateRequest);
            fail("Expected product quantity greater than 0");
        }catch (ResponseStatusException ex) {

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Product quantity must be greater than 0", ex.getReason());
        }
    }

    @Test
    void TestThatUpdateProductThrowsInvalidErrorWhenProductCategoryIsNull(){
        updateRequest.setProductCategory(null);

        try {
            productService.updateProduct(updateRequest);
            fail("Expected product category not null");
        }catch (ResponseStatusException ex) {

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Product category is required", ex.getReason());
        }
    }


    @Test
    void TestThatUpdateProductThrowsInvalidErrorWhenProductNameIsInvalid() {
        updateRequest.setProductName("invalid_name");

        try {
            productService.updateProduct(updateRequest);
            fail("Expected product valid name not found");
        }catch (ResponseStatusException ex) {

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Product not found", ex.getReason());
        }
    }

    @Test
    void TestThatUpdateProductThrowsInvalidErrorWhenProductIsNotFound() {
        try {
            productService.updateProduct(updateRequest);
            fail("Expected product not found");
        }catch (ResponseStatusException ex) {

            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
            assertEquals("Product not found", ex.getReason());
        }
    }

    @Test
    void TestThatUpdateProductWasUpdatedSuccessfully() {

        Product existing = new Product();
        existing.setProductId("Checking_Id");
        existing.setProductName("Old Name");
        existing.setProductDescription("Old Desc");
        existing.setProductPrice(1000.00);
        existing.setProductQuantity(5);
        existing.setProductCategory(Category.ELECTRONICS);

        productRepository.save(existing);

        UpdateProductResponseDto updateResponse = productService.updateProduct(updateRequest);

        assertNotNull(updateResponse.getProductId(), "Product ID cannot be null");
        assertEquals(updateResponse.getProductName(),updateRequest.getProductName());
        assertEquals(updateResponse.getProductPrice(),updateRequest.getProductPrice());
        assertEquals(updateResponse.getProductDescription(), updateRequest.getProductDescription());
        assertEquals(updateResponse.getProductPrice(), updateRequest.getProductPrice());
        assertEquals(updateResponse.getProductCategory(), updateRequest.getProductCategory());

        Product saved = productRepository.findById(updateResponse.getProductId()).orElse(null);
        assertNotNull(saved, "Product must exist in DB");

        assertEquals("iPhone 17 ultra",  saved.getProductName());

    }
}