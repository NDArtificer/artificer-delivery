package com.artificer.artifcerdelivery.delivery.tracking.domain.infrastructure.kafka;

import com.artificer.artifcerdelivery.delivery.tracking.domain.infrastructure.event.IntegrationEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class IntegrationEventPublisherKafkaImpl implements IntegrationEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publish(Object event, String key, String topic) {
        SendResult<String, Object> result = kafkaTemplate.send(topic, key, event).join();

        RecordMetadata metadata = result.getRecordMetadata();
        log.info("Message Published : \n\t Topic: {}\n\t Offset {}",
                metadata.topic(),
                metadata.offset());

    }
}
