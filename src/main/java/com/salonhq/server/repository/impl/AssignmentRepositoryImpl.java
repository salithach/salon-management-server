package com.salonhq.server.repository.impl;

import com.mongodb.client.result.DeleteResult;
import com.salonhq.server.dao.DailyAssignment;
import com.salonhq.server.model.tenant.TenantContext;
import com.salonhq.server.repository.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.salonhq.server.util.Constants.DbFields.*;

@Repository
public class AssignmentRepositoryImpl implements AssignmentRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public AssignmentRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Optional<DailyAssignment> retrieveDailyAssignment(String date) {
        String tenantId = TenantContext.getTenant();
        Query query = Query.query(Criteria.where(DATE.getValue()).is(date)
            .and(TENANT_ID.getValue()).is(tenantId));
        List<DailyAssignment> dailyAssignments = mongoTemplate.find(query, DailyAssignment.class);
        if (dailyAssignments.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(dailyAssignments.get(0));
    }

    @Override
    public DailyAssignment addDailyAssignment(DailyAssignment dailyAssignment) {
        return mongoTemplate.save(dailyAssignment);
    }

    @Override
    public DeleteResult removeDailyAssignment(String id) {
        String tenantId = TenantContext.getTenant();
        Query query = Query.query(Criteria.where(ID.getValue()).is(id)
            .and(TENANT_ID.getValue()).is(tenantId));
        return mongoTemplate.remove(query, DailyAssignment.class);
    }
}
