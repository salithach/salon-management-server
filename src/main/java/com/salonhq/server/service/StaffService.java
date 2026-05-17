package com.salonhq.server.service;

import com.salonhq.server.dao.StaffMember;
import com.salonhq.server.model.response.DeleteResponse;

import java.util.List;

public interface StaffService {
    StaffMember createStaff(StaffMember staffMember);
    void updateStaff();
    DeleteResponse deleteStaff(String identifier);
    List<StaffMember> getStaff();
}
