package com.salonhq.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;

@Configuration
public class TenantMongoConfiguration {

    @Bean
    public MongoTemplate tenantMongoTemplate(MongoDatabaseFactory factory, MongoConverter converter) {
        return new TenantMongoTemplate(factory, converter);
    }

}
