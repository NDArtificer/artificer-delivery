package com.artificer.artifcerdelivery.courier.management.api.controller;

import com.artificer.artifcerdelivery.courier.management.api.model.CourierInput;
import com.artificer.artifcerdelivery.courier.management.api.model.CourierOutput;
import com.artificer.artifcerdelivery.courier.management.api.model.CourierPayoutCalculationInput;
import com.artificer.artifcerdelivery.courier.management.api.model.CourierPayoutResultModel;
import com.artificer.artifcerdelivery.courier.management.domain.model.Courier;
import com.artificer.artifcerdelivery.courier.management.domain.repository.CourierRepository;
import com.artificer.artifcerdelivery.courier.management.domain.service.CourierPayoutService;
import com.artificer.artifcerdelivery.courier.management.domain.service.CourierRegistrationService;
import com.artificer.artifcerdelivery.courier.management.mapper.CourierMapper;
import com.artificer.artifcerdelivery.courier.management.pages.CustomPage;
import com.artificer.artifcerdelivery.courier.management.pages.PageMetaData;
import com.artificer.artifcerdelivery.courier.management.security.CanEditCouriers;
import com.artificer.artifcerdelivery.courier.management.security.CanReadCouriers;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api/v1/couriers")
@RequiredArgsConstructor
public class CourierController {

    private final CourierRegistrationService courierRegistrationService;
    private final CourierRepository courierRepository;
    private final CourierPayoutService courierPayoutService;

    @Autowired
    private CourierMapper courierMapper;

    @GetMapping
    @CanReadCouriers
    public ResponseEntity<CustomPage<CourierOutput>> findAll(@PageableDefault Pageable pageable) {

        Page<Courier> couriersPage = courierRepository.findAll(pageable);

        List<CourierOutput> courierOutputs = courierMapper.toModel(couriersPage.getContent());

        PageMetaData pageMetaData = PageMetaData.brandNewPage(couriersPage);

        CustomPage<CourierOutput> couriersOutputPage = new CustomPage<>(courierOutputs, pageMetaData);

        return ResponseEntity.ok(couriersOutputPage);
    }

    @GetMapping("/{courierId}")
    @CanReadCouriers
    public CourierOutput findById(@PathVariable UUID courierId) {
        Courier courier = courierRegistrationService.findCourierOrfail(courierId);
        return courierMapper.toModel(courier);
    }

    @CanEditCouriers
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourierOutput registerCourier(@Valid @RequestBody CourierInput courier) {
        Courier courier1 = courierRegistrationService.create(courier);
        return courierMapper.toModel(courier1);
    }

    @CanEditCouriers
    @PutMapping("/{courierId}")
    public CourierOutput updateCourier(@PathVariable UUID courierId, @Valid @RequestBody CourierInput courier) {
        Courier courierAtualizado = courierRegistrationService.update(courierId, courier);
        return courierMapper.toModel(courierAtualizado);
    }

    @CanEditCouriers
    @PostMapping("/payout-calculation")
    public CourierPayoutResultModel calculatePayout(@RequestBody CourierPayoutCalculationInput input) {
        BigDecimal payoutFee = courierPayoutService.calculatePayout(input.getDistanceInKm());
        return new CourierPayoutResultModel(payoutFee);
    }

    @CanEditCouriers
    @DeleteMapping("/{courierId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourier(@PathVariable UUID courierId) {
        Courier courier = courierRegistrationService.findCourierOrfail(courierId);
        courierRepository.deleteById(courier.getId());
    }
}
