package com.tsasaki.marketfinder.dto;

/**
 * GitHub競合分析における競合リポジトリ情報を表すDTO。
 *
 * @param name            リポジトリ名
 * @param htmlUrl         GitHub上のリポジトリURL
 * @param description     リポジトリ説明文
 * @param language        主な使用言語
 * @param stargazersCount Star数
 * @param forksCount      Fork数
 * @param openIssuesCount 未解決Issue数
 * @param marketScore     Market Finder独自の市場スコア
 */
public record CompetitorDto(
        String name,
        String htmlUrl,
        String description,
        String language,
        int stargazersCount,
        int forksCount,
        int openIssuesCount,
        double marketScore
) {
}
