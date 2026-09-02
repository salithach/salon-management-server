package com.salonhq.server.util;

import com.salonhq.server.model.RoleType;
import com.salonhq.server.model.tenant.TenantContext;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class AuthUtil {
    /**
     * If a target tenant ID is provided, verifies the caller is ROLE_ADMIN
     * and overrides TenantContext so the data is saved under the target tenant,
     * not the admin's own tenant.
     */
    public static void applyTenantOverride(String targetTenantId) {
        if (targetTenantId == null || targetTenantId.isEmpty()) return;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
          if (auth != null && !isAdmin(auth)) {
            throw new AccessDeniedException("Only admins can specify X-Tenant-Id");
        }
        TenantContext.setTenant(targetTenantId);
    }

    public static boolean isAdmin(Authentication auth) {
        return auth.getAuthorities().stream()
            .anyMatch(grantedAuthority ->
                Objects.equals(grantedAuthority.getAuthority(),
                RoleType.ROLE_ADMIN.name()
            ));
    }
}
