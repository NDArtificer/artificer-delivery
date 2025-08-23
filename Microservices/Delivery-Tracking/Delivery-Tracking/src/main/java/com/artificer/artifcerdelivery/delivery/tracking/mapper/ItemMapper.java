package com.artificer.artifcerdelivery.delivery.tracking.mapper;

import com.artificer.artifcerdelivery.delivery.tracking.api.model.output.ItemOutput;
import com.artificer.artifcerdelivery.delivery.tracking.domain.model.Item;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemOutput toModel(Item item);

    List<ItemOutput> toModel(List<Item> itens);
}
