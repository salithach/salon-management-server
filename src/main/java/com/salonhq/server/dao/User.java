package com.salonhq.server.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.salonhq.server.model.request.salon.SalonInformation;
import com.salonhq.server.model.request.salon.SalonLocation;
import com.salonhq.server.model.request.salon.SalonOwner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {
    @Id
    String id;
    String name;
    String email;
    String username;
    @JsonIgnore
    String password;
    @Builder.Default
    Set<Role> roles = new HashSet<>();
    SalonOwner owner;
    SalonInformation salon;
    SalonLocation location;
}
