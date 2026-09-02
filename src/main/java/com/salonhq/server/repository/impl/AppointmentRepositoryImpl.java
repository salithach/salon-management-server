package com.salonhq.server.repository.impl;

import com.mongodb.client.result.DeleteResult;
import com.salonhq.server.dao.SalonAppointment;
import com.salonhq.server.model.request.AppointmentRequest;
import com.salonhq.server.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class AppointmentRepositoryImpl implements AppointmentRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    AppointmentRepositoryImpl(@Qualifier("tenantMongoTemplate") MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<SalonAppointment> getAllAppointments(String appointmentDate) {
        return mongoTemplate.find(
            Query.query(
                Criteria.where("date").is(appointmentDate)
            ),
            SalonAppointment.class
        );
    }

    @Override
    public SalonAppointment getAppointmentById(String id) {
        return mongoTemplate.findById(id, SalonAppointment.class);
    }

    @Override
    public SalonAppointment addAppointment(AppointmentRequest appointment) {
        SalonAppointment salonAppointment = new SalonAppointment();
        String appointmentId = UUID.randomUUID().toString();
        salonAppointment.setId(appointmentId);
        salonAppointment.setClient(appointment.getClient());
        salonAppointment.setDate(appointment.getDate());
        salonAppointment.setTime(appointment.getTime());
        salonAppointment.setServices(appointment.getServices());
        salonAppointment.setAssignee(appointment.getAssignee());
        salonAppointment.setStatus(appointment.getStatus());
        salonAppointment.setNotes(appointment.getNotes());
        return mongoTemplate.save(salonAppointment);
    }

    @Override
    public SalonAppointment updateAppointmentById(String id, AppointmentRequest appointment) {
        SalonAppointment existing = mongoTemplate.findById(id, SalonAppointment.class);
        if (existing == null) return null;
        existing.setClient(appointment.getClient() != null ? appointment.getClient() : existing.getClient());
        existing.setDate(appointment.getDate() != null ? appointment.getDate() : existing.getDate());
        existing.setTime(appointment.getTime() != null ? appointment.getTime() : existing.getTime());
        existing.setServices(appointment.getServices() != null ? appointment.getServices() : existing.getServices());
        existing.setAssignee(appointment.getAssignee() != null ? appointment.getAssignee() : existing.getAssignee());
        existing.setStatus(appointment.getStatus() != null ? appointment.getStatus() : existing.getStatus());
        existing.setNotes(appointment.getNotes() != null ? appointment.getNotes() : existing.getNotes());
        return mongoTemplate.save(existing);
    }

    @Override
    public DeleteResult deleteAppointmentById(String id) {
        Query query = Query.query(Criteria.where("id").is(id));
        return mongoTemplate.remove(query, SalonAppointment.class);
    }
}
