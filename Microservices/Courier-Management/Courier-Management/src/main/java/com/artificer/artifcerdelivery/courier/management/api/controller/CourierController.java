package com.artificer.artifcerdelivery.courier.management.api.controller;

import com.artificer.artifcerdelivery.courier.management.api.model.CourierInput;
import com.artificer.artifcerdelivery.courier.management.api.model.CourierPayoutCalculationInput;
import com.artificer.artifcerdelivery.courier.management.api.model.CourierPayoutResultModel;
import com.artificer.artifcerdelivery.courier.management.domain.model.Courier;
import com.artificer.artifcerdelivery.courier.management.domain.repository.CourierRepository;
import com.artificer.artifcerdelivery.courier.management.domain.service.CourierPayoutService;
import com.artificer.artifcerdelivery.courier.management.domain.service.CourierRegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api/v1/couriers")
@RequiredArgsConstructor
public class CourierController {

    private final CourierRegistrationService courierRegistrationService;
    private final CourierRepository courierRepository;
    private final CourierPayoutService courierPayoutService;

    @GetMapping
    public PagedModel<Courier> findAll(@PageableDefault Pageable pageable) {
        return new PagedModel<>(courierRepository.findAll(pageable)); // Placeholder for actual courier retrieval logic
    }

    @GetMapping("/{courierId}")
    public Courier findById(@PathVariable UUID courierId) {
        return courierRepository.findById(courierId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Courier not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Courier registerCourier(@Valid @RequestBody CourierInput courier) {
        return courierRegistrationService.create(courier);
    }

    @PutMapping("/{courierId}")
    public Courier updateCourier(@PathVariable UUID courierId, @Valid @RequestBody CourierInput courier) {
        return courierRegistrationService.update(courierId, courier);
    }

    @PostMapping("/payout-calculation")
    public CourierPayoutResultModel calculatePayout(@RequestBody CourierPayoutCalculationInput input) {
        BigDecimal payoutFee = courierPayoutService.calculatePayout(input.getDistanceInKm());
        return new CourierPayoutResultModel(payoutFee);
    }

    @DeleteMapping("/{courierId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourier(@PathVariable UUID courierId) {
        if (!courierRepository.existsById(courierId)) {
            throw new ResponseStatusException(NOT_FOUND, "Courier not found");
        }
        courierRepository.deleteById(courierId);
    }
}
