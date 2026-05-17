package com.salonhq.server.service;

import com.salonhq.server.dao.DailyAssignment;

public interface AssignmentService {
    DailyAssignment getDailyAssignment(String date);
    DailyAssignment createDailyAssignment(DailyAssignment dailyAssignment);
    DailyAssignment unAssignStaffMember(DailyAssignment dailyAssignment);
}
