package com.salonhq.server.config;

import com.salonhq.server.dao.InventoryItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InventoryDataInitializer implements CommandLineRunner {

    private final MongoTemplate mongoTemplate;

    public InventoryDataInitializer(@Qualifier("tenantMongoTemplate") MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        // Only seed if inventory is empty
        long count = mongoTemplate.count(new Query(), InventoryItem.class);
        if (count > 0) {
            return;
        }

        List<InventoryItem> items = new ArrayList<>();

        // Styling items
        items.add(createItem("", "Styling", 8, "PCS", 3));
        items.add(createItem("Hair Gel", "Styling", 6, "BOTTLES", 2));
        items.add(createItem("Hair Spray", "Styling", 5, "BOTTLES", 2));

        // Coloring items
        items.add(createItem("Bleach Powder", "Coloring", 2, "KG", 1));
        items.add(createItem("Hair Dye - Black", "Coloring", 12, "TUBES", 4));
        items.add(createItem("Hair Dye - Brown", "Coloring", 3, "TUBES", 4));
        items.add(createItem("Hair Dye - Blonde", "Coloring", 4, "TUBES", 3));
        items.add(createItem("Developer 20 Vol", "Coloring", 8, "BOTTLES", 2));

        // Hair Care items
        items.add(createItem("Shampoo (1L)", "Hair Care", 6, "BOTTLES", 2));
        items.add(createItem("Conditioner", "Hair Care", 4, "BOTTLES", 2));
        items.add(createItem("Hair Mask", "Hair Care", 3, "JARS", 2));
        items.add(createItem("Hair Serum", "Hair Care", 7, "BOTTLES", 3));

        // Nail Care items
        items.add(createItem("Nail Polish - Red", "Nail Care", 10, "BOTTLES", 3));
        items.add(createItem("Nail Polish - Pink", "Nail Care", 8, "BOTTLES", 3));
        items.add(createItem("Nail File", "Nail Care", 15, "PCS", 5));
        items.add(createItem("Nail Buffer", "Nail Care", 12, "PCS", 4));

        // Skin Care items
        items.add(createItem("Face Cleanser", "Skin Care", 5, "BOTTLES", 2));
        items.add(createItem("Face Moisturizer", "Skin Care", 4, "JARS", 2));
        items.add(createItem("Sunscreen SPF 30", "Skin Care", 6, "BOTTLES", 2));

        // Waxing items
        items.add(createItem("Body Wax - Warm", "Waxing", 7, "TUBS", 2));
        items.add(createItem("Wax Strips", "Waxing", 20, "PACKS", 5));
        items.add(createItem("Pre-Wax Oil", "Waxing", 4, "BOTTLES", 2));
        items.add(createItem("Post-Wax Lotion", "Waxing", 5, "BOTTLES", 2));

        // Tools
        items.add(createItem("Scissors - Hair", "Tools", 6, "PCS", 2));
        items.add(createItem("Comb - Fine Tooth", "Tools", 10, "PCS", 3));
        items.add(createItem("Brush - Round", "Tools", 8, "PCS", 3));
        items.add(createItem("Hair Clips", "Tools", 25, "PCS", 10));

        // Cleaning items
        items.add(createItem("Disinfectant Spray", "Cleaning", 4, "BOTTLES", 2));
        items.add(createItem("Sanitizing Wipes", "Cleaning", 8, "PACKS", 3));
        items.add(createItem("Towels - White", "Cleaning", 30, "PCS", 10));

        // Other
        items.add(createItem("Gloves - Latex", "Other", 100, "PAIRS", 20));
        items.add(createItem("Cotton Pads", "Other", 50, "PACKS", 10));

        // Insert all items
        mongoTemplate.insert(items, InventoryItem.class);
    }

    private InventoryItem createItem(String name, String category, Integer quantity, String unit, Integer threshold) {
        return InventoryItem.builder()
            .id(UUID.randomUUID().toString())
            .name(name)
            .category(category)
            .quantity(quantity)
            .unit(unit)
            .threshold(threshold)
            .status(determineStatus(quantity, threshold))
            .build();
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

