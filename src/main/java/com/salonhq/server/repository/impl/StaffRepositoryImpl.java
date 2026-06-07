package com.salonhq.server.repository.impl;

import com.mongodb.client.result.DeleteResult;
import com.salonhq.server.dao.StaffMember;
import com.salonhq.server.model.tenant.TenantContext;
import com.salonhq.server.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.salonhq.server.util.Constants.DbFields.*;

@Repository
public class StaffRepositoryImpl implements StaffRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public StaffRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public StaffMember addStaff(StaffMember staffMember) {
        staffMember.setId(UUID.randomUUID().toString());
        return mongoTemplate.save(staffMember);
    }

    @Override
    public Optional<StaffMember> getByUsername(String username) {
        String tenantId = TenantContext.getTenant();
        Query query = Query.query(Criteria.where(USERNAME.getValue()).is(username)
            .and(TENANT_ID.getValue()).is(tenantId));
        return Optional.ofNullable(mongoTemplate.findOne(query, StaffMember.class));
    }

    @Override
    public Optional<StaffMember> getByEmail(String email) {
        String tenantId = TenantContext.getTenant();
        Query query = Query.query(Criteria.where(EMAIL.getValue()).is(email)
            .and(TENANT_ID.getValue()).is(tenantId));
        return Optional.ofNullable(mongoTemplate.findOne(query, StaffMember.class));
    }

    @Override
    public Optional<StaffMember> getById(String id) {
        String tenantId = TenantContext.getTenant();
        Query query = Query.query(Criteria.where(ID.getValue()).is(id)
            .and(TENANT_ID.getValue()).is(tenantId));
        return Optional.ofNullable(mongoTemplate.findOne(query, StaffMember.class));
    }

    @Override
    public DeleteResult removeStaff(String username) {
        String tenantId = TenantContext.getTenant();
        Query query = Query.query(Criteria.where(USERNAME.getValue()).is(username)
            .and(TENANT_ID.getValue()).is(tenantId));
        return mongoTemplate.remove(query, StaffMember.class);
    }

    @Override
    public void modifyStaff() {

    }

    @Override
    public List<StaffMember> retrieveStaff() {
        String tenantId = TenantContext.getTenant();
        Query query = Query.query(Criteria.where(TENANT_ID.getValue()).is(tenantId));
        return mongoTemplate.find(query, StaffMember.class);
    }
}
