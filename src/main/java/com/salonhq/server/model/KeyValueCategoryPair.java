package com.salonhq.server.model;

import com.salonhq.server.model.tenant.TenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class KeyValueCategoryPair extends TenantEntity {
    String key;
    String value;
    String category;
}
