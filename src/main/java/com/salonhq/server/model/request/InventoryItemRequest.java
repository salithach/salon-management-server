package com.salonhq.server.model.request;

import lombok.Data;

@Data
public class InventoryItemRequest {
    String name;
    String category;
    Integer quantity;
    String unit;
    Integer threshold;
}

