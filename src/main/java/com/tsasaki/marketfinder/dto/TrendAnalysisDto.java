package com.tsasaki.marketfinder.dto;

/**
 * 市場トレンド分析結果を保持するDTOです。
 *
 * @param averageMarketScore        平均Market Score
 * @param averageStars              平均Stars数
 * @param openIssueCount            Open Issue数
 * @param recentlyUpdatedIssueCount 最近更新Issue数
 * @param popularityScore           人気度スコア
 * @param activityScore             活発度スコア
 * @param demandScore               需要スコア
 * @param painLevelScore            課題量スコア
 * @param trendLevel                HOT/WARM/COLD
 */
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
