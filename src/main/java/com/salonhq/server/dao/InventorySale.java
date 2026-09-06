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
@Document(collection = "sales")
public class InventorySale extends TenantEntity {
    @Id
    String id;
    String itemId;
    String name;
    Integer quantity;
    Double price;
    Double totalPrice;
}

