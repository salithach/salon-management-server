package com.salonhq.server.repository;

import com.salonhq.server.dao.Job;
import com.salonhq.server.model.JobDetails;

public interface JobRepository {
    Job addJob(Job jobInfo);
}
