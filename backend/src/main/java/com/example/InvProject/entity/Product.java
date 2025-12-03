package com.example.InvProject.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // products.name (not null)
    @Column(nullable = false)
    private String name;

    // products.description (TEXT)
    @Column(columnDefinition = "TEXT")
    private String description;

    // products.price numeric(19,2)
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    // products.quantity_in_stock
    @Column(name = "quantity_in_stock", nullable = false)
    private int quantityInStock;

    // products.reorder_point (nullable)
    @Column(name = "reorder_point")
    private Integer reorderPoint;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;
}