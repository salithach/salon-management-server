package com.salonhq.server.service;

import com.salonhq.server.dao.JobRole;
import com.salonhq.server.dao.JobType;
import com.salonhq.server.dao.SalonType;
import com.salonhq.server.model.KeyValueCategoryPair;
import com.salonhq.server.model.KeyValuePair;

import java.util.List;

public interface MetaDataService {
    List<JobRole> getJobRolesList();
    List<JobType> getJobTypesList();
    List<SalonType> getSalonTypesList();
    List<JobRole> createJobRoles(List<KeyValuePair> jobRoles);
    List<JobType> createJobTypes(List<KeyValueCategoryPair> jobTypes);
    List<SalonType> createSalonTypes(List<KeyValueCategoryPair> salonTypes);
}
