package com.gbesh.first.controller;


import com.gbesh.first.exceptions.AlreadyExistsException;
import com.gbesh.first.exceptions.ResourceNotFoundException;
import com.gbesh.first.model.Category;
import com.gbesh.first.response.ApiResponse;
import com.gbesh.first.service.category.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final ICategoryService categoryService;

    @GetMapping("/home")
    public ResponseEntity<ApiResponse> home(){
        return ResponseEntity.ok(new ApiResponse("hello", new HashMap<String, Integer>(Map.of(
                "man", 34,
                "woman", 12
        ))));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories(){
        try {
            List<Category> categoryList = categoryService.getAllCategories();
            return ResponseEntity.ok(new ApiResponse("Found", categoryList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category name){
        try {
            Category category = categoryService.addCategory(name);
            return ResponseEntity.ok(new ApiResponse("Success", category));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long categoryId){
        try {
            Category category = categoryService.getCategoryById(categoryId);
            return ResponseEntity.ok(new ApiResponse("Success", category));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("name/{name}")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name){
        try {
            Category category = categoryService.getCategoryByName(name);
            return ResponseEntity.ok(new ApiResponse("Found", category));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{categoryId}/delete")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long categoryId){
        try {
            categoryService.deleteCategoryById(categoryId);
            return ResponseEntity.ok(new ApiResponse("Deleted", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{categoryId}/update")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable Long categoryId, @RequestBody Category category){
        try{
            Category updatedCategory = categoryService.updateCategory(category.getName(), categoryId);
            return ResponseEntity.ok(new ApiResponse("Update Success", updatedCategory));
        } catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
}
