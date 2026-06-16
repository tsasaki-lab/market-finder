package com.tsasaki.marketfinder.dto;

public record MarketScoreDto(
        int totalScore,
        int starsScore,
        int freshnessScore
) {
}