package com.tsasaki.marketfinder.dto;

/**
 * Issue検索結果の集計情報を保持するDTOです。
 *
 * @param issueCount           Issue件数
 * @param openCount            Open Issue件数
 * @param closedCount          Closed Issue件数
 * @param recentlyUpdatedCount 最近更新されたIssue件数
 */
public record IssueSummaryDto(
        int issueCount,
        int openCount,
        int closedCount,
        int recentlyUpdatedCount
) {
}
