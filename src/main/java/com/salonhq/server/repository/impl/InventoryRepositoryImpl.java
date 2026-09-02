package com.salonhq.server.repository.impl;

import com.mongodb.client.result.DeleteResult;
import com.salonhq.server.dao.InventoryItem;
import com.salonhq.server.model.request.InventoryItemRequest;
import com.salonhq.server.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class InventoryRepositoryImpl implements InventoryRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public InventoryRepositoryImpl(@Qualifier("tenantMongoTemplate") MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<InventoryItem> getAllInventoryItems() {
        return mongoTemplate.findAll(InventoryItem.class);
    }

    @Override
    public InventoryItem getInventoryItemById(String id) {
        return mongoTemplate.findById(id, InventoryItem.class);
    }

    @Override
    public InventoryItem addInventoryItem(InventoryItemRequest request) {
        InventoryItem item = new InventoryItem();
        item.setId(UUID.randomUUID().toString());
        item.setName(request.getName());
        item.setCategory(request.getCategory());
        item.setQuantity(request.getQuantity());
        item.setUnit(request.getUnit());
        item.setThreshold(request.getThreshold());
        item.setStatus(determineStatus(request.getQuantity(), request.getThreshold()));
        return mongoTemplate.save(item);
    }

    @Override
    public InventoryItem updateInventoryItemById(String id, InventoryItemRequest request) {
        InventoryItem existing = mongoTemplate.findById(id, InventoryItem.class);
        if (existing == null) return null;
        existing.setName(request.getName() != null ? request.getName() : existing.getName());
        existing.setCategory(request.getCategory() != null ? request.getCategory() : existing.getCategory());
        existing.setQuantity(request.getQuantity() != null ? request.getQuantity() : existing.getQuantity());
        existing.setUnit(request.getUnit() != null ? request.getUnit() : existing.getUnit());
        existing.setThreshold(request.getThreshold() != null ? request.getThreshold() : existing.getThreshold());
        existing.setStatus(determineStatus(existing.getQuantity(), existing.getThreshold()));
        return mongoTemplate.save(existing);
    }

    @Override
    public DeleteResult deleteInventoryItemById(String id) {
        Query query = Query.query(Criteria.where("id").is(id));
        return mongoTemplate.remove(query, InventoryItem.class);
    }

    private String determineStatus(Integer quantity, Integer threshold) {
        if (quantity == null || quantity <= 0) {
            return "Out of Stock";
        }
        if (threshold == null) {
            return "In Stock";
        }
        if (quantity <= threshold) {
            return "Low Stock";
        }
        return "In Stock";
    }
}

