package com.salonhq.server.repository.impl;

import com.salonhq.server.dao.Role;
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
    public List<Role> getJobRoles() {
        return mongoTemplate.find(new Query(), Role.class);
    }

    @Override
    public List<Role> createJobRoles(List<KeyValuePair> jobRoleRequest) {
        List<Role> roles = jobRoleRequest.stream()
            .map(jobRole -> Role.builder()
                .id(UUID.randomUUID().toString())
                .key(jobRole.getKey())
                .name(jobRole.getValue())
                .build()
            )
        .collect(Collectors.toList());
        return (List<Role>) mongoTemplate.insertAll(roles);
    }
}
