package com.salonhq.server.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import static com.salonhq.server.util.Constants.LOCAL_ENV;

@Configuration
@Slf4j
public class MongoConfiguration extends AbstractMongoClientConfiguration {

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

    @Override
    protected @NonNull String getDatabaseName() {
        return databaseName;
    }

    private String getConnectionString() {
        if (environment.equalsIgnoreCase(LOCAL_ENV)) {
            log.info("[MongoDB] Environment: LOCAL — connecting to {}", localMongoUrl);
            return localMongoUrl;
        }
        log.info("[MongoDB] Environment: {} — connecting to Atlas cluster: {}", environment, databaseCluster);
        String encodedUser = URLEncoder.encode(databaseUser, StandardCharsets.UTF_8);
        String encodedPassword = URLEncoder.encode(databasePassword, StandardCharsets.UTF_8);
        return String.format(
            "mongodb+srv://%s:%s@%s/%s?authSource=admin&retryWrites=true&w=majority&appName=%s",
            encodedUser,
            encodedPassword,
            databaseCluster,
            databaseName,
            dbAppName
        );
    }

    @Override
    public @NonNull MongoClient mongoClient() {
        MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(new ConnectionString(getConnectionString()))
            .build();
        return MongoClients.create(settings);
    }
}
