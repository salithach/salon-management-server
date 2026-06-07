package com.salonhq.server.service.impl;

import com.salonhq.server.dao.JobRole;
import com.salonhq.server.dao.JobType;
import com.salonhq.server.dao.SalonType;
import com.salonhq.server.model.KeyValueCategoryPair;
import com.salonhq.server.model.KeyValuePair;
import com.salonhq.server.repository.MetaDataRepository;
import com.salonhq.server.service.MetaDataService;
import com.salonhq.server.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MetaDataServiceImpl implements MetaDataService {

    private final MetaDataRepository metaDataRepository;

    @Autowired
    public MetaDataServiceImpl(MetaDataRepository metaDataRepository) {
        this.metaDataRepository = metaDataRepository;
    }

    public List<JobRole> getJobRolesList() {
        return metaDataRepository.getJobRoles();
    }

    @Override
    public List<JobType> getJobTypesList() {
        return metaDataRepository.getJobTypes();
    }

    @Override
    public List<SalonType> getSalonTypesList() {
        return metaDataRepository.getSalonTypes();
    }

    @Override
    public List<JobRole> createJobRoles(List<KeyValuePair> jobRoleRequest) {
        List<JobRole> jobRoles = CommonUtil.mapToList(jobRoleRequest, r -> JobRole.builder()
            .id(UUID.randomUUID().toString())
            .key(r.getKey())
            .value(r.getValue())
            .build()
        );
        return metaDataRepository.createJobRoles(jobRoles);
    }

    @Override
    public List<JobType> createJobTypes(List<KeyValueCategoryPair> jobTypeRequest) {
        List<JobType> jobTypes = CommonUtil.mapToList(jobTypeRequest, r -> JobType.builder()
            .id(UUID.randomUUID().toString())
            .key(r.getKey())
            .value(r.getValue())
            .category(r.getCategory())
            .build()
        );
        return metaDataRepository.createJobTypes(jobTypes);
    }

    @Override
    public List<SalonType> createSalonTypes(List<KeyValueCategoryPair> salonTypeRequest) {
        List<SalonType> salonTypes = CommonUtil.mapToList(salonTypeRequest, r -> SalonType.builder()
            .id(UUID.randomUUID().toString())
            .key(r.getKey())
            .value(r.getValue())
            .category(r.getCategory())
            .build()
        );
        return metaDataRepository.createSalonTypes(salonTypes);
    }
}
