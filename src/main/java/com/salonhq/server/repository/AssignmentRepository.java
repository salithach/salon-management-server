package com.salonhq.server.repository;

import com.mongodb.client.result.DeleteResult;
import com.salonhq.server.dao.DailyAssignment;
import com.salonhq.server.dao.StaffMember;

import java.util.List;
import java.util.Optional;

public interface AssignmentRepository {
    Optional<DailyAssignment> retrieveDailyAssignment(String date);
    DailyAssignment addDailyAssignment(DailyAssignment dailyAssignment);
}
