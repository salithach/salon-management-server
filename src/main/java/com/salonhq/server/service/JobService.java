package com.salonhq.server.service;

import com.salonhq.server.dao.Job;
import com.salonhq.server.model.request.JobRequest;

public interface JobService {
    Job createJob(JobRequest jobRequest);
}
