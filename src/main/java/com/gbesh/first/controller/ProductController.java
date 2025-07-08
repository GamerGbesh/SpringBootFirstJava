package com.gbesh.first.controller;

import com.gbesh.first.dto.ProductDto;
import com.gbesh.first.exceptions.ProductNotFoundException;
import com.gbesh.first.model.Product;
import com.gbesh.first.request.AddProductRequest;
import com.gbesh.first.request.ProductUpdateRequest;
import com.gbesh.first.response.ApiResponse;
import com.gbesh.first.service.product.IProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final IProductService productService;

    @GetMapping("/get/{productId}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId){
        try {
            Product product = productService.getProductById(productId);
            ProductDto productDto = productService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("Success", productDto));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long productId, @RequestBody ProductUpdateRequest request){
        try {
            Product product = productService.updateProduct(request, productId);
            ProductDto productDto = productService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("Updated", productDto));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllProducts(){
        try {
            List<Product> products = productService.getAllProducts();
            List<ProductDto> productDtos = productService.convertProducts(products);
            return ResponseEntity.ok(new ApiResponse("Gotten all products", productDtos));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping(value="/count", params = {"brand", "name"})
    public ResponseEntity<ApiResponse> countProductsByBrandAndName(@RequestParam String brand, @RequestParam String name){
        try {
            return ResponseEntity.ok(new ApiResponse("Count of products by brand '" + brand + "' and name '" + name + "'",
                    productService.countProductByBrandAndName(brand, name)));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<ApiResponse> deleteProductById(@PathVariable Long productId){
        try {
            productService.deleteProductById(productId);
            return ResponseEntity.ok(new ApiResponse("Deleted product of id " + productId, null));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest request){
        try {
            Product product = productService.addProduct(request);
            ProductDto productDto = productService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("Product added successfully", productDto));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
}
