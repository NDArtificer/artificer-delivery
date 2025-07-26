package com.artificer.artifcerdelivery.delivery.tracking.domain.service;

import com.artificer.artifcerdelivery.delivery.tracking.api.model.DeliveryInput;
import com.artificer.artifcerdelivery.delivery.tracking.api.model.ItemInput;
import com.artificer.artifcerdelivery.delivery.tracking.domain.model.ContactPoint;
import com.artificer.artifcerdelivery.delivery.tracking.domain.model.Delivery;
import com.artificer.artifcerdelivery.delivery.tracking.domain.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryPreparationService {

    private final DeliveryRepository deliveryRepository;

    private final DeliveryTimeEstimationService deliveryTimeEstimationService;
    private final CourierPayoutCalculationService courierPayoutCalculationService;

    @Transactional
    public Delivery draft(DeliveryInput deliveryInput) {
        Delivery delivery = Delivery.draft();
        handlePreparation(delivery, deliveryInput);
        return deliveryRepository.saveAndFlush(delivery);
    }

    @Transactional
    public Delivery edit(UUID deliveryId, DeliveryInput deliveryInput) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new IllegalArgumentException("Delivery not found"));
        delivery.removeAllItems();
        handlePreparation(delivery, deliveryInput);
        return deliveryRepository.saveAndFlush(delivery);
    }

    private void handlePreparation(Delivery delivery, DeliveryInput deliveryInput) {
        var senderInput = deliveryInput.getSender();
        var recipientInput = deliveryInput.getRecipient();
        var sender = ContactPoint.builder().
                name(senderInput.getName()).
                number(senderInput.getNumber()).
                phoneNumber(senderInput.getPhone()).
                complement(senderInput.getComplement()).
                zipCode(senderInput.getZipCode()).
                street(senderInput.getStreet()).
                build();

        var recipent = ContactPoint.builder().
                name(recipientInput.getName()).
                number(recipientInput.getNumber()).
                phoneNumber(recipientInput.getPhone()).
                complement(recipientInput.getComplement()).
                zipCode(recipientInput.getZipCode()).
                street(recipientInput.getStreet()).
                build();

        DeliveryEstimate deliveryEstimate = deliveryTimeEstimationService.estimate(sender, recipent);
        BigDecimal calculatedPayout = courierPayoutCalculationService.calculatePayout(deliveryEstimate.getDistanceInKm());
        BigDecimal distanceFee = calculateFee(deliveryEstimate); // Example distance fee calculation

        Delivery.PreparationDetails preparationDetails = Delivery.PreparationDetails.builder()
                .sender(sender)
                .recipient(recipent)
                .expectedDeliveryTime(deliveryEstimate.getEstimatedTime())
                .currierPayout(calculatedPayout)
                .distanceFee(distanceFee)
                .build();

        delivery.editPreparationDetails(preparationDetails);

        for (ItemInput itemInput : deliveryInput.getItems()) {
            delivery.addItem(itemInput.getName(), itemInput.getQuantity());
        }

    }

    private static BigDecimal calculateFee(DeliveryEstimate deliveryEstimate) {
        return BigDecimal.valueOf(deliveryEstimate.getDistanceInKm())
                .multiply(BigDecimal.valueOf(3)).setScale(2 , RoundingMode.HALF_EVEN); // Example fee calculation: 3 currency units per km);

    }
}
