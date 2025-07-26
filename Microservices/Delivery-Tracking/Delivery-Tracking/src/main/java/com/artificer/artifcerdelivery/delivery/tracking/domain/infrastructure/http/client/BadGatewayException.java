package com.artificer.artifcerdelivery.delivery.tracking.domain.infrastructure.http.client;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class BadGatewayException extends RuntimeException{
    public BadGatewayException() {
        super("Bad Gateway");
    }

    public BadGatewayException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadGatewayException(String message) {
        super(message);
    }
}
