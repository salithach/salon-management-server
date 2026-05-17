package com.salonhq.server.service;

import com.salonhq.server.dao.DailyAssignment;
import com.salonhq.server.dao.StaffMember;
import com.salonhq.server.model.response.DeleteResponse;

import java.util.List;

public interface AssignmentService {
    DailyAssignment getDailyAssignment(String date);
    DailyAssignment createDailyAssignment(DailyAssignment dailyAssignment);
    DailyAssignment unAssignStaffMember(DailyAssignment dailyAssignment);
}
