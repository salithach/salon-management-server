package com.salonhq.server.controller;

import com.salonhq.server.dao.DailyAssignment;
import com.salonhq.server.model.response.EnvelopedResponse;
import com.salonhq.server.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    @Autowired
    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAssignment(@RequestParam("date") String date) {
        DailyAssignment dailyAssignment = assignmentService.getDailyAssignment(date);
        EnvelopedResponse<Object> response = EnvelopedResponse.builder()
            .data(dailyAssignment)
            .errors(List.of())
        .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/assign")
    public ResponseEntity<?> addAssignment(@RequestBody DailyAssignment dailyAssignmentRequest) {
        DailyAssignment dailyAssignment = assignmentService.createDailyAssignment(dailyAssignmentRequest);
        EnvelopedResponse<Object> response = EnvelopedResponse.builder()
            .data(dailyAssignment)
            .errors(List.of())
        .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/unassign")
    public ResponseEntity<?> removeAssignment(@RequestBody DailyAssignment unAssignmentRequest) {
        DailyAssignment dailyAssignment = assignmentService.unAssignStaffMember(unAssignmentRequest);
        EnvelopedResponse<Object> response = EnvelopedResponse.builder()
            .data(dailyAssignment)
            .errors(List.of())
        .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
