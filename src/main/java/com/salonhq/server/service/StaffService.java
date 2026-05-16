package com.salonhq.server.service;

import com.salonhq.server.dao.StaffMember;

import java.util.List;

public interface StaffService {
    StaffMember createStaff(StaffMember staffMember);
    void updateStaff();
    void deleteStaff();
    List<StaffMember> getStaff();
}
