package com.artificer.artifcerdelivery.delivery.tracking.mapper;

import com.artificer.artifcerdelivery.delivery.tracking.api.model.output.DeliveryOutput;
import com.artificer.artifcerdelivery.delivery.tracking.domain.model.Delivery;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ItemMapper.class, ContactPointMapper.class})
public interface DeliveryMapper {

    DeliveryOutput toModel(Delivery delivery);

    List<DeliveryOutput> toModel(List<Delivery> deliveries);
}
