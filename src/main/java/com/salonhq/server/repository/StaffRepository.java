package com.salonhq.server.repository;

import com.mongodb.client.result.DeleteResult;
import com.salonhq.server.dao.DailyAssignment;
import com.salonhq.server.dao.StaffMember;

import java.util.List;
import java.util.Optional;

public interface StaffRepository {
    StaffMember addStaff(StaffMember staffMember);
    Optional<StaffMember> getByUsername(String username);
    Optional<StaffMember> getByEmail(String email);
    DeleteResult removeStaff(String identifier);
    Optional<StaffMember> getById(String id);
    void modifyStaff();
    List<StaffMember> retrieveStaff();
}
