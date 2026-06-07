package com.salonhq.server.repository.impl;

import com.salonhq.server.dao.JobRole;
import com.salonhq.server.dao.JobType;
import com.salonhq.server.dao.SalonType;
import com.salonhq.server.repository.MetaDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.salonhq.server.util.Constants.DbFields.TENANT_ID;

@Repository
public class MetaDataRepositoryImpl implements MetaDataRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MetaDataRepositoryImpl(@Qualifier("tenantMongoTemplate") MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<JobRole> getJobRoles() {
        Query query = Query.query(Criteria.where(TENANT_ID.getValue()));
        return mongoTemplate.find(query, JobRole.class);
    }

    @Override
    public List<JobType> getJobTypes() {
        Query query = Query.query(Criteria.where(TENANT_ID.getValue()));
        return mongoTemplate.find(query, JobType.class);
    }

    @Override
    public List<SalonType> getSalonTypes() {
        Query query = Query.query(Criteria.where(TENANT_ID.getValue()));
        return mongoTemplate.find(query, SalonType.class);
    }

    @Override
    public List<JobRole> createJobRoles(List<JobRole> jobRoles) {
        return mongoTemplate.insertAll(jobRoles).stream().toList();
    }

    @Override
    public List<JobType> createJobTypes(List<JobType> jobTypes) {
        return mongoTemplate.insertAll(jobTypes).stream().toList();
    }

    @Override
    public List<SalonType> createSalonTypes(List<SalonType> salonTypes) {
        return mongoTemplate.insertAll(salonTypes).stream().toList();
    }
}
