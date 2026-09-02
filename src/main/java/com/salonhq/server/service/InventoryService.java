package com.salonhq.server.service;

import com.salonhq.server.dao.InventoryItem;
import com.salonhq.server.model.request.InventoryItemRequest;
import com.salonhq.server.model.response.DeleteResponse;

import java.util.List;
import java.util.Map;

public interface InventoryService {
    List<InventoryItem> getAllInventoryItems();
    InventoryItem getInventoryItemById(String id);
    InventoryItem createInventoryItem(InventoryItemRequest request);
    InventoryItem editInventoryItem(String id, InventoryItemRequest request);
    DeleteResponse deleteInventoryItem(String id);
    Map<String, Integer> getInventorySummary();
}

