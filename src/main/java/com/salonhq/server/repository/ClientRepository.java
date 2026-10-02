package com.salonhq.server.repository;

import com.salonhq.server.dao.SalonClient;
import com.salonhq.server.model.request.appointments.Client;

import java.util.List;

public interface ClientRepository {
    SalonClient upsertClient(Client client);
    List<SalonClient> getAllClients();
    List<SalonClient> getClientsCreatedBetween(String startDate, String endDate);
    SalonClient getClientById(String id);
    SalonClient deleteClientById(String id);
}

