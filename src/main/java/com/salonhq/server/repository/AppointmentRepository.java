package com.salonhq.server.repository;


import com.mongodb.client.result.DeleteResult;
import com.salonhq.server.dao.SalonAppointment;
import com.salonhq.server.model.request.AppointmentRequest;

import java.util.List;

public interface AppointmentRepository {
    List<SalonAppointment> getAllAppointments(String appointmentDate);
    SalonAppointment getAppointmentById(String id);
    SalonAppointment addAppointment(AppointmentRequest appointment);
    SalonAppointment updateAppointmentById(String id, AppointmentRequest appointment);
    DeleteResult deleteAppointmentById(String id);
}
