package com.example.demo.domain.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Setter
@Getter
public class PageResponseDto implements ResponseDto {

    private final int totalItems;
    private final int totalPages;
    private final int page;
    private final int size;

    public PageResponseDto(Page<?> page) {
        this.totalItems = page.getNumberOfElements();
        this.totalPages = page.getTotalPages();
        this.page = page.getNumber();
        this.size = page.getSize();
    }

    @Override
    public String toLogJson() {
        return toJson();
    }
}