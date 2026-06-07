package com.salonhq.server.repository.impl;

import com.salonhq.server.dao.JobRole;
import com.salonhq.server.dao.JobType;
import com.salonhq.server.model.KeyValueCategoryPair;
import com.salonhq.server.model.KeyValuePair;
import com.salonhq.server.repository.MetaDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class MetaDataRepositoryImpl implements MetaDataRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MetaDataRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<JobRole> getJobRoles() {
        return mongoTemplate.find(new Query(), JobRole.class);
    }

    @Override
    public List<JobType> getJobTypes() {
        return mongoTemplate.find(new Query(), JobType.class);
    }

    @Override
    public List<JobRole> createJobRoles(List<KeyValuePair> jobRoleRequest) {
        List<JobRole> jobRoles = jobRoleRequest.stream()
            .map(jobRole ->
                JobRole.builder()
                    .id(UUID.randomUUID().toString())
                    .key(jobRole.getKey())
                    .value(jobRole.getValue())
                .build()
            )
            .collect(Collectors.toList());
        return mongoTemplate.insertAll(jobRoles).stream().toList();
    }

    @Override
    public List<JobType> createJobTypes(List<KeyValueCategoryPair> jobTypeRequest) {
        List<JobType> jobTypes = jobTypeRequest.stream()
            .map(jobRole ->
                JobType.builder()
                    .id(UUID.randomUUID().toString())
                    .key(jobRole.getKey())
                    .value(jobRole.getValue())
                    .category(jobRole.getCategory())
                .build()
            )
        .collect(Collectors.toList());
        return mongoTemplate.insertAll(jobTypes).stream().toList();
    }
}
