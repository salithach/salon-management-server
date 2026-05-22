package com.salonhq.server.service;

import com.salonhq.server.dao.Role;
import com.salonhq.server.model.KeyValuePair;

import java.util.List;

public interface MetaDataService {
    List<Role> getJobRolesList();
    List<Role> createJobRoles(List<KeyValuePair> jobRole);
}
