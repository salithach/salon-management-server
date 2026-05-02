package com.salonhq.server.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CredentialException extends RuntimeException{
    public CredentialException(String message) {
        super(message);
    }
}
