package com.schoolservice.school_service_backend.common.util;

import com.schoolservice.school_service_backend.common.response.PageResponse;
import org.springframework.data.domain.Page;

import java.util.function.Function;

public class PageMapper {

    private PageMapper() {}

    public static <T> PageResponse<T> toPageResponse(Page<T> page) {

        return PageResponse.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    /* Senior version – mapping inside */
    public static <T, R> PageResponse<R> toPageResponse(Page<T> page, Function<T, R> mapper) {

        return PageResponse.<R>builder()
                .content(page.map(mapper).getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }
}