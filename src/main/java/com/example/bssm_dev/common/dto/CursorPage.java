package com.example.bssm_dev.common.dto;

import org.springframework.data.domain.Slice;

import java.util.List;

public record CursorPage<T>(
        List<T> values,
        boolean hasNext
) {
}
