package com.salonhq.server.service.impl;

import com.salonhq.server.dao.SalonClient;
import com.salonhq.server.model.request.appointments.Client;
import com.salonhq.server.repository.ClientRepository;
import com.salonhq.server.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public SalonClient saveClient(Client client) {
        if (client.getId() == null || client.getId().isBlank()) {
            client.setId(UUID.randomUUID().toString());
        } else {
            SalonClient existingClient = clientRepository.getClientById(client.getId());
            if (existingClient != null) {
                Client clientToUpdate = Client.builder()
                    .id(existingClient.getId())
                    .name(client.getName())
                    .phone(client.getPhone())
                    .email(client.getEmail())
                .build();
                return clientRepository.upsertClient(clientToUpdate);
            }
        }
        return clientRepository.upsertClient(client);
    }

    @Override
    public List<SalonClient> getClients() {
        return clientRepository.getAllClients();
    }

    @Override
    public SalonClient getClientById(String id) {
        return clientRepository.getClientById(id);
    }
}

