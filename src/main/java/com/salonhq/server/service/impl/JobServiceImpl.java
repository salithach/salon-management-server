package com.salonhq.server.service.impl;

import com.salonhq.server.dao.Job;
import com.salonhq.server.dao.StaffMember;
import com.salonhq.server.model.JobDetails;
import com.salonhq.server.model.request.JobRequest;
import com.salonhq.server.repository.JobRepository;
import com.salonhq.server.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        String username = jobRequest.getAssignee().getUsername();
        String id = String.format("%s-%s-%s", date, username, "jobs");
        JobDetails newJob = JobDetails.builder()
            .services(jobRequest.getServices())
            .price(jobRequest.getPrice())
            .description(jobRequest.getDescription())
        .build();
        Job jobToSave = jobRepository.getJobByUsernameAndDate(username, date)
            .map(existingJob -> {
                existingJob.getJobs().add(newJob);
                return existingJob;
            })
            .orElseGet(
                () -> Job.builder()
                    .id(id)
                    .date(date)
                    .assignee(username)
                    .jobs(new ArrayList<>(List.of(newJob)))
                .build()
            );
        return jobRepository.addJob(jobToSave);
    }

    @Override
    public List<Job> getJobs(String date) {
        return jobRepository.getJobs(date);
    }

}
