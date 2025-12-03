package com.example.InvProject.dto;

public class InventoryDTOs {
  public record AdjustInventoryRequest(int delta) {}
  public record InventoryView(Long productId, String name, int quantityOnHand, Integer reorderPoint) {}
}
