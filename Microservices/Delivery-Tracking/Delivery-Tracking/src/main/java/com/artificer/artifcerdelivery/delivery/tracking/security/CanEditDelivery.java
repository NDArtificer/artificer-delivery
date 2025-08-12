package com.artificer.artifcerdelivery.delivery.tracking.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAnyRole('ADMIN','CLIENT') and hasAuthority('SCOPE_deliveries:write')")
public @interface CanEditDelivery {
    // This annotation can be used to secure methods that allow editing of delivery tracking information
}
