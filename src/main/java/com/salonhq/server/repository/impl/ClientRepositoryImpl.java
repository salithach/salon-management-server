package com.salonhq.server.repository.impl;

import com.salonhq.server.dao.SalonClient;
import com.salonhq.server.model.request.appointments.Client;
import com.salonhq.server.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class ClientRepositoryImpl implements ClientRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ClientRepositoryImpl(@Qualifier("tenantMongoTemplate") MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public SalonClient upsertClient(Client client) {
        SalonClient existing = findExistingClient(client);

        if (existing != null) {
            Query matchQuery = Query.query(Criteria.where("id").is(existing.getId()));
            Update update = new Update()
                .set("name", client.getName())
                .set("phone", client.getPhone())
                .set("email", client.getEmail());
            mongoTemplate.updateFirst(matchQuery, update, SalonClient.class);
            existing.setName(client.getName());
            existing.setPhone(client.getPhone());
            existing.setEmail(client.getEmail());
            return existing;
        } else {
            SalonClient newClient = SalonClient.builder()
                .id(UUID.randomUUID().toString())
                .name(client.getName())
                .phone(client.getPhone())
                .email(client.getEmail())
                .build();
            return mongoTemplate.save(newClient);
        }
    }

    private SalonClient findExistingClient(Client client) {
        // Search priority: name → phone → email
        if (client.getName() != null && !client.getName().isBlank()) {
            SalonClient byName = mongoTemplate.findOne(
                Query.query(Criteria.where("name").is(client.getName())), SalonClient.class);
            if (byName != null) return byName;
        }
        if (client.getPhone() != null && !client.getPhone().isBlank()) {
            SalonClient byPhone = mongoTemplate.findOne(
                Query.query(Criteria.where("phone").is(client.getPhone())), SalonClient.class);
            if (byPhone != null) return byPhone;
        }
        if (client.getEmail() != null && !client.getEmail().isBlank()) {
            return mongoTemplate.findOne(
                Query.query(Criteria.where("email").is(client.getEmail())), SalonClient.class);
        }
        return null;
    }

    @Override
    public List<SalonClient> getAllClients() {
        return mongoTemplate.find(new Query(), SalonClient.class);
    }
}

