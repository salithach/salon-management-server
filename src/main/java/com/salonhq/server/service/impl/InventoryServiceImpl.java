package com.salonhq.server.service.impl;

import com.mongodb.client.result.DeleteResult;
import com.salonhq.server.dao.InventoryItem;
import com.salonhq.server.dao.InventorySale;
import com.salonhq.server.exception.InventoryOperationException;
import com.salonhq.server.model.request.InventoryItemRequest;
import com.salonhq.server.model.request.InventorySaleRequest;
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

    @Override
    public InventorySale sellInventoryItem(String itemId, InventorySaleRequest request) {
        InventoryItem existing = inventoryRepository.getInventoryItemById(itemId);
        if (existing == null) {
            throw new InventoryOperationException(String.format("Inventory item not found for id: %s", itemId));
        }
        if (!itemId.equalsIgnoreCase(request.getId())) {
            throw new InventoryOperationException(String.format("Inventory item id: %s should be same as sale id: %s", itemId, request.getId()));
        }
        if (!existing.getId().equalsIgnoreCase(request.getId()) || !existing.getId().equalsIgnoreCase(itemId)) {
            throw new InventoryOperationException(String.format("Inventory item id: %s should be same as sale id: %s", itemId, existing.getId()));
        }
        Integer quantitySold = request.getQuantity();
        if (existing.getQuantity() < quantitySold) {
            throw new InventoryOperationException(String.format("Not enough stock for item id: %s", itemId));
        }
        try {
            existing.setQuantity(existing.getQuantity() - quantitySold);
            inventoryRepository.updateInventoryItemById(itemId, new InventoryItemRequest(
                existing.getName(),
                existing.getCategory(),
                existing.getQuantity(),
                existing.getUnit(),
                existing.getThreshold(),
                existing.getPrice()
            ));
            InventorySale inventorySale = InventorySale.builder()
                .itemId(itemId)
                .name(existing.getName())
                .price(request.getPrice())
                .totalPrice(request.getPrice() * quantitySold)
                .quantity(quantitySold)
            .build();
            return inventoryRepository.recordInventorySale(itemId, inventorySale);
        } catch (Exception e) {
            throw new InventoryOperationException(String.format("Failed to record sale for item id: %s", itemId));
        }
    }
}

