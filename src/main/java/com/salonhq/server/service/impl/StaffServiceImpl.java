package com.salonhq.server.service.impl;

import com.mongodb.client.result.DeleteResult;
import com.salonhq.server.dao.DailyAssignment;
import com.salonhq.server.dao.StaffMember;
import com.salonhq.server.exception.StaffOperationException;
import com.salonhq.server.model.response.DeleteResponse;
import com.salonhq.server.repository.StaffRepository;
import com.salonhq.server.service.AssignmentService;
import com.salonhq.server.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.salonhq.server.model.ErrorMessage.USER_EMAIL_TAKEN;
import static com.salonhq.server.model.ErrorMessage.USER_NAME_TAKEN;

@Service
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final AssignmentService assignmentService;

    @Autowired
    public StaffServiceImpl(StaffRepository staffRepository, AssignmentService assignmentService) {
        this.staffRepository = staffRepository;
        this.assignmentService = assignmentService;
    }

    @Override
    public StaffMember createStaff(StaffMember staffMember) {
        Optional<StaffMember> staffMemberByUsername = staffRepository.getByUsername(staffMember.getUsername());
        Optional<StaffMember> staffMemberByEmail = staffRepository.getByEmail(staffMember.getEmail());
        if (staffMemberByUsername.isPresent()) {
            throw new RuntimeException(USER_NAME_TAKEN.getValue());
        } else if (staffMemberByEmail.isPresent()) {
            throw new RuntimeException(USER_EMAIL_TAKEN.getValue());
        } else {
            return staffRepository.addStaff(staffMember);
        }
    }

    @Override
    public void updateStaff() {

    }

    @Override
    public DeleteResponse deleteStaff(String username) {
        Optional<StaffMember> existing = staffRepository.getByUsername(username);
        if (existing.isEmpty()) {
            throw new StaffOperationException(String.format("Staff member not found for identifier: %s", username));
        }
        StaffMember staffMember = existing.get();
        DeleteResult deleteStaffResult = staffRepository.removeStaff(username);
        DailyAssignment unAssignedStaffMember = assignmentService.unAssignStaffMember(
            DailyAssignment.builder()
                .date(LocalDate.now().toString())
                .members(List.of(staffMember))
            .build()
        );
        boolean matchFromUnassignment = unAssignedStaffMember.getMembers().stream()
                .anyMatch(m -> m.getId().equals(staffMember.getId()));
        if (deleteStaffResult.getDeletedCount() == 1 && !matchFromUnassignment) {
            return DeleteResponse.builder().message(String.format("Deleted: %s from staff", username)).build();
        } else {
            throw new StaffOperationException(String.format("Failed to delete staff member: %s", username));
        }
    }

    @Override
    public List<StaffMember> getStaff() {
        return staffRepository.retrieveStaff();
    }

}
