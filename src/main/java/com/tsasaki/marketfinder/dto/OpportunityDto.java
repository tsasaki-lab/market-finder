package com.tsasaki.marketfinder.dto;

/**
 * SaaS化機会の分析結果を表すDTOです。
 *
 * @param repositoryName   対象リポジトリ名
 * @param repositoryUrl    リポジトリURL
 * @param opportunityScore SaaS化機会スコア
 * @param opportunityLevel SaaS化機会レベル
 * @param reason           スコア算出理由
 */
public record OpportunityDto(
        String repositoryName,
        String repositoryUrl,
        double opportunityScore,
        String opportunityLevel,
        String reason
) {
}
