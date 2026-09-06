package com.salonhq.server.service;

import com.salonhq.server.dao.Job;
import com.salonhq.server.model.request.JobRequest;
import com.salonhq.server.model.response.DeleteResponse;

import java.util.List;

public interface JobService {
    Job createJob(JobRequest jobRequest);
    List<Job> getJobs(String date);
    DeleteResponse removeJobsByDate(String date);
}
