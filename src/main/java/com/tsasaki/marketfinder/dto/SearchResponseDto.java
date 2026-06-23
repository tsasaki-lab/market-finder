package com.tsasaki.marketfinder.dto;

import java.util.List;

/**
 * 検索処理の実行結果を保持するDTOです。
 *
 * <p>
 * リポジトリ検索結果、Issue検索結果、
 * 各種サマリー情報、トレンド分析結果、
 * Issueキーワード分析結果、AI分析結果、
 * SaaS機会分析結果をまとめて管理します。
 * </p>
 *
 * @param results           リポジトリ検索結果
 * @param issues            Issue検索結果
 * @param errorMessage      システムエラーメッセージ
 * @param validationMessage 入力チェックエラーメッセージ
 * @param summary           リポジトリ検索結果サマリー
 * @param issueSummary      Issue検索結果サマリー
 * @param trendAnalysis     市場トレンド分析結果
 * @param issueKeywords     Issue頻出キーワード分析結果
 * @param aiSummary         AIによる市場分析結果
 * @param opportunities     SaaS機会分析結果
 */
public record SearchResponseDto(
        List<GitHubRepositoryDto> results,
        List<GitHubIssueDto> issues,
        String errorMessage,
        String validationMessage,
        SearchSummaryDto summary,
        IssueSummaryDto issueSummary,
        TrendAnalysisDto trendAnalysis,
        List<IssueKeywordDto> issueKeywords,
        AiSummaryDto aiSummary,
        List<OpportunityDto> opportunities
) {
}
