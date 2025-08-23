package com.artificer.artifcerdelivery.delivery.tracking.api.controllers;

import com.artificer.artifcerdelivery.delivery.tracking.api.model.input.CourierIdInput;
import com.artificer.artifcerdelivery.delivery.tracking.api.model.input.DeliveryInput;
import com.artificer.artifcerdelivery.delivery.tracking.api.model.output.DeliveryOutput;
import com.artificer.artifcerdelivery.delivery.tracking.domain.model.Delivery;
import com.artificer.artifcerdelivery.delivery.tracking.domain.repository.DeliveryRepository;
import com.artificer.artifcerdelivery.delivery.tracking.domain.service.DeliveryCheckpointService;
import com.artificer.artifcerdelivery.delivery.tracking.domain.service.DeliveryPreparationService;
import com.artificer.artifcerdelivery.delivery.tracking.mapper.DeliveryMapper;
import com.artificer.artifcerdelivery.delivery.tracking.pages.CustomPage;
import com.artificer.artifcerdelivery.delivery.tracking.pages.PageMetaData;
import com.artificer.artifcerdelivery.delivery.tracking.security.CanEditDelivery;
import com.artificer.artifcerdelivery.delivery.tracking.security.CanReadDelivery;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryPreparationService deliveryPreparationService;
    private final DeliveryCheckpointService deliveryCheckpointService;
    private final DeliveryRepository deliveryRepository;

    @Autowired
    private DeliveryMapper deliveryMapper;

    @GetMapping
    @CanReadDelivery
    public ResponseEntity<CustomPage<DeliveryOutput>> findAll(@PageableDefault Pageable pageable) {
        Page<Delivery> deliveries = deliveryRepository.findAll(pageable);
        List<DeliveryOutput> deliveriesOutput = deliveryMapper.toModel(deliveries.getContent());
        PageMetaData pageMetaData = PageMetaData.brandNewPage(deliveries);
        CustomPage<DeliveryOutput> customPage = new CustomPage<>(deliveriesOutput,pageMetaData);
        return ResponseEntity.ok(customPage);
    }

    @GetMapping("/{deliveryId}")
    @CanReadDelivery
    public ResponseEntity<DeliveryOutput> findById(@PathVariable UUID deliveryId) {
        Delivery delivery = deliveryPreparationService.findOrFail(deliveryId);
        DeliveryOutput model = deliveryMapper.toModel(delivery);
        return ResponseEntity.ok(model);
    }

    @CanEditDelivery
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DeliveryOutput> draft(@RequestBody @Valid DeliveryInput deliveryInput) {
        Delivery delivery = deliveryPreparationService.draft(deliveryInput);
        DeliveryOutput deliveryOutput = deliveryMapper.toModel(delivery);
        return ResponseEntity.status(HttpStatus.CREATED).body(deliveryOutput);

    }

    @CanEditDelivery
    @PutMapping("/{deliveryId}")
    public ResponseEntity<DeliveryOutput> edit(@PathVariable UUID deliveryId, @RequestBody @Valid DeliveryInput deliveryInput) {
        Delivery delivery = deliveryPreparationService.edit(deliveryId, deliveryInput);
        DeliveryOutput deliveryOutput = deliveryMapper.toModel(delivery);
        return ResponseEntity.ok(deliveryOutput);
    }

    @CanEditDelivery
    @PutMapping("/{deliveryId}/placement")
    public  ResponseEntity<Void> place(@PathVariable UUID deliveryId) {
        deliveryCheckpointService.place(deliveryId);
        return ResponseEntity.noContent().build();
    }

    @CanEditDelivery
    @PutMapping("/{deliveryId}/pickups")
    public ResponseEntity<Void> pickups(@PathVariable UUID deliveryId, @RequestBody @Valid CourierIdInput courierIdInput) {
        deliveryCheckpointService.pickUps(deliveryId, courierIdInput.getCourierId());
        return ResponseEntity.noContent().build();
    }

    @CanEditDelivery
    @PutMapping("/{deliveryId}/completition")
    public ResponseEntity<Void> complete(@PathVariable UUID deliveryId) {
        deliveryCheckpointService.complete(deliveryId);
        return ResponseEntity.noContent().build();
    }

    @CanEditDelivery
    @PutMapping("/{deliveryId}/cancellation")
    public ResponseEntity<Void> cancel(@PathVariable UUID deliveryId) {
        deliveryCheckpointService.cancel(deliveryId);
        return ResponseEntity.noContent().build();
    }
}
