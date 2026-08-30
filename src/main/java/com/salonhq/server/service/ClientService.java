package com.salonhq.server.service;

import com.salonhq.server.dao.SalonClient;
import com.salonhq.server.model.request.appointments.Client;

import java.util.List;

public interface ClientService {
    SalonClient saveClient(Client client);
    List<SalonClient> getClients();
}

