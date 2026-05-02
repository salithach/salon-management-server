package com.salonhq.server.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import static com.salonhq.server.util.Constants.LOCAL_ENV;

@Configuration
@Slf4j
public class MongoConfiguration {

    @Value("${salonhq.mongo.db.cluster}")
    private String databaseCluster;

    @Value("${salonhq.mongo.db.name}")
    private String databaseName;

    @Value("${salonhq.mongo.db.user}")
    private String databaseUser;

    @Value("${salonhq.mongo.db.password}")
    private String databasePassword;

    @Value("${salonhq.mongo.db.appName}")
    private String dbAppName;

    @Value("${salonhq.env}")
    private String environment;

    @Value("${salonhq.mongo.local.url}")
    private String localMongoUrl;

    private String getConnectionString() {
        if (environment.equalsIgnoreCase(LOCAL_ENV)) {
            return localMongoUrl;
        }
        String encodedUser = URLEncoder.encode(databaseUser, StandardCharsets.UTF_8);
        String encodedPassword = URLEncoder.encode(databasePassword, StandardCharsets.UTF_8);
        return String.format(
            "mongodb+srv://%s:%s@%s/?appName=%s",
            encodedUser,
            encodedPassword,
            databaseCluster,
            dbAppName
        );
    }

    @Bean
    public MongoClient mongoClient() {
        final ConnectionString connectionString = new ConnectionString(getConnectionString());
        CodecRegistry registry = CodecRegistries.fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(),
            CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
        );
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .codecRegistry(registry)
        .build();
        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoDatabaseFactory mongoDbFactory() {
        return new SimpleMongoClientDatabaseFactory(mongoClient(), databaseName);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoDbFactory());
    }

}
