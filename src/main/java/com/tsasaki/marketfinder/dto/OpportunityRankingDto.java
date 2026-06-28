package com.tsasaki.marketfinder.dto;

/**
 * SaaS機会の優先度ランキングを表すDTOです。
 *
 * @param title            ランキング対象のタイトル
 * @param priority         優先度
 * @param rankingScore     ランキングスコア
 * @param opportunityScore 機会スコア
 * @param competitionScore 競合スコア
 * @param reason           優先度判定の理由
 */
public record OpportunityRankingDto(
        String title,
        String priority,
        int rankingScore,
        int opportunityScore,
        int competitionScore,
        String reason
) {
}
