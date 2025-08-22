package com.artificer.artifcerdelivery.courier.management.pages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CustomPage<T> {
    private List<T> content;
    private PageMetaData page;


}
