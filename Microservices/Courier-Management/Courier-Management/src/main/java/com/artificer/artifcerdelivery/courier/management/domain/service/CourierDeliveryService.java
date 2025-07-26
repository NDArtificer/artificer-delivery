package com.artificer.artifcerdelivery.courier.management.domain.service;

import com.artificer.artifcerdelivery.courier.management.domain.model.Courier;
import com.artificer.artifcerdelivery.courier.management.domain.repository.CourierRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CourierDeliveryService {

    public final CourierRepository courierRepository;

    public void assing(UUID id){
        Courier courier = courierRepository.findTop1ByOrderByLastFulfilledDeliveryAtAsc().orElseThrow();
        courier.assign(id);
        courierRepository.saveAndFlush(courier);

        log.info("Assigned delivery {} to courier {}", id, courier.getId());

    }


    public void fulfill(UUID deliveryId) {
        Courier courier = courierRepository.findByPendingDeliveries_id(deliveryId).orElseThrow();
        courier.fulfill(deliveryId);
        courierRepository.saveAndFlush(courier);
        log.info("Fulfilled delivery {} by courier {}", deliveryId, courier.getId());
    }
}
