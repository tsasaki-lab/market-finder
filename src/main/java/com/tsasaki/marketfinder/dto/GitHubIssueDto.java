package com.tsasaki.marketfinder.dto;

public record GitHubIssueDto(
        String title,
        String url,
        String repositoryName,
        String state,
        String createdAt,
        String updatedAt
) {
}