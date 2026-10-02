package com.salonhq.server.model.request;

import lombok.Data;

@Data
public class InventorySaleRequest {
    String id;
    String name;
    Integer quantity;
    Double price;
}

