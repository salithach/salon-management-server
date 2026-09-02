package com.salonhq.server.repository;

import com.mongodb.client.result.DeleteResult;
import com.salonhq.server.dao.InventoryItem;
import com.salonhq.server.model.request.InventoryItemRequest;

import java.util.List;

public interface InventoryRepository {
    List<InventoryItem> getAllInventoryItems();
    InventoryItem getInventoryItemById(String id);
    InventoryItem addInventoryItem(InventoryItemRequest request);
    InventoryItem updateInventoryItemById(String id, InventoryItemRequest request);
    DeleteResult deleteInventoryItemById(String id);
}

