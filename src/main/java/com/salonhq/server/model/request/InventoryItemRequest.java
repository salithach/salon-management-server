package com.salonhq.server.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryItemRequest {
    String name;
    String category;
    Integer quantity;
    String unit;
    Integer threshold;
    Double price;
}

