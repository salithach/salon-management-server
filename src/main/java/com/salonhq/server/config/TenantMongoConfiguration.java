package com.salonhq.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;

public class TenantMongoConfiguration {

    @Bean
    public MongoTemplate mongoTemplate(MongoDatabaseFactory factory, MongoConverter converter) {
        return new TenantMongoTemplate(factory, converter);
    }

}
