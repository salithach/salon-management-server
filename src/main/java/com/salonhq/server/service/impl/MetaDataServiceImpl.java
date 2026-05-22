package com.salonhq.server.service.impl;

import com.salonhq.server.dao.Role;
import com.salonhq.server.model.KeyValuePair;
import com.salonhq.server.repository.MetaDataRepository;
import com.salonhq.server.service.MetaDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetaDataServiceImpl implements MetaDataService {

    private final MetaDataRepository metaDataRepository;

    @Autowired
    public MetaDataServiceImpl(MetaDataRepository metaDataRepository) {
        this.metaDataRepository = metaDataRepository;
    }

    public List<Role> getJobRolesList() {
        return metaDataRepository.getJobRoles();
    }

    @Override
    public List<Role> createJobRoles(List<KeyValuePair> jobRoles) {
        return metaDataRepository.createJobRoles(jobRoles);
    }
}
