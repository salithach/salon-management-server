package com.salonhq.server.util;

import lombok.Getter;

public class Constants {
    public static final String ROLES = "roles";
    public static final String LOCAL_ENV = "LOCAL";
    public static final String AUTH_HEADER = "Authorization";
    public static final String BEARER = "Bearer";

    @Getter
    public enum DbFields {
        ID("_id"),
        USERNAME("username"),
        EMAIL("email"),
        TENANT_ID("tenantId"),
        DATE("date"),
        ASSIGNEE("assignee");
        private final String value;
        DbFields(String value) {
            this.value = value;
        }
    }

}
