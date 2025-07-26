package com.artificer.artifcerdelivery.delivery.tracking.domain.infrastructure.http.client;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
public class GatewayTimeOutException extends RuntimeException{
    public GatewayTimeOutException() {
        super("Gateway Timeout");
    }

    public GatewayTimeOutException(String message, Throwable cause) {
        super(message, cause);
    }

    public GatewayTimeOutException(String message) {
        super(message);
    }
}
