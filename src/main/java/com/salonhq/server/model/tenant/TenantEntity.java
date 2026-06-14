package com.salonhq.server.model.tenant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public abstract class TenantEntity implements TenantAware {
    private String tenantId;
}
