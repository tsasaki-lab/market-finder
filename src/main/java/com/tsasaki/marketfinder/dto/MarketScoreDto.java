package com.tsasaki.marketfinder.dto;

/**
 * Market Scoreの算出結果を保持するDTOです。
 *
 * @param totalScore     総合スコア
 * @param starsScore     Stars評価スコア
 * @param freshnessScore 更新頻度評価スコア
 */
public record MarketScoreDto(
        int totalScore,
        int starsScore,
        int freshnessScore
) {
}
