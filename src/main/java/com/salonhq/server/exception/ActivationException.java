package com.salonhq.server.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ActivationException extends RuntimeException {
    public ActivationException(String message) {
        super(message);
    }
}
