package com.salonhq.server.service.impl;

import com.mongodb.client.result.DeleteResult;
import com.salonhq.server.dao.SalonAppointment;
import com.salonhq.server.exception.AppointmentOperationException;
import com.salonhq.server.model.request.AppointmentRequest;
import com.salonhq.server.model.response.DeleteResponse;
import com.salonhq.server.repository.AppointmentRepository;
import com.salonhq.server.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public List<SalonAppointment> getAppointments(String appointmentDate) {
        if (appointmentDate == null || appointmentDate.isEmpty()) {
            appointmentDate = LocalDate.now().toString();
        }
        return appointmentRepository.getAllAppointments(appointmentDate);
    }

    @Override
    public SalonAppointment createAppointment(AppointmentRequest appointmentRequest) {
        return appointmentRepository.addAppointment(appointmentRequest);
    }

    @Override
    public SalonAppointment editAppointment(String id, AppointmentRequest appointmentRequest) {
        SalonAppointment existingAppointment = appointmentRepository.getAppointmentById(id);
        if (existingAppointment == null) {
            throw new AppointmentOperationException(String.format("Appointment not found for id: %s", id));
        }
        return appointmentRepository.updateAppointmentById(id, appointmentRequest);
    }

    @Override
    public DeleteResponse deleteAppointment(String id) {
        SalonAppointment existing = appointmentRepository.getAppointmentById(id);
        if (existing == null) {
            throw new AppointmentOperationException(String.format("Appointment not found for id: %s", id));
        }
        DeleteResult result = appointmentRepository.deleteAppointmentById(id);
        if (result.getDeletedCount() == 1) {
            return DeleteResponse.builder().message(String.format("Deleted appointment: %s", id)).build();
        } else {
            throw new AppointmentOperationException(String.format("Failed to delete appointment: %s", id));
        }
    }
}
