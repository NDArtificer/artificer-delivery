package com.artificer.artifcerdelivery.delivery.tracking.api.model.output;

import com.artificer.artifcerdelivery.delivery.tracking.domain.model.DeliveryStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class DeliveryOutput {

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
    private ContactPointOutput sender;
    private ContactPointOutput recipient;
    private List<ItemOutput> items;
}
