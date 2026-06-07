package com.salonhq.server.repository;

import com.salonhq.server.dao.JobRole;
import com.salonhq.server.dao.JobType;
import com.salonhq.server.dao.SalonType;

import java.util.List;

public interface MetaDataRepository {
    List<JobRole> getJobRoles();
    List<JobType> getJobTypes();
    List<SalonType> getSalonTypes();
    List<JobRole> createJobRoles(List<JobRole> jobRoles);
    List<JobType> createJobTypes(List<JobType> jobTypes);
    List<SalonType> createSalonTypes(List<SalonType> salonTypes);
}
