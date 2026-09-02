package com.salonhq.server.dao;

import com.salonhq.server.model.tenant.TenantEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "inventory")
public class InventoryItem extends TenantEntity {
    @Id
    String id;
    String name;
    String category;
    Integer quantity;
    String unit;
    Integer threshold;
    String status;
}

