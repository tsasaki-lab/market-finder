package com.tsasaki.marketfinder.dto;

/**
 * リポジトリ検索結果の集計情報を保持するDTOです。
 *
 * @param resultCount        検索結果件数
 * @param averageMarketScore 平均Market Score
 * @param maxMarketScore     最大Market Score
 * @param totalStars         合計Stars数
 */
public record SearchSummaryDto(
        int resultCount,
        double averageMarketScore,
        int maxMarketScore,
        int totalStars
) {
}
