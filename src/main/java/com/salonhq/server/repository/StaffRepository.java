package com.salonhq.server.repository;

import com.salonhq.server.dao.StaffMember;

import java.util.List;
import java.util.Optional;

public interface StaffRepository {
    StaffMember addStaff(StaffMember staffMember);
    Optional<StaffMember> getByUsername(String username);
    Optional<StaffMember> getByEmail(String email);
    void removeStaff();
    void modifyStaff();
    List<StaffMember> retrieveStaff();
}
