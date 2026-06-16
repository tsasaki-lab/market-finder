package com.tsasaki.marketfinder.dto;

import java.util.List;

public record SearchResponseDto(
        List<GitHubRepositoryDto> results,
        String errorMessage
) {
    public boolean hasError() {
        return errorMessage != null && !errorMessage.isBlank();
    }
}