package com.salonhq.server.model;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    USER_NAME_TAKEN("Username already taken"),
    USER_EMAIL_TAKEN("Email already taken"),
    USER_NOT_FOUND("User not found"),
    INVALID_USERNAME( "Username is invalid"),
    USER_NOT_AUTHORIZED("User is not authorized");
    private final String value;
    ErrorMessage(String value) {
        this.value = value;
    }

}
