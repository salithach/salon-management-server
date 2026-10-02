package com.salonhq.server.config;

import com.salonhq.server.model.tenant.TenantAware;
import com.salonhq.server.model.tenant.TenantContext;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@Component
public class TenantMongoListener extends AbstractMongoEventListener<Object> {

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Object> event) {
        Object entity = event.getSource();
        if (entity instanceof TenantAware tenantEntity) {
            if (tenantEntity.getTenantId() == null) {
                tenantEntity.setTenantId(TenantContext.getTenant());
            }
        }
    }

}
