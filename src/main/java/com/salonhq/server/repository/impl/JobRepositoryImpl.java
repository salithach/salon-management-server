package com.salonhq.server.repository.impl;

import com.mongodb.client.result.DeleteResult;
import com.salonhq.server.dao.Job;
import com.salonhq.server.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.salonhq.server.util.Constants.DbFields.*;

@Repository
public class JobRepositoryImpl implements JobRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    JobRepositoryImpl(@Qualifier("tenantMongoTemplate") MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Job addJob(Job jobInfo) {
        return mongoTemplate.save(jobInfo);
    }

    @Override
    public List<Job> getJobs(String date) {
        Query query = Query.query(Criteria.where(DATE.getValue()).is(date));
        return mongoTemplate.find(query, Job.class);
    }

    @Override
    public Optional<Job> getJobByUsernameAndDate(String username, String date) {
        Query query = Query.query(Criteria.where(ASSIGNEE.getValue()).is(username)
            .and(DATE.getValue()).is(date));
        Job jobByUsername = mongoTemplate.findOne(query, Job.class);
        if (jobByUsername != null) {
            return Optional.of(jobByUsername);
        }
        return Optional.empty();
    }

    @Override
    public DeleteResult deleteJobsByDate(String date) {
        Query query = Query.query(Criteria.where(DATE.getValue()).is(date));
        return mongoTemplate.remove(query, Job.class);
    }
}
