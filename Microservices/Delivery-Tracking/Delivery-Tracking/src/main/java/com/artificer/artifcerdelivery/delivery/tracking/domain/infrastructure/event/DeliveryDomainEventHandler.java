package com.artificer.artifcerdelivery.delivery.tracking.domain.infrastructure.event;

import com.artificer.artifcerdelivery.delivery.tracking.domain.event.DeliveryFulfilledEvent;
import com.artificer.artifcerdelivery.delivery.tracking.domain.event.DeliveryPickedupEvent;
import com.artificer.artifcerdelivery.delivery.tracking.domain.event.DeliveryPlacedEvent;
import com.artificer.artifcerdelivery.delivery.tracking.domain.infrastructure.kafka.IntegrationEventPublisherKafkaImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.artificer.artifcerdelivery.delivery.tracking.domain.infrastructure.kafka.KafkaTopicConfig.deliveryEventsTopicName;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryDomainEventHandler {

    private final IntegrationEventPublisherKafkaImpl integrationEventPublisher;

    @EventListener
    public void handle(DeliveryPlacedEvent event) {
        log.info("Handling DeliveryPlacedEvent: {}", event.toString());
        integrationEventPublisher.publish(event, event.getDeliveryId().toString(), deliveryEventsTopicName);
    }

    @EventListener
    public void handle(DeliveryPickedupEvent event) {
        log.info("Handling DeliveryPickedupEvent: {}", event.toString());
        integrationEventPublisher.publish(event, event.getDeliveryId().toString(), deliveryEventsTopicName);

    }

    @EventListener
    public void handle(DeliveryFulfilledEvent event) {
        log.info("Handling DeliveryFulfilledEvent: {}", event.toString());
        integrationEventPublisher.publish(event, event.getDeliveryId().toString(), deliveryEventsTopicName);
    }
}
