package com.tsasaki.marketfinder.dto;

import java.util.List;

public record SearchResponseDto(
        List<GitHubRepositoryDto> results,
        List<GitHubIssueDto> issues,
        String errorMessage,
        String validationMessage,
        SearchSummaryDto summary,
        IssueSummaryDto issueSummary,
        TrendAnalysisDto trendAnalysis
) {
}