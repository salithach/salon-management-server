package com.salonhq.server.config;

import com.salonhq.server.model.tenant.TenantContext;
import org.jspecify.annotations.NonNull;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static com.salonhq.server.util.Constants.DbFields.TENANT_ID;

public class TenantMongoTemplate extends MongoTemplate {

    public TenantMongoTemplate(MongoDatabaseFactory dbFactory, MongoConverter converter) {
        super(dbFactory, converter);
    }

    private Query addTenant(Query query) {
        if (query == null) {
            query = new Query();
        }
        String tenantId = TenantContext.getTenant();
        if (tenantId == null) {
            throw new RuntimeException("Tenant not set");
        }
        query.addCriteria(Criteria.where(TENANT_ID.getValue()).is(tenantId));
        return query;
    }

    @Override
    public @NonNull <T> List<T> find(@NonNull Query query, @NonNull Class<T> entityClass) {
        return super.find(addTenant(query), entityClass);
    }

    @Override
    public <T> T findOne(@NonNull Query query, @NonNull Class<T> entityClass) {
        return super.findOne(addTenant(query), entityClass);
    }

    @Override
    public long count(@NonNull Query query, @NonNull Class<?> entityClass) {
        return super.count(addTenant(query), entityClass);
    }
}
