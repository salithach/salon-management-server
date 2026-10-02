package com.salonhq.server.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.salonhq.server.model.request.salon.SalonInformation;
import com.salonhq.server.model.request.salon.SalonLocation;
import com.salonhq.server.model.request.salon.SalonOwner;
import com.salonhq.server.model.tenant.TenantEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User extends TenantEntity {
    @Id
    String id;
    String email;
    String username;
    @JsonIgnore
    String password;
    @Builder.Default
    Set<Role> roles = new HashSet<>();
    SalonOwner owner;
    SalonInformation salon;
    SalonLocation location;
    @Builder.Default
    boolean isActive = false;
}
