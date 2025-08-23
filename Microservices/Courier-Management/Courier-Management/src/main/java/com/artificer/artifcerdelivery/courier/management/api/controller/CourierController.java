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

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


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

    @CanReadCouriers
    @GetMapping("/{courierId}")
    public ResponseEntity<CourierOutput> findById(@PathVariable UUID courierId) {
        Courier courier = courierRegistrationService.findCourierOrfail(courierId);
        CourierOutput courierOutput = courierMapper.toModel(courier);
        return ResponseEntity.ok(courierOutput);
    }

    @CanEditCouriers
    @PostMapping
    public ResponseEntity<CourierOutput> registerCourier(@Valid @RequestBody CourierInput courierInput) {
        Courier courier = courierRegistrationService.create(courierInput);
        CourierOutput courierOutput = courierMapper.toModel(courier);
        return ResponseEntity.status(HttpStatus.CREATED).body(courierOutput);
    }

    @CanEditCouriers
    @DeleteMapping("/{courierId}")
    public ResponseEntity<Void> deleteCourier(@PathVariable UUID courierId) {
        Courier courier = courierRegistrationService.findCourierOrfail(courierId);
        courierRepository.deleteById(courier.getId());
        return ResponseEntity.noContent().build();
    }

    @CanEditCouriers
    @PutMapping("/{courierId}")
    public ResponseEntity<CourierOutput> updateCourier(@PathVariable UUID courierId, @Valid @RequestBody CourierInput courier) {
        Courier courierAtualizado = courierRegistrationService.update(courierId, courier);
        CourierOutput courierOutput = courierMapper.toModel(courierAtualizado);
        return ResponseEntity.ok(courierOutput);
    }

    @CanEditCouriers
    @PostMapping("/payout-calculation")
    public ResponseEntity<CourierPayoutResultModel> calculatePayout(@RequestBody CourierPayoutCalculationInput input) {
        BigDecimal payoutFee = courierPayoutService.calculatePayout(input.getDistanceInKm());
        CourierPayoutResultModel courierPayoutResultModel = new CourierPayoutResultModel(payoutFee);
        return ResponseEntity.ok(courierPayoutResultModel);
    }

}
