package com.artificer.artifcerdelivery.courier.management.domain.service;

import com.artificer.artifcerdelivery.courier.management.api.model.CourierInput;
import com.artificer.artifcerdelivery.courier.management.domain.model.Courier;
import com.artificer.artifcerdelivery.courier.management.domain.repository.CourierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CourierRegistrationService {
private final CourierRepository courierRepository;
    public Courier create(CourierInput courierInput) {
        var curier = Courier.brandNew(courierInput.getName(),  courierInput.getPhone());
        return courierRepository.saveAndFlush(curier);
    }

    public Courier update(UUID courierId, CourierInput courierInput) {
        var courierToUpdate = courierRepository.findById(courierId)
                .orElseThrow(() -> new IllegalArgumentException("Courier not found"));
        courierToUpdate.setName(courierInput.getName());
        courierToUpdate.setPhone(courierInput.getPhone());
        return courierRepository.saveAndFlush(courierToUpdate);
    }
}
