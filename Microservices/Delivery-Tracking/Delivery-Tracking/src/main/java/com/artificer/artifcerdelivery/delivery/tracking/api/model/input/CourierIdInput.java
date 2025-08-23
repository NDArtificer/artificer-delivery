package com.artificer.artifcerdelivery.delivery.tracking.api.model.input;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CourierIdInput {

    @NotNull
    private UUID courierId;
}
