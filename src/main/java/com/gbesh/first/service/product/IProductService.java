package com.gbesh.first.service.product;

import com.gbesh.first.dto.ProductDto;
import com.gbesh.first.model.Product;
import com.gbesh.first.request.AddProductRequest;
import com.gbesh.first.request.ProductUpdateRequest;

import java.util.List;

public interface IProductService {
    Product addProduct(AddProductRequest product);
    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProduct(ProductUpdateRequest product, Long productId);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByBrandAndCategory(String brand, String category);
    List<Product> getProductsByName(String name);
    List<Product> getProductsByBrandAndName(String brand, String name);
    Long countProductByBrandAndName(String brand, String name);

    ProductDto convertToDto(Product product);

    List<ProductDto> convertProducts(List<Product> products);
}
