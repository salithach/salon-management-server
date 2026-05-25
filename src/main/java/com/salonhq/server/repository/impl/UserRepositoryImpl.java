package com.salonhq.server.repository.impl;

import com.salonhq.server.dao.User;
import com.salonhq.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.salonhq.server.util.Constants.EMAIL;
import static com.salonhq.server.util.Constants.USERNAME;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public UserRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Query query = Query.query(Criteria.where(USERNAME).is(username));
        return Optional.ofNullable(mongoTemplate.findOne(query, User.class));
    }

    @Override
    public Optional<User> findByUserEmail(String email) {
        Query query = Query.query(Criteria.where(EMAIL).is(email));
        return Optional.ofNullable(mongoTemplate.findOne(query, User.class));
    }

    @Override
    public User save(User user) {
        return mongoTemplate.save(user);
    }
}
