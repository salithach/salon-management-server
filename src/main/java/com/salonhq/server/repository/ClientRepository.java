package com.salonhq.server.repository;

import com.salonhq.server.dao.SalonClient;
import com.salonhq.server.model.request.appointments.Client;

import java.util.List;

public interface ClientRepository {
    SalonClient upsertClient(Client client);
    List<SalonClient> getAllClients();
}

