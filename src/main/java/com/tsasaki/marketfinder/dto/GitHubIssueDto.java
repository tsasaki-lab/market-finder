package com.tsasaki.marketfinder.dto;

/**
 * GitHub Issue情報を保持するDTOです。
 *
 * @param title          Issueタイトル
 * @param url            Issue URL
 * @param repositoryName リポジトリ名
 * @param state          Issue状態
 * @param createdAt      作成日時
 * @param updatedAt      更新日時
 */
public record GitHubIssueDto(
        String title,
        String url,
        String repositoryName,
        String state,
        String createdAt,
        String updatedAt
) {
}
