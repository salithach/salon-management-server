package com.salonhq.server.repository;

import com.salonhq.server.dao.DailyAssignment;

import java.util.Optional;

public interface AssignmentRepository {
    Optional<DailyAssignment> retrieveDailyAssignment(String date);
    DailyAssignment addDailyAssignment(DailyAssignment dailyAssignment);
}
