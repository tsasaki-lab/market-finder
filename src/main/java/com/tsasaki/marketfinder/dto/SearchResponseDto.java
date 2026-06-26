package com.tsasaki.marketfinder.dto;

import java.util.List;

/**
 * 検索結果画面に表示する情報を保持するDTOです。
 *
 * @param results             検索結果のGitHubリポジトリ一覧
 * @param issues              GitHub Issue一覧
 * @param errorMessage        システムエラーメッセージ
 * @param validationMessage   入力チェックエラーメッセージ
 * @param summary             検索結果の集計情報
 * @param issueSummary        Issue集計情報
 * @param trendAnalysis       トレンド分析結果
 * @param issueKeywords       Issueキーワード分析結果
 * @param aiSummary           AIによる市場分析結果
 * @param competitionAnalysis GitHub競合分析結果
 * @param opportunities       SaaS機会ランキング
 * @param saasIdeas           AIが生成したSaaSアイデア一覧
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
        CompetitionAnalysisDto competitionAnalysis,
        List<OpportunityDto> opportunities,
        List<SaasIdeaDto> saasIdeas
) {
}
