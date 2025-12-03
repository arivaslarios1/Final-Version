package com.example.InvProject.dto;

public record ProductInventoryDto(
        Long id,
        String name,
        int quantityOnHand,
        Integer reorderPoint
) {}