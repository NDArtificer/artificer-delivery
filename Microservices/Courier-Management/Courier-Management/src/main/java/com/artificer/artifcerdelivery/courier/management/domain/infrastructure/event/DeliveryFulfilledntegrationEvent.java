package com.artificer.artifcerdelivery.courier.management.domain.infrastructure.event;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class DeliveryFulfilledntegrationEvent {
    private OffsetDateTime ocurredAt;
    private UUID deliveryId;
}
