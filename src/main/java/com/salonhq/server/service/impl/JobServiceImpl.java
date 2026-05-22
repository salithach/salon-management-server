package com.salonhq.server.service.impl;

import com.salonhq.server.dao.Job;
import com.salonhq.server.model.JobDetails;
import com.salonhq.server.model.request.JobRequest;
import com.salonhq.server.repository.JobRepository;
import com.salonhq.server.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;

    @Autowired
    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public Job createJob(JobRequest jobRequest) {
        String date = LocalDate.now().toString();
        String id = String.format("%s-%s-%s", date, jobRequest.getAssignee().getUsername(), "jobs");
        Job job = Job.builder()
            .id(id)
            .date(date)
            .assignee(jobRequest.getAssignee().getUsername())
            .details(
                JobDetails.builder()
                    .description(jobRequest.getDescription())
                    .price(jobRequest.getPrice())
                    .services(jobRequest.getService())
                .build()
            )
        .build();
        return jobRepository.addJob(job);
    }

}
