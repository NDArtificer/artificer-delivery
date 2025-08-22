package com.artificer.artifcerdelivery.courier.management.mapper;

import com.artificer.artifcerdelivery.courier.management.api.model.CourierOutput;
import com.artificer.artifcerdelivery.courier.management.domain.model.Courier;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourierMapper {

    CourierOutput toModel(Courier courier);

    List<CourierOutput> toModel(List<Courier> couriers);

}
