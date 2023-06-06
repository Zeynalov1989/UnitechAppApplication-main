package com.unibank.UnitechAppApplication.exception;

import java.io.Serial;

public class InvalidJwtAuthenticationException extends IllegalArgumentException {
    @Serial
    private static final long serialVersionUID = 1L;
    public InvalidJwtAuthenticationException(String message) {
        super(message);
    }
}
