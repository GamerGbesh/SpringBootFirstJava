package com.gbesh.first.service.product;

import com.gbesh.first.dto.CategoryDto;
import com.gbesh.first.dto.ImageDTO;
import com.gbesh.first.dto.ProductDto;
import com.gbesh.first.exceptions.ProductNotFoundException;
import com.gbesh.first.model.Category;
import com.gbesh.first.model.Image;
import com.gbesh.first.model.Product;
import com.gbesh.first.repository.CategoryRepository;
import com.gbesh.first.repository.ImageRepository;
import com.gbesh.first.repository.ProductRepository;
import com.gbesh.first.request.AddProductRequest;
import com.gbesh.first.request.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    @Override
    public Product addProduct(AddProductRequest request) {
        // Check if category is found in Db,
        // If yes, set it as the new product else save it as a new category
        // Then set it as the category of the product
        String categoryName = request.getCategory().getName();
        Category category = Optional.ofNullable(categoryRepository.findByName(categoryName))
                .orElseGet(() ->{
                    Category newCategory = new Category(categoryName);
                    return categoryRepository.save(newCategory);
                });
        return productRepository.save(createProduct(request, category));
    }

    private Product createProduct(AddProductRequest request, Category category){
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getDescription(),
                request.getPrice(),
                request.getInventory(),
                category
        );
    }


    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found!"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id).ifPresentOrElse(productRepository::delete,
                () -> {throw new ProductNotFoundException("Product not found!");});
    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(productRepository::save)
                .orElseThrow(() -> new ProductNotFoundException("Product not found!"));
    }

    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request){
        existingProduct.setBrand(request.getBrand());
        existingProduct.setName(request.getName());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByBrandAndCategory(String brand, String category) {
        return productRepository.findByCategoryNameAndBrand(brand, category);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }

    @Override
    public ProductDto convertToDto(Product product){
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDTO> imageDTOS =  images.stream().map(image -> modelMapper.map(image, ImageDTO.class)).toList();
        Category category = product.getCategory();
        CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);
        productDto.setCategory(categoryDto);
        productDto.setImages(imageDTOS);
        return productDto;
    }

    @Override
    public List<ProductDto> convertProducts(List<Product> products){
        return products.stream().map(this::convertToDto).toList();
    }


}
