package com.artificer.artifcerdelivery.courier.management.api.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Setter
@Getter
public class CourierOutput{

    private UUID id;

    private String name;

    private String phone;

    private Integer fullfilledDeliveriesQuantity;

    private Integer pendingDeliveriesQuantity;
}
