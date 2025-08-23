package com.artificer.artifcerdelivery.courier.management.domain.infrastructure.kafka;

import com.artificer.artifcerdelivery.courier.management.domain.infrastructure.event.DeliveryCancellationIntegrationEvent;
import com.artificer.artifcerdelivery.courier.management.domain.infrastructure.event.DeliveryFulfilledntegrationEvent;
import com.artificer.artifcerdelivery.courier.management.domain.infrastructure.event.DeliveryPlacedIntegrationEvent;
import com.artificer.artifcerdelivery.courier.management.domain.service.CourierDeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@KafkaListener(topics = {"deliveries.v1.events" }, groupId = "courier-management")
@RequiredArgsConstructor
public class KafkaDeliveriesMessageHandler {

    public CourierDeliveryService courierDeliveryService;

    @KafkaHandler(isDefault = true)
    public void defaultHandle(@Payload Object message) {
        log.warn("Received unknown message type: {}", message.getClass().getName());
    }

    @KafkaHandler
    public void handle(@Payload DeliveryPlacedIntegrationEvent event) {
        log.info("Received DeliveryPlacedIntegrationEvent: {}", event);
        courierDeliveryService.assing(event.getDeliveryId());
        // Handle the event logic here
    }

    @KafkaHandler
    public void handle(@Payload DeliveryFulfilledntegrationEvent event) {
        log.warn("Received DeliveryFulfilledIntegrationEvent type: {}", event.getClass().getName());
        courierDeliveryService.fulfill(event.getDeliveryId());
    }

    @KafkaHandler
    public void handle(@Payload DeliveryCancellationIntegrationEvent event) {
        log.warn("Received DeliveryCancellationIntegrationEvent type: {}", event.getClass().getName());
        courierDeliveryService.fulfill(event.getDeliveryId());
    }

}
