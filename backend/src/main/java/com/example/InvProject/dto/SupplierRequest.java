package com.example.InvProject.dto;

public record SupplierRequest(
    String name,
    String phoneNumber,
    String email,
    String contactPerson
) {}