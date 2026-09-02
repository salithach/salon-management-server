package com.salonhq.server.service;

import com.salonhq.server.dao.SalonAppointment;
import com.salonhq.server.model.request.AppointmentRequest;
import com.salonhq.server.model.response.DeleteResponse;

import java.util.List;

public interface AppointmentService {
    List<SalonAppointment> getAppointments(String appointmentDate);
    SalonAppointment createAppointment(AppointmentRequest appointmentRequest);
    SalonAppointment editAppointment(String id, AppointmentRequest appointmentRequest);
    DeleteResponse deleteAppointment(String id);
}
