package com.salonhq.server.controller;

import com.salonhq.server.dao.SalonAppointment;
import com.salonhq.server.model.request.AppointmentRequest;
import com.salonhq.server.model.response.DeleteResponse;
import com.salonhq.server.model.response.EnvelopedResponse;
import com.salonhq.server.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAppointments(@RequestParam(value = "date", required = false) String appointmentDate) {
        List<SalonAppointment> appointmentsResponse = appointmentService.getAppointments(appointmentDate);
        EnvelopedResponse<Object> response = EnvelopedResponse.builder()
            .data(appointmentsResponse)
            .errors(List.of())
        .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("")
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentRequest appointmentRequest) {
        SalonAppointment appointmentResponse = appointmentService.createAppointment(appointmentRequest);
        EnvelopedResponse<Object> response = EnvelopedResponse.builder()
            .data(appointmentResponse)
            .errors(List.of())
        .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{appointmentId}")
    public ResponseEntity<?> editAppointment(
        @PathVariable String appointmentId,
        @RequestBody AppointmentRequest appointmentRequest
    ) {
        SalonAppointment appointmentResponse = appointmentService.editAppointment(appointmentId, appointmentRequest);
        EnvelopedResponse<Object> response = EnvelopedResponse.builder()
            .data(appointmentResponse)
            .errors(List.of())
        .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<?> deleteAppointment(@PathVariable String appointmentId) {
        DeleteResponse deleteResponse = appointmentService.deleteAppointment(appointmentId);
        EnvelopedResponse<Object> response = EnvelopedResponse.builder()
            .data(deleteResponse)
            .errors(List.of())
        .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
