package com.tsasaki.marketfinder.service;

import com.tsasaki.marketfinder.client.GitHubApiClient;
import com.tsasaki.marketfinder.dto.*;
import org.springframework.stereotype.Service;

import java.util.Optional;
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

    private final GitHubApiClient gitHubApiClient;
    private final SearchSummaryService searchSummaryService;
    private final IssueSummaryService issueSummaryService;
    private final TrendAnalysisService trendAnalysisService;
    private final IssueKeywordAnalysisService issueKeywordAnalysisService;
    private final AiSummaryService aiSummaryService;
    private final OpportunityScoreService opportunityScoreService;
    private final SaasIdeaGeneratorService saasIdeaGeneratorService;
    private final SearchResponseFactory searchResponseFactory;
    private final SearchRequestValidator searchRequestValidator;
    private final RepositorySortService repositorySortService;

    public SearchService(
            GitHubApiClient gitHubApiClient,
            SearchSummaryService searchSummaryService,
            IssueSummaryService issueSummaryService,
            TrendAnalysisService trendAnalysisService,
            IssueKeywordAnalysisService issueKeywordAnalysisService,
            AiSummaryService aiSummaryService,
            OpportunityScoreService opportunityScoreService,
            SaasIdeaGeneratorService saasIdeaGeneratorService,
            SearchResponseFactory searchResponseFactory,
            SearchRequestValidator searchRequestValidator,
            RepositorySortService repositorySortService
    ) {
        this.gitHubApiClient = gitHubApiClient;
        this.searchSummaryService = searchSummaryService;
        this.issueSummaryService = issueSummaryService;
        this.trendAnalysisService = trendAnalysisService;
        this.issueKeywordAnalysisService = issueKeywordAnalysisService;
        this.aiSummaryService = aiSummaryService;
        this.opportunityScoreService = opportunityScoreService;
        this.saasIdeaGeneratorService = saasIdeaGeneratorService;
        this.searchResponseFactory = searchResponseFactory;
        this.searchRequestValidator = searchRequestValidator;
        this.repositorySortService = repositorySortService;
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

        Optional<String> validationError =
                searchRequestValidator.validateKeyword(normalizedKeyword);

        if (validationError.isPresent()) {
            return searchResponseFactory.validationError(validationError.get());
        }

        try {
            List<GitHubRepositoryDto> repositories =
                    gitHubApiClient.searchRepositories(normalizedKeyword, normalizedLanguage);

            List<GitHubRepositoryDto> sortedRepositories =
                    repositorySortService.sort(repositories, sort);

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
            return searchResponseFactory.systemError(
                    "検索中にエラーが発生しました。時間をおいて再度お試しください。"
            );
        }
    }

}
