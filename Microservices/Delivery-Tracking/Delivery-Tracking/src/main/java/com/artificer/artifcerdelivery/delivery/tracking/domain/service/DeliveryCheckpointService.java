package com.artificer.artifcerdelivery.delivery.tracking.domain.service;

import com.artificer.artifcerdelivery.delivery.tracking.domain.repository.DeliveryRepository;
import com.artificer.artifcerdelivery.delivery.tracking.exception.EntidadeNaoEncontradaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryCheckpointService {

    private final DeliveryRepository deliveryRepository;


    public void place(UUID deliveryId) {
        var delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Delivery not found"));

        delivery.place();
        deliveryRepository.saveAndFlush(delivery);
    }

    public void pickUps(UUID deliveryId, UUID courierId) {
        var delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Delivery not found"));
        delivery.pickUp(courierId); // Assuming Delivery has a pickup method that handles courier assignment
        deliveryRepository.saveAndFlush(delivery);
    }

    public  void complete(UUID deliveryId) {
        var delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Delivery not found"));

        delivery.markAsDelivered();
        deliveryRepository.saveAndFlush(delivery);
    }

    public void cancel(UUID deliveryId) {
        var delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Delivery not found"));

        delivery.cancel();
        deliveryRepository.saveAndFlush(delivery);
    }
}
