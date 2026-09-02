package com.salonhq.server.controller;

import com.salonhq.server.dao.InventoryItem;
import com.salonhq.server.model.request.InventoryItemRequest;
import com.salonhq.server.model.response.DeleteResponse;
import com.salonhq.server.model.response.EnvelopedResponse;
import com.salonhq.server.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("")
    public ResponseEntity<?> getInventoryItems() {
        List<InventoryItem> items = inventoryService.getAllInventoryItems();
        EnvelopedResponse<Object> response = EnvelopedResponse.builder()
            .data(items)
            .errors(List.of())
        .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/summary")
    public ResponseEntity<?> getInventorySummary() {
        Map<String, Integer> summary = inventoryService.getInventorySummary();
        EnvelopedResponse<Object> response = EnvelopedResponse.builder()
            .data(summary)
            .errors(List.of())
        .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<?> getInventoryItemById(@PathVariable String itemId) {
        InventoryItem item = inventoryService.getInventoryItemById(itemId);
        EnvelopedResponse<Object> response = EnvelopedResponse.builder()
            .data(item)
            .errors(List.of())
        .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("")
    public ResponseEntity<?> addInventoryItem(@RequestBody InventoryItemRequest request) {
        InventoryItem item = inventoryService.createInventoryItem(request);
        EnvelopedResponse<Object> response = EnvelopedResponse.builder()
            .data(item)
            .errors(List.of())
        .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<?> editInventoryItem(
        @PathVariable String itemId,
        @RequestBody InventoryItemRequest request
    ) {
        InventoryItem item = inventoryService.editInventoryItem(itemId, request);
        EnvelopedResponse<Object> response = EnvelopedResponse.builder()
            .data(item)
            .errors(List.of())
        .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<?> deleteInventoryItem(@PathVariable String itemId) {
        DeleteResponse deleteResponse = inventoryService.deleteInventoryItem(itemId);
        EnvelopedResponse<Object> response = EnvelopedResponse.builder()
            .data(deleteResponse)
            .errors(List.of())
        .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

