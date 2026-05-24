package com.salonhq.server.controller;

import com.salonhq.server.dao.JobRole;
import com.salonhq.server.dao.JobType;
import com.salonhq.server.model.KeyValuePair;
import com.salonhq.server.model.response.EnvelopedResponse;
import com.salonhq.server.service.MetaDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/metadata")
public class MetaDataController {

    private final MetaDataService metaDataService;

    @Autowired
    public MetaDataController(MetaDataService metaDataService) {
        this.metaDataService = metaDataService;
    }

    @GetMapping("/jobRoles")
    public ResponseEntity<?> getJobRoles() {
        List<JobRole> jobRolesResponse = metaDataService.getJobRolesList();
        EnvelopedResponse<Object> response = EnvelopedResponse.builder()
            .data(jobRolesResponse)
            .errors(List.of())
        .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/jobRoles")
    public ResponseEntity<?> addJobRole(@RequestBody List<KeyValuePair> roleRequest) {
        List<JobRole> jobRolesResponse = metaDataService.createJobRoles(roleRequest);
        EnvelopedResponse<Object> response = EnvelopedResponse.builder()
            .data(jobRolesResponse)
            .errors(List.of())
        .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/jobTypes")
    public ResponseEntity<?> getJobTypes() {
        List<JobType> jobTypesResponse = metaDataService.getJobTypesList();
        EnvelopedResponse<Object> response = EnvelopedResponse.builder()
            .data(jobTypesResponse)
            .errors(List.of())
        .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/jobTypes")
    public ResponseEntity<?> addJobTypes(@RequestBody List<KeyValuePair> roleRequest) {
        List<JobType> jobTypesResponse = metaDataService.createJobTypes(roleRequest);
        EnvelopedResponse<Object> response = EnvelopedResponse.builder()
            .data(jobTypesResponse)
            .errors(List.of())
        .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
