package com.example.InvProject.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.InvProject.dto.CategoryCreateRequest;
import com.example.InvProject.entity.Category;
import com.example.InvProject.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
    
    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    public Category createCategory(CategoryCreateRequest request){
        Category newCategory = new Category();
        newCategory.setName(request.name());
        return categoryRepository.save(newCategory);
    }

    public Category getCategoryById(Long id){
        return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category with id:" + id + " cant be found"));
    }

    public Category updateCategory(Long id, CategoryCreateRequest request){
        Category categoryToUpdate = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category with id:" + id + " cant be found"));
        categoryToUpdate.setName(request.name());
        return categoryRepository.save(categoryToUpdate);
    }

    public void removeCategory(Long id){
        if(!categoryRepository.existsById(id)){
            throw new RuntimeException("Category not founnd with id:" + id);
        }
        categoryRepository.deleteById(id);
    }

}
