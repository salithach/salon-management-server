package com.salonhq.server.repository;

import com.salonhq.server.dao.Job;

import java.util.List;
import java.util.Optional;

public interface JobRepository {
    Job addJob(Job jobInfo);
    List<Job> getJobs(String date);
    Optional<Job> getJobByUsername(String username);
}
