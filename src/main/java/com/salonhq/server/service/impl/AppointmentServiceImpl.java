package com.salonhq.server.service.impl;

import com.salonhq.server.dao.SalonAppointment;
import com.salonhq.server.model.request.AppointmentRequest;
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
}
