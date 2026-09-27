package com.salonhq.server.controller;

import com.salonhq.server.dao.SalonClient;
import com.salonhq.server.model.response.EnvelopedResponse;
import com.salonhq.server.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("")
    public ResponseEntity<?> getClients() {
        List<SalonClient> clients = clientService.getClients();
        EnvelopedResponse<Object> response = EnvelopedResponse.builder()
            .data(clients)
            .errors(List.of())
        .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{clientId}")
    public ResponseEntity<?> deleteClientById(@PathVariable String clientId) {
        SalonClient client = clientService.deleteClientById(clientId);
        EnvelopedResponse<Object> response = EnvelopedResponse.builder()
            .data(client)
            .errors(List.of())
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

