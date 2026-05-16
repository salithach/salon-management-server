package com.salonhq.server.repository.impl;

import com.salonhq.server.dao.StaffMember;
import com.salonhq.server.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.salonhq.server.util.Constants.EMAIL;
import static com.salonhq.server.util.Constants.USERNAME;

@Repository
public class StaffRepositoryImpl implements StaffRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public StaffRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public StaffMember addStaff(StaffMember staffMember) {
        String id = UUID.randomUUID().toString();
        staffMember.setId(id);
        return mongoTemplate.save(staffMember);
    }

    @Override
    public Optional<StaffMember> getByUsername(String username) {
        Query query = Query.query(Criteria.where(USERNAME).is(username));
        return Optional.ofNullable(mongoTemplate.findOne(query, StaffMember.class));
    }

    @Override
    public Optional<StaffMember> getByEmail(String email) {
        Query query = Query.query(Criteria.where(EMAIL).is(email));
        return Optional.ofNullable(mongoTemplate.findOne(query, StaffMember.class));
    }

    @Override
    public void removeStaff() {

    }

    @Override
    public void modifyStaff() {

    }

    @Override
    public List<StaffMember> retrieveStaff() {
        return mongoTemplate.find(new Query(), StaffMember.class);
    }
}
