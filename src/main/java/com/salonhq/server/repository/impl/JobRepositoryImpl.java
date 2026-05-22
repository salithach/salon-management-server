package com.salonhq.server.repository.impl;

import com.salonhq.server.dao.Job;
import com.salonhq.server.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JobRepositoryImpl implements JobRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    JobRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Job addJob(Job jobInfo) {
        return mongoTemplate.save(jobInfo);
    }
}
