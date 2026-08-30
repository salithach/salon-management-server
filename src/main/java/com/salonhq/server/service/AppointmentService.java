package com.salonhq.server.service;

import com.salonhq.server.dao.SalonAppointment;
import com.salonhq.server.model.request.AppointmentRequest;

import java.util.List;

public interface AppointmentService {
    List<SalonAppointment> getAppointments(String appointmentDate);
    SalonAppointment createAppointment(AppointmentRequest appointmentRequest);
}
