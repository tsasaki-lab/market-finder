package com.tsasaki.marketfinder.service;

import com.tsasaki.marketfinder.client.GitHubApiClient;
import com.tsasaki.marketfinder.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 検索処理全体を制御するサービスです。
 *
 * <p>
 * 入力チェック、GitHub API呼び出し、並び替え、サマリー生成、
 * トレンド分析、Issueキーワード分析をまとめて実行します。
 * </p>
 */
@Service
public class SearchService {

    private static final int MAX_KEYWORD_LENGTH = 50;

    private final GitHubApiClient gitHubApiClient;
    private final SearchSummaryService searchSummaryService;
    private final IssueSummaryService issueSummaryService;
    private final TrendAnalysisService trendAnalysisService;
    private final IssueKeywordAnalysisService issueKeywordAnalysisService;
    private final AiSummaryService aiSummaryService;
    private final OpportunityScoreService opportunityScoreService;
    private final SaasIdeaGeneratorService saasIdeaGeneratorService;

    public SearchService(
            GitHubApiClient gitHubApiClient,
            SearchSummaryService searchSummaryService,
            IssueSummaryService issueSummaryService,
            TrendAnalysisService trendAnalysisService,
            IssueKeywordAnalysisService issueKeywordAnalysisService,
            AiSummaryService aiSummaryService,
            OpportunityScoreService opportunityScoreService,
            SaasIdeaGeneratorService saasIdeaGeneratorService
    ) {
        this.gitHubApiClient = gitHubApiClient;
        this.searchSummaryService = searchSummaryService;
        this.issueSummaryService = issueSummaryService;
        this.trendAnalysisService = trendAnalysisService;
        this.issueKeywordAnalysisService = issueKeywordAnalysisService;
        this.aiSummaryService = aiSummaryService;
        this.opportunityScoreService = opportunityScoreService;
        this.saasIdeaGeneratorService = saasIdeaGeneratorService;
    }

    /**
     * 指定された条件で市場調査用の検索を実行します。
     *
     * @param keyword  検索キーワード
     * @param language 絞り込み対象のプログラミング言語
     * @param sort     並び替え条件
     * @return 検索結果、サマリー、分析結果を含むレスポンス
     */
    public SearchResponseDto search(String keyword, String language, String sort) {
        String normalizedKeyword = keyword == null ? "" : keyword.trim();
        String normalizedLanguage = language == null ? "" : language.trim();

        if (normalizedKeyword.isBlank()) {
            return new SearchResponseDto(
                    List.of(),
                    List.of(),
                    null,
                    "検索キーワードを入力してください。",
                    emptySearchSummary(),
                    emptyIssueSummary(),
                    emptyTrendAnalysis(),
                    List.of(),
                    AiSummaryDto.unavailable(),
                    List.of(),
                    List.of()
            );
        }

        if (normalizedKeyword.length() > MAX_KEYWORD_LENGTH) {
            return new SearchResponseDto(
                    List.of(),
                    List.of(),
                    null,
                    "検索キーワードは50文字以内で入力してください。",
                    emptySearchSummary(),
                    emptyIssueSummary(),
                    emptyTrendAnalysis(),
                    List.of(),
                    AiSummaryDto.unavailable(),
                    List.of(),
                    List.of()
            );
        }

        try {
            List<GitHubRepositoryDto> repositories =
                    gitHubApiClient.searchRepositories(normalizedKeyword, normalizedLanguage);

            List<GitHubRepositoryDto> sortedRepositories =
                    sortResults(repositories, sort);

            List<GitHubIssueDto> issues =
                    gitHubApiClient.searchIssues(normalizedKeyword, normalizedLanguage);

            SearchSummaryDto summary =
                    searchSummaryService.summarize(sortedRepositories);

            IssueSummaryDto issueSummary =
                    issueSummaryService.summarize(issues);

            TrendAnalysisDto trendAnalysis =
                    trendAnalysisService.analyze(sortedRepositories, issues);

            List<IssueKeywordDto> issueKeywords =
                    issueKeywordAnalysisService.analyze(issues);

            List<OpportunityDto> opportunities =
                    opportunityScoreService.calculate(sortedRepositories, trendAnalysis, issueSummary);

            SearchResponseDto response = new SearchResponseDto(
                    sortedRepositories,
                    issues,
                    null,
                    null,
                    summary,
                    issueSummary,
                    trendAnalysis,
                    issueKeywords,
                    AiSummaryDto.unavailable(),
                    opportunities,
                    List.of()
            );

            AiSummaryDto aiSummary = aiSummaryService.generate(response);

            List<SaasIdeaDto> saasIdeas =
                    saasIdeaGeneratorService.generate(opportunities);

            return new SearchResponseDto(
                    sortedRepositories,
                    issues,
                    null,
                    null,
                    summary,
                    issueSummary,
                    trendAnalysis,
                    issueKeywords,
                    aiSummary,
                    opportunities,
                    saasIdeas
            );

        } catch (IllegalStateException e) {
            return new SearchResponseDto(
                    List.of(),
                    List.of(),
                    "検索中にエラーが発生しました。時間をおいて再度お試しください。",
                    null,
                    emptySearchSummary(),
                    emptyIssueSummary(),
                    emptyTrendAnalysis(),
                    List.of(),
                    AiSummaryDto.unavailable(),
                    List.of(),
                    List.of()
            );
        }
    }

    /**
     * 指定された条件でリポジトリ検索結果を並び替えます。
     *
     * @param results リポジトリ検索結果
     * @param sort    並び替え条件
     * @return 並び替え後のリポジトリ検索結果
     */
    private List<GitHubRepositoryDto> sortResults(
            List<GitHubRepositoryDto> results,
            String sort
    ) {
        if (sort == null || sort.isBlank() || sort.equals("stars")) {
            return results.stream()
                    .sorted((a, b) -> Integer.compare(b.stars(), a.stars()))
                    .toList();
        }

        if (sort.equals("marketScore")) {
            return results.stream()
                    .sorted((a, b) -> Integer.compare(b.marketScore(), a.marketScore()))
                    .toList();
        }

        if (sort.equals("updated")) {
            return results.stream()
                    .sorted((a, b) -> b.updatedAt().compareTo(a.updatedAt()))
                    .toList();
        }

        return results;
    }

    private SearchSummaryDto emptySearchSummary() {
        return new SearchSummaryDto(0, 0.0, 0, 0);
    }

    private IssueSummaryDto emptyIssueSummary() {
        return new IssueSummaryDto(0, 0, 0, 0);
    }

    private TrendAnalysisDto emptyTrendAnalysis() {
        return new TrendAnalysisDto(0.0, 0, 0, 0, 0, 0, 0, 0, "N/A");
    }
}
