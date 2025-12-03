package com.example.InvProject.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.InvProject.dto.ProductInventoryDto;
import com.example.InvProject.entity.Product;
import com.example.InvProject.service.ProductService;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final ProductService productService;

    public InventoryController(ProductService productService) {
        this.productService = productService;
    }

    // All products with their inventory quantities
    @GetMapping
    public List<ProductInventoryDto> getAllInventory() {
        return productService.getAllProducts().stream()
                .map(this::toDto)
                .toList();
    }

    // Only low-stock products (quantity <= reorderPoint)
    @GetMapping("/low-stock")
    public List<ProductInventoryDto> getLowStock() {
        return productService.getAllProducts().stream()
                .filter(p -> p.getReorderPoint() != null
                          && p.getQuantityInStock() <= p.getReorderPoint())
                .map(this::toDto)
                .toList();
    }

    private ProductInventoryDto toDto(Product p) {
        return new ProductInventoryDto(
                p.getId(),
                p.getName(),
                p.getQuantityInStock(),   // maps to DTO field quantityOnHand
                p.getReorderPoint()
        );
    }
}