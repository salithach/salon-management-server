package com.salonhq.server.model.tenant;

public interface TenantAware {
    String getTenantId();
    void setTenantId(String tenantId);
}
