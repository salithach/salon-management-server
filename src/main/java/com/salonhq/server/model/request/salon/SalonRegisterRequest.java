package com.salonhq.server.model.request.salon;

import com.salonhq.server.dao.User;
import com.salonhq.server.model.request.UserRegisterRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class SalonRegisterRequest extends UserRegisterRequest {
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