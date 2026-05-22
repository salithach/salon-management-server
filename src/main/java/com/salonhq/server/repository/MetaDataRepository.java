package com.salonhq.server.repository;

import com.salonhq.server.dao.Role;
import com.salonhq.server.model.KeyValuePair;

import java.util.List;

public interface MetaDataRepository {
    List<Role> getJobRoles();
    List<Role> createJobRoles(List<KeyValuePair> jobRoleRequest);
}
