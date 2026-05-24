package com.salonhq.server.service;

import com.salonhq.server.dao.JobRole;
import com.salonhq.server.dao.JobType;
import com.salonhq.server.model.KeyValuePair;

import java.util.List;

public interface MetaDataService {
    List<JobRole> getJobRolesList();
    List<JobType> getJobTypesList();
    List<JobRole> createJobRoles(List<KeyValuePair> jobRoles);
    List<JobType> createJobTypes(List<KeyValuePair> jobTypes);
}
