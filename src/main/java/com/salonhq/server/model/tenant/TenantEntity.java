package com.salonhq.server.model.tenant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class TenantEntity implements TenantAware {
    private String tenantId;
}
