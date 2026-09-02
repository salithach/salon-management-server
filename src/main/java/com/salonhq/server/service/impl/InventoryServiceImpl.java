package com.salonhq.server.service.impl;

import com.mongodb.client.result.DeleteResult;
import com.salonhq.server.dao.InventoryItem;
import com.salonhq.server.exception.InventoryOperationException;
import com.salonhq.server.model.request.InventoryItemRequest;
import com.salonhq.server.model.response.DeleteResponse;
import com.salonhq.server.repository.InventoryRepository;
import com.salonhq.server.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public List<InventoryItem> getAllInventoryItems() {
        return inventoryRepository.getAllInventoryItems();
    }

    @Override
    public InventoryItem getInventoryItemById(String id) {
        return inventoryRepository.getInventoryItemById(id);
    }

    @Override
    public InventoryItem createInventoryItem(InventoryItemRequest request) {
        return inventoryRepository.addInventoryItem(request);
    }

    @Override
    public InventoryItem editInventoryItem(String id, InventoryItemRequest request) {
        InventoryItem existing = inventoryRepository.getInventoryItemById(id);
        if (existing == null) {
            throw new InventoryOperationException(String.format("Inventory item not found for id: %s", id));
        }
        return inventoryRepository.updateInventoryItemById(id, request);
    }

    @Override
    public DeleteResponse deleteInventoryItem(String id) {
        InventoryItem existing = inventoryRepository.getInventoryItemById(id);
        if (existing == null) {
            throw new InventoryOperationException(String.format("Inventory item not found for id: %s", id));
        }
        DeleteResult result = inventoryRepository.deleteInventoryItemById(id);
        if (result.getDeletedCount() == 1) {
            return DeleteResponse.builder().message(String.format("Deleted inventory item: %s", id)).build();
        } else {
            throw new InventoryOperationException(String.format("Failed to delete inventory item: %s", id));
        }
    }

    @Override
    public Map<String, Integer> getInventorySummary() {
        List<InventoryItem> items = inventoryRepository.getAllInventoryItems();
        Map<String, Integer> summary = new HashMap<>();

        int totalItems = items.size();
        int inStock = (int) items.stream().filter(i -> "In Stock".equals(i.getStatus())).count();
        int lowStock = (int) items.stream().filter(i -> "Low Stock".equals(i.getStatus())).count();
        int outOfStock = (int) items.stream().filter(i -> "Out of Stock".equals(i.getStatus())).count();

        summary.put("totalItems", totalItems);
        summary.put("inStock", inStock);
        summary.put("lowStock", lowStock);
        summary.put("outOfStock", outOfStock);

        return summary;
    }
}

