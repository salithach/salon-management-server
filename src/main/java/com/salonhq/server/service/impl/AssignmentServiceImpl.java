package com.salonhq.server.service.impl;

import com.mongodb.client.result.DeleteResult;
import com.salonhq.server.dao.DailyAssignment;
import com.salonhq.server.dao.StaffMember;
import com.salonhq.server.exception.StaffOperationException;
import com.salonhq.server.model.response.DeleteResponse;
import com.salonhq.server.repository.AssignmentRepository;
import com.salonhq.server.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;

    @Autowired
    public AssignmentServiceImpl(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    @Override
    public DailyAssignment getDailyAssignment(String date) {
        return assignmentRepository.retrieveDailyAssignment(date).orElse(null);
    }

    @Override
    public DailyAssignment createDailyAssignment(DailyAssignment dailyAssignment) {
        Optional<DailyAssignment> existingDailyAssignment = assignmentRepository.retrieveDailyAssignment(dailyAssignment.getDate());
        if (existingDailyAssignment.isPresent()) {
            DailyAssignment existingAssignment = existingDailyAssignment.get();
            if (existingAssignment.getMembers().isEmpty()) {
                existingAssignment.setMembers(dailyAssignment.getMembers());
            } else {
                List<StaffMember> existingAssignees = existingAssignment.getMembers();
                dailyAssignment.getMembers().forEach(member -> {
                    boolean alreadyExists = existingAssignees.stream()
                            .anyMatch(e -> e.getId().equals(member.getId()));
                    if (!alreadyExists) {
                        existingAssignees.add(member);
                    }
                });
                existingAssignment.setMembers(existingAssignees);
            }
            return assignmentRepository.addDailyAssignment(existingAssignment);
        } else {
            if (dailyAssignment.getMembers().isEmpty()) {
                throw new StaffOperationException("No staff members found in assignment");
            } else {
                dailyAssignment.setId(UUID.randomUUID().toString());
                dailyAssignment.setDate(LocalDate.now().toString());
                return assignmentRepository.addDailyAssignment(dailyAssignment);
            }
        }
    }

    @Override
    public DailyAssignment unAssignStaffMember(DailyAssignment unAssignment) {
        Optional<DailyAssignment> existingDailyAssignment = assignmentRepository.retrieveDailyAssignment(unAssignment.getDate());
        if (existingDailyAssignment.isPresent()) {
            DailyAssignment existingAssignment = existingDailyAssignment.get();
            if (existingAssignment.getMembers().isEmpty()) {
                throw new StaffOperationException("Assignment has no members in staff member");
            } else {
                List<StaffMember> existingAssignees = existingAssignment.getMembers();
                existingAssignees.removeIf(member -> unAssignment.getMembers().stream()
                        .anyMatch(e -> e.getId().equals(member.getId())));
                existingAssignment.setMembers(existingAssignees);
                return assignmentRepository.addDailyAssignment(existingAssignment);
            }
        }
        throw new StaffOperationException(String.format("No assignment found for date: %s", unAssignment.getDate()));
    }

    @Override
    public DeleteResponse resetDailyAssignment(String id, String date) {
        DeleteResult deleteAssignmentResult = assignmentRepository.removeDailyAssignment(id);
        if (deleteAssignmentResult.getDeletedCount() == 1) {
            return DeleteResponse.builder().message(String.format("Deleted assignment for: %s", date)).build();
        } else {
            throw new RuntimeException(String.format("Failed to delete daily assignment: %s", date));
        }
    }
}
