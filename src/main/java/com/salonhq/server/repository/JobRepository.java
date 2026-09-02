package com.salonhq.server.repository;

import com.mongodb.client.result.DeleteResult;
import com.salonhq.server.dao.Job;

import java.util.List;
import java.util.Optional;

public interface JobRepository {
    Job addJob(Job jobInfo);
    List<Job> getJobs(String date);
    Optional<Job> getJobByUsernameAndDate(String username, String date);
    DeleteResult deleteJobsByDate(String date);
}
