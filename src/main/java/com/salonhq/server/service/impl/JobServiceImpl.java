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
        StaffMember assignee = jobRequest.getAssignee();
        String username = assignee.getUsername();
        String id = String.format("%s-%s-%s", date, jobRequest.getAssignee().getUsername(), "jobs");
        Job job = Job.builder()
            .id(id)
            .date(date)
            .assignee(username)
        .build();
        JobDetails jobDetails = JobDetails.builder()
            .services(jobRequest.getServices())
            .price(jobRequest.getPrice())
            .description(jobRequest.getDescription())
        .build();
        Optional<Job> jobByUsername = jobRepository.getJobByUsername(username);
        if(jobByUsername.isPresent()) {
            Job existingJobByUser = jobByUsername.get();
            List<JobDetails> existingJobs = existingJobByUser.getJobs();
            existingJobs.add(jobDetails);
            job.setJobs(existingJobs);
        } else {
            job.setJobs(List.of(jobDetails));
        }
        return jobRepository.addJob(job);
    }

    @Override
    public List<Job> getJobs(String date) {
        return jobRepository.getJobs(date);
    }

}
