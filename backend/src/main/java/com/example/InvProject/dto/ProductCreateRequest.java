package com.example.InvProject.dto;

import java.math.BigDecimal;

public record ProductCreateRequest(String name, String description, BigDecimal price, int quantityInStock, Long categoryId) {
    
}
 