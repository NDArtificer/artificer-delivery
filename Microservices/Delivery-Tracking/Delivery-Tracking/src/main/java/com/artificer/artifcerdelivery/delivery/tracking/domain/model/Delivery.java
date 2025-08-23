package com.artificer.artifcerdelivery.delivery.tracking.domain.model;

import com.artificer.artifcerdelivery.delivery.tracking.domain.event.DeliveryCancelationEvent;
import com.artificer.artifcerdelivery.delivery.tracking.domain.event.DeliveryFulfilledEvent;
import com.artificer.artifcerdelivery.delivery.tracking.domain.event.DeliveryPickedupEvent;
import com.artificer.artifcerdelivery.delivery.tracking.domain.event.DeliveryPlacedEvent;
import com.artificer.artifcerdelivery.delivery.tracking.exception.NegocioException;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.*;

@Entity
@Setter(AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Delivery extends AbstractAggregateRoot<Delivery> {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;
    private UUID courierId;

    private DeliveryStatus status;

    private OffsetDateTime placedAt;
    private OffsetDateTime assignedAt;
    private OffsetDateTime expectedDeliveryAt;
    private OffsetDateTime fulfilledAt;

    private BigDecimal distanceFee;
    private BigDecimal currierPayout;
    private BigDecimal totalCost;

    private Integer totalItems;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "zipCode", column = @Column(name = "sender_zip_code")),
            @AttributeOverride(name = "street", column = @Column(name = "sender_street")),
            @AttributeOverride(name = "number", column = @Column(name = "sender_number")),
            @AttributeOverride(name = "complement", column = @Column(name = "sender_complement")),
            @AttributeOverride(name = "name", column = @Column(name = "sender_name")),
            @AttributeOverride(name = "phoneNumber", column = @Column(name = "sender_phone_number"))
    })
    private ContactPoint sender;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "zipCode", column = @Column(name = "recipient_zip_code")),
            @AttributeOverride(name = "street", column = @Column(name = "recipient_street")),
            @AttributeOverride(name = "number", column = @Column(name = "recipient_number")),
            @AttributeOverride(name = "complement", column = @Column(name = "recipient_complement")),
            @AttributeOverride(name = "name", column = @Column(name = "recipient_name")),
            @AttributeOverride(name = "phoneNumber", column = @Column(name = "recipient_phone_number"))
    })
    private ContactPoint recipient;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "delivery")
    private List<Item> items = new ArrayList<>();


    public static Delivery draft() {
        Delivery delivery = new Delivery();
        delivery.setId(UUID.randomUUID());
        delivery.setTotalItems(0);
        delivery.setTotalCost(BigDecimal.ZERO);
        delivery.setCurrierPayout(BigDecimal.ZERO);
        delivery.setStatus(DeliveryStatus.DRAFT);
        delivery.setPlacedAt(OffsetDateTime.now());
        delivery.setDistanceFee(BigDecimal.ZERO);
        return delivery;
    }

    public UUID addItem(String name, Integer quantity) {
        Item item = Item.brandNew(name, quantity, this);
        this.items.add(item);
        calculateTotalItems();
        return item.getId();
    }

    private void changeItemQuantity(UUID itemId, Integer quantity) {
        Item item = getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst().orElseThrow();
        item.setQuantity(quantity);
        calculateTotalItems();
    }

    public void removeItem(UUID itemId) {
        this.items.removeIf(item -> item.getId().equals(itemId));
        calculateTotalItems();
    }

    public void removeAllItems() {
        this.items.clear();
        calculateTotalItems();
    }

    public void place() {
        changeStatus(DeliveryStatus.WAITING_FOR_COURIER);
        this.placedAt = OffsetDateTime.now();
        super.registerEvent(new DeliveryPlacedEvent(this.placedAt, this.id));
    }

    public void markAsDelivered() {
        changeStatus(DeliveryStatus.DELIVERED);
        this.fulfilledAt = OffsetDateTime.now();
        super.registerEvent(new DeliveryFulfilledEvent(this.fulfilledAt, this.id));
    }

    public void cancel() {
        changeStatus(DeliveryStatus.CANCELLED);
        super.registerEvent(new DeliveryCancelationEvent(this.fulfilledAt, this.id));
    }

    public void pickUp(UUID courierId) {
        if (!isFilled()) {
            throw new NegocioException("Cannot pick up a delivery that is not filled with sender, recipient and items.");
        }
        setCourierId(courierId);
        changeStatus(DeliveryStatus.IN_TRANSIT);
        setAssignedAt(OffsetDateTime.now());
        super.registerEvent(new DeliveryPickedupEvent(this.assignedAt, this.id));
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(this.items);
    }

    private void calculateTotalItems() {
        int sum = this.items.stream()
                .mapToInt(Item::getQuantity)
                .sum();
        setTotalItems(sum);
    }

    public boolean isFilled() {
        return this.getSender() != null &&
                this.getRecipient() != null &&
                this.getItems() != null;
    }

    private void changeStatus(DeliveryStatus status) {
        if (this.status.canNotChangeTo(status)) {
            throw new NegocioException("Cannot change status from " + this.status + " to " + status);
        }
        this.status = status;
    }

    public void verifyIfCanBeEdited() {
        if (this.status != DeliveryStatus.DRAFT) {
            throw new NegocioException("Cannot edit a delivery that is not in DRAFT status.");
        }
    }

    public void editPreparationDetails(PreparationDetails preparationDetails) {
        if (preparationDetails == null) {
            throw new NegocioException("Preparation details cannot be null.");
        }
        verifyIfCanBeEdited();
        this.setSender(preparationDetails.getSender());
        this.setRecipient(preparationDetails.getRecipient());
        this.setDistanceFee(preparationDetails.getDistanceFee());
        this.setCurrierPayout(preparationDetails.getCurrierPayout());
        this.setExpectedDeliveryAt(OffsetDateTime.now().plus(preparationDetails.getExpectedDeliveryTime()));
        this.setTotalCost(preparationDetails.getDistanceFee().add(preparationDetails.getCurrierPayout()));
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class PreparationDetails {
        private ContactPoint sender;
        private ContactPoint recipient;
        private BigDecimal distanceFee;
        private BigDecimal currierPayout;
        private Duration expectedDeliveryTime;

        // Getters can be added here if needed
    }
}
