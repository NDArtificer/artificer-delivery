package com.artificer.artifcerdelivery.delivery.tracking.domain.service;

import com.artificer.artifcerdelivery.delivery.tracking.domain.model.ContactPoint;

public interface DeliveryTimeEstimationService {
    DeliveryEstimate estimate(ContactPoint sender, ContactPoint receiver);
}
