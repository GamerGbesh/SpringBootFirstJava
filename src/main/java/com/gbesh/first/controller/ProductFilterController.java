package com.gbesh.first.controller;

import com.gbesh.first.dto.ProductDto;
import com.gbesh.first.model.Product;
import com.gbesh.first.response.ApiResponse;
import com.gbesh.first.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products/filter")
public class ProductFilterController {
    private final IProductService productService;

    @GetMapping(params = {"brand", "!category", "!name"})
    public ResponseEntity<ApiResponse> getProductsByBrand(@RequestParam String brand){
        try {
            List<Product> products = productService.getProductsByBrand(brand);
            List<ProductDto> productDtos = productService.convertProducts(products);
            return ResponseEntity.ok(new ApiResponse("Gotten products by brand '" + brand + "'",
                   productDtos ));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping(params = {"brand", "category", "!name"})
    public ResponseEntity<ApiResponse> getProductsByBrandAndCategory(@RequestParam String brand, @RequestParam String category){
        try {
            List<Product> products = productService.getProductsByBrandAndCategory(brand, category);
            List<ProductDto> productDtos = productService.convertProducts(products);
            return ResponseEntity.ok(new ApiResponse("Gotten products by brand '" + brand + "' and category '" + category + "'",
                    productDtos));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping(params = {"category", "!brand"})
    public ResponseEntity<ApiResponse> getProductsByCategory(@RequestParam String category){
        try {
            List<Product> products = productService.getProductsByCategory(category);
            List<ProductDto> productDtos = productService.convertProducts(products);

            return ResponseEntity.ok(new ApiResponse("Gotten prodcuts by category '" + category + "'",
                    productDtos));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping(params = {"name", "!brand"})
    public ResponseEntity<ApiResponse> getProductsByName(@RequestParam String name){
        try {
            List<Product> products = productService.getProductsByName(name);
            List<ProductDto> productDtos = productService.convertProducts(products);

            return ResponseEntity.ok(new ApiResponse("Gotten products by name '" + name + "'",
                    productDtos));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping(params = {"name", "brand"})
    public ResponseEntity<ApiResponse> getProductsByNameAndBrand(@RequestParam String name, @RequestParam String brand){
        try {
            List<Product> products = productService.getProductsByBrandAndName(brand, name);
            List<ProductDto> productDtos = productService.convertProducts(products);
            return ResponseEntity.ok(new ApiResponse("Gotten products by name '" + name + "' and brand '" + brand + "'",
                    productDtos));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
}
