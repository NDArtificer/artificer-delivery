package com.artificer.artifcerdelivery.delivery.tracking.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class DeliveryFulfilledEvent {
    private OffsetDateTime ocurrendAt;
    private UUID deliveryId;
}
