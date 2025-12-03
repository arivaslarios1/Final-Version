package com.example.InvProject.service;

import com.example.InvProject.dto.ProductCreateRequest;
import com.example.InvProject.entity.Category;
import com.example.InvProject.entity.Product;
import com.example.InvProject.repository.CategoryRepository;
import com.example.InvProject.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    // ===== Read =====
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));
    }

    // ===== Create =====
    public Product createProduct(ProductCreateRequest request) {
        Product p = new Product();
        p.setName(request.name());
        p.setDescription(request.description());
        // Product.price is likely BigDecimal — assign directly
        p.setPrice(request.price());
        // Ensure your Product entity has setQuantityInStock(int)
        p.setQuantityInStock(request.quantityInStock());

        if (request.categoryId() != null) {
            Category cat = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found: " + request.categoryId()));
            p.setCategory(cat);
        }

        return productRepository.save(p);
    }

    // ===== Update =====
    public Product updateProduct(Long id, ProductCreateRequest request) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));

        existing.setName(request.name());
        existing.setDescription(request.description());
        existing.setPrice(request.price());
        existing.setQuantityInStock(request.quantityInStock());

        if (request.categoryId() != null) {
            Category cat = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found: " + request.categoryId()));
            existing.setCategory(cat);
        } else {
            existing.setCategory(null);
        }

        return productRepository.save(existing);
    }

    // ===== Delete =====
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product not found: " + id);
        }
        productRepository.deleteById(id);
    }
}
