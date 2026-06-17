package com.tsasaki.marketfinder.dto;

/**
 * GitHubリポジトリ情報を保持するDTOです。
 *
 * @param name           リポジトリ名
 * @param fullName       owner/repository形式の名前
 * @param url            GitHub URL
 * @param description    リポジトリ説明
 * @param stars          Stars数
 * @param language       プログラミング言語
 * @param updatedAt      最終更新日時
 * @param marketScore    市場スコア
 * @param starsScore     Stars評価スコア
 * @param freshnessScore 更新頻度評価スコア
 */
public record GitHubRepositoryDto(
        String name,
        String fullName,
        String url,
        String description,
        int stars,
        String language,
        String updatedAt,
        int marketScore,
        int starsScore,
        int freshnessScore
) {
}
