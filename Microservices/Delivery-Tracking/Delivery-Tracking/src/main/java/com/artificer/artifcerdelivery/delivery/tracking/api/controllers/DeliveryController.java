package com.artificer.artifcerdelivery.delivery.tracking.api.controllers;

import com.artificer.artifcerdelivery.delivery.tracking.api.model.CourierIdInput;
import com.artificer.artifcerdelivery.delivery.tracking.api.model.DeliveryInput;
import com.artificer.artifcerdelivery.delivery.tracking.domain.model.Delivery;
import com.artificer.artifcerdelivery.delivery.tracking.domain.repository.DeliveryRepository;
import com.artificer.artifcerdelivery.delivery.tracking.domain.service.DeliveryCheckpointService;
import com.artificer.artifcerdelivery.delivery.tracking.domain.service.DeliveryPreparationService;
import com.artificer.artifcerdelivery.delivery.tracking.security.CanEditDelivery;
import com.artificer.artifcerdelivery.delivery.tracking.security.CanReadDelivery;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryPreparationService deliveryPreparationService;
    private final DeliveryCheckpointService deliveryCheckpointService;
    private final DeliveryRepository deliveryRepository;

    @GetMapping
    @CanReadDelivery
    public PagedModel<Delivery> findAll(@PageableDefault Pageable pageable) {
        return new PagedModel<>(deliveryRepository.findAll(pageable)); // Placeholder for actual delivery retrieval logic
    }

    @GetMapping("/{deliveryId}")
    @CanReadDelivery
    public Delivery findById(@PathVariable UUID deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)); // Placeholder for actual delivery retrieval logic
    }

    @CanEditDelivery
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Delivery draft(@RequestBody @Valid DeliveryInput deliveryInput) {
        return deliveryPreparationService.draft(deliveryInput); // Placeholder for actual delivery creation logic

    }

    @CanEditDelivery
    @PutMapping("/{deliveryId}")
    public Delivery edit(@PathVariable UUID deliveryId, @RequestBody @Valid DeliveryInput deliveryInput) {
        return deliveryPreparationService.edit(deliveryId, deliveryInput); // Placeholder for actual delivery editing logic

    }

    @CanEditDelivery
    @PostMapping("/{deliveryId}/placement")
    public void place(@PathVariable UUID deliveryId) {
        deliveryCheckpointService.place(deliveryId);
    }


    @CanEditDelivery
    @PostMapping("/{deliveryId}/pickups")
    public void pickups(@PathVariable UUID deliveryId, @RequestBody @Valid CourierIdInput courierIdInput) {
        deliveryCheckpointService.pickUps(deliveryId, courierIdInput.getCourierId());
    }

    @CanEditDelivery
    @PostMapping("/{deliveryId}/completition")
    public void complete(@PathVariable UUID deliveryId) {
        deliveryCheckpointService.complete(deliveryId);
    }

}
