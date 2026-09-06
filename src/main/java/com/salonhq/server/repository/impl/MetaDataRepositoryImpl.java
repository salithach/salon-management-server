package com.salonhq.server.repository.impl;

import com.salonhq.server.dao.JobRole;
import com.salonhq.server.dao.JobType;
import com.salonhq.server.dao.SalonType;
import com.salonhq.server.repository.MetaDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MetaDataRepositoryImpl implements MetaDataRepository {

    private final MongoTemplate tenantMongoTemplate;
    private final MongoTemplate globalMongoTemplate;

    @Autowired
    public MetaDataRepositoryImpl(
        @Qualifier("tenantMongoTemplate") MongoTemplate tenantMongoTemplate,
        @Qualifier("mongoTemplate") MongoTemplate globalMongoTemplate
    ) {
        this.tenantMongoTemplate = tenantMongoTemplate;
        this.globalMongoTemplate = globalMongoTemplate;
    }

    @Override
    public List<JobRole> getJobRoles() {
        return tenantMongoTemplate.find(new Query(), JobRole.class);
    }

    @Override
    public List<JobType> getJobTypes() {
        return tenantMongoTemplate.find(new Query(), JobType.class);
    }

    @Override
    public List<SalonType> getSalonTypes() {
        return globalMongoTemplate.find(new Query(), SalonType.class);
    }

    @Override
    public List<JobRole> createJobRoles(List<JobRole> jobRoles) {
        return tenantMongoTemplate.insertAll(jobRoles).stream().toList();
    }

    @Override
    public List<JobType> createJobTypes(List<JobType> jobTypes) {
        return tenantMongoTemplate.insertAll(jobTypes).stream().toList();
    }

    @Override
    public List<SalonType> createSalonTypes(List<SalonType> salonTypes) {
        return globalMongoTemplate.insertAll(salonTypes).stream().toList();
    }
}
