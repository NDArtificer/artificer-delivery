package com.artificer.artifcerdelivery.delivery.tracking.domain.infrastructure.fake;

import com.artificer.artifcerdelivery.delivery.tracking.domain.model.ContactPoint;
import com.artificer.artifcerdelivery.delivery.tracking.domain.service.DeliveryEstimate;
import com.artificer.artifcerdelivery.delivery.tracking.domain.service.DeliveryTimeEstimationService;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class DeliveryTimeEstimationServiceFakeImpl implements DeliveryTimeEstimationService {

    @Override
    public DeliveryEstimate estimate(ContactPoint sender, ContactPoint receiver) {
        // Simulate a fake delivery time estimation based on distance
        // For example, assume 1 km takes 2 minutes to deliver
        return new DeliveryEstimate(Duration.ofHours(3), 3.15);
    }
}
