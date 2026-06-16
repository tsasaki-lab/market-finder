package com.tsasaki.marketfinder.dto;

public record SearchSummaryDto(
        int resultCount,
        double averageMarketScore,
        int maxMarketScore,
        int totalStars
) {
}