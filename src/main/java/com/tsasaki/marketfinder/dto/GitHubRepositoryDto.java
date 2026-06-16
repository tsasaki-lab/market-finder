package com.tsasaki.marketfinder.dto;

public record GitHubRepositoryDto(
        String name,
        String fullName,
        String url,
        String description,
        int stars,
        String language,
        String updatedAt
) {
}