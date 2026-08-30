package com.salonhq.server.dao;

import com.salonhq.server.model.request.appointments.Client;
import com.salonhq.server.model.tenant.TenantEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "appointments")
public class SalonAppointment extends TenantEntity {
    @Id
    String id;
    Client client;
    String date;
    String time;
    List<String> services;
    String assignee;
    String status;
    String notes;
}
