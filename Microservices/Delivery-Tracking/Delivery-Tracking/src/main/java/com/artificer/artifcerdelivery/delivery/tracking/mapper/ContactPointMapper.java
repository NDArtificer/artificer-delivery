package com.artificer.artifcerdelivery.delivery.tracking.mapper;

import com.artificer.artifcerdelivery.delivery.tracking.api.model.output.ContactPointOutput;
import com.artificer.artifcerdelivery.delivery.tracking.domain.model.ContactPoint;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ContactPointMapper {
    @Mapping(source = "phoneNumber", target = "phone")
    ContactPointOutput toModel(ContactPoint contactPoint);
}
