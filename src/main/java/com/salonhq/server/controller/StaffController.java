package com.salonhq.server.controller;

import com.salonhq.server.dao.StaffMember;
import com.salonhq.server.model.response.DeleteResponse;
import com.salonhq.server.model.response.EnvelopedResponse;
import com.salonhq.server.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.salonhq.server.util.AuthUtil.applyTenantOverride;

@RestController
@RequestMapping("/api/v1/staff")
public class StaffController {

    private final StaffService staffService;

    @Autowired
    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping("")
    public ResponseEntity<?> getStaffMembers(
        @RequestHeader(value = "X-Tenant-Id", required = false) String targetTenantId
    ) {
        applyTenantOverride(targetTenantId);
        List<StaffMember> staffMembers = staffService.getStaff();
        EnvelopedResponse<Object> response = EnvelopedResponse.builder()
            .data(staffMembers)
            .errors(List.of())
        .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("")
    public ResponseEntity<?> addStaffMember(@RequestBody StaffMember staffMemberRequest) {
        StaffMember staffMember = staffService.createStaff(staffMemberRequest);
        EnvelopedResponse<Object> response = EnvelopedResponse.builder()
            .data(staffMember)
            .errors(List.of())
        .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<?> removeStaffMember(@PathVariable String username) {
        DeleteResponse deleteResponse = staffService.deleteStaff(username);
        EnvelopedResponse<Object> response = EnvelopedResponse.builder()
            .data(deleteResponse)
            .errors(List.of())
        .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
