package com.salonhq.server.model.request;

import com.salonhq.server.model.request.appointments.Client;
import lombok.Data;

import java.util.List;

@Data
public class AppointmentRequest {
    Client client;
    String date;
    String time;
    List<String> services;
    String assignee;
    String status;
    String notes;
}
