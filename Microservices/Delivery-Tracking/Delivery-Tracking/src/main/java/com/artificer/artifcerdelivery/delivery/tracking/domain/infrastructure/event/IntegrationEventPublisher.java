package com.artificer.artifcerdelivery.delivery.tracking.domain.infrastructure.event;

public interface IntegrationEventPublisher {
    void publish(Object event, String key,String topic);
}
