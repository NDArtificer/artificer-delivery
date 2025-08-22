package com.artificer.artifcerdelivery.courier.management.pages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@AllArgsConstructor
public class PageMetaData {
    private int size;
    private int number;
    private long totalElements;
    private int totalPages;

    public static PageMetaData brandNewPage(Page page) {
        return new PageMetaData(
                page.getSize(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

}