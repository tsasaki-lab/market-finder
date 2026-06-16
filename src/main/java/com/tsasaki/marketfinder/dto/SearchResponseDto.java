package com.tsasaki.marketfinder.dto;

import java.util.List;

public record SearchResponseDto(
        List<GitHubRepositoryDto> results,
        String errorMessage,
        String validationMessage
) {
}