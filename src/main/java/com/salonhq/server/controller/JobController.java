package com.salonhq.server.controller;

import com.salonhq.server.dao.Job;
import com.salonhq.server.model.request.JobRequest;
import com.salonhq.server.model.response.DeleteResponse;
import com.salonhq.server.model.response.EnvelopedResponse;
import com.salonhq.server.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {

    private final JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("")
    public ResponseEntity<?> addJob(@RequestBody JobRequest jobRequest) {
        Job jobResponse = jobService.createJob(jobRequest);
        EnvelopedResponse<Object> response = EnvelopedResponse.builder()
            .data(jobResponse)
            .errors(List.of())
        .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("")
    public ResponseEntity<?> getJobs(@RequestParam("date") String date) {
        List<Job> jobsResponse = jobService.getJobs(date);
        EnvelopedResponse<Object> response = EnvelopedResponse.builder()
            .data(jobsResponse)
            .errors(List.of())
        .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteJobs(@RequestParam("date") String date) {
        DeleteResponse jobsDeletionResponse = jobService.removeJobsByDate(date);
        EnvelopedResponse<Object> response = EnvelopedResponse.builder()
            .data(jobsDeletionResponse)
            .errors(List.of())
        .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
