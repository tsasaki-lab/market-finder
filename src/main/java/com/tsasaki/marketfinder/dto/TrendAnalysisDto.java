package com.tsasaki.marketfinder.dto;

public record TrendAnalysisDto(
        double averageMarketScore,
        int averageStars,
        int openIssueCount,
        int recentlyUpdatedIssueCount,
        String trendLevel
) {
}