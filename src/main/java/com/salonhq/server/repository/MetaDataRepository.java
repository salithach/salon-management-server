package com.salonhq.server.repository;

import com.salonhq.server.dao.JobRole;
import com.salonhq.server.dao.JobType;
import com.salonhq.server.model.KeyValuePair;

import java.util.List;

public interface MetaDataRepository {
    List<JobRole> getJobRoles();
    List<JobType> getJobTypes();
    List<JobRole> createJobRoles(List<KeyValuePair> jobRoleRequest);
    List<JobType> createJobTypes(List<KeyValuePair> jobTypeRequest);
}
