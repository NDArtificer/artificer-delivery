package com.artificer.artifcerdelivery.delivery.tracking.domain.model;

import java.util.List;

public enum DeliveryStatus {
    DRAFT,
    CANCELLED(DRAFT),
    WAITING_FOR_COURIER(DRAFT),
    IN_TRANSIT(WAITING_FOR_COURIER),
    DELIVERED(IN_TRANSIT);

    private final List<DeliveryStatus> previousStatus;

    DeliveryStatus(DeliveryStatus... previousStatus) {
        this.previousStatus = List.of(previousStatus);
    }

    public boolean canNotChangeTo(DeliveryStatus status) {
        return this.previousStatus.contains(status);
    }

    public boolean canChangeTo(DeliveryStatus status) {
        return !canNotChangeTo(status);
    }
}
