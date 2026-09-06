package com.salonhq.server.model.request;

import com.salonhq.server.dao.User;
import com.salonhq.server.model.request.salon.SalonInformation;
import com.salonhq.server.model.request.salon.SalonLocation;
import com.salonhq.server.model.request.salon.SalonOwner;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class SalonRegistrationRequest extends UserRegistrationRequest {
    SalonOwner owner;
    SalonInformation salon;
    SalonLocation location;
    public User toUser() {
        String id = UUID.randomUUID().toString();
        return User.builder()
            .id(id)
            .owner(owner)
            .salon(salon)
            .location(location)
            .username(username)
            .email(email)
            .password(password)
        .build();
    }
}