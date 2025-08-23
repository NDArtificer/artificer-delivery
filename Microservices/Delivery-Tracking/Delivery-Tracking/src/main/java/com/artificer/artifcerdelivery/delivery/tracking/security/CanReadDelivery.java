package com.artificer.artifcerdelivery.delivery.tracking.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAnyRole('ADMIN','CLIENT') or hasAuthority('SCOPE_deliveries:read')")
public @interface CanReadDelivery {
    // This annotation can be used to secure methods that allow reading of delivery tracking information
}
