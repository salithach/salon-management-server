package com.salonhq.server.service.impl;

import com.salonhq.server.dao.StaffMember;
import com.salonhq.server.repository.StaffRepository;
import com.salonhq.server.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.salonhq.server.model.ErrorMessage.USER_EMAIL_TAKEN;
import static com.salonhq.server.model.ErrorMessage.USER_NAME_TAKEN;

@Service
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;

    @Autowired
    public StaffServiceImpl(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
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
    public void deleteStaff() {

    }

    @Override
    public List<StaffMember> getStaff() {
        return staffRepository.retrieveStaff();
    }
}
