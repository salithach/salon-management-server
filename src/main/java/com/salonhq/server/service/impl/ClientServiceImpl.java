package com.salonhq.server.service.impl;

import com.salonhq.server.dao.SalonClient;
import com.salonhq.server.model.request.appointments.Client;
import com.salonhq.server.repository.ClientRepository;
import com.salonhq.server.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public SalonClient saveClient(Client client) {
        return clientRepository.upsertClient(client);
    }

    @Override
    public List<SalonClient> getClients() {
        return clientRepository.getAllClients();
    }
}

