package com.artificer.artifcerdelivery.delivery.tracking.domain.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ContactPoint {
    private String zipCode;
    private String street;
    private String number;
    private String complement;
    private String name;
    private String phoneNumber;


    // Getters and setters can be added here if needed
}
