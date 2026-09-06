package com.salonhq.server.model.tenant;

public class TenantContext {

    private static final ThreadLocal<String> tenant = new ThreadLocal<>();

    public static void setTenant(String tenantId) {
        tenant.set(tenantId);
    }

    public static String getTenant() {
        return tenant.get();
    }

    public static void clear() {
        tenant.remove();
    }

}
