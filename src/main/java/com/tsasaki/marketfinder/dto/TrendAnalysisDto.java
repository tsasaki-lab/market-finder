package com.tsasaki.marketfinder.dto;

public record TrendAnalysisDto(
        double averageMarketScore,
        int averageStars,
        int openIssueCount,
        int recentlyUpdatedIssueCount,

        int popularityScore,
        int activityScore,
        int demandScore,
        int painLevelScore,

        String trendLevel
) {
}