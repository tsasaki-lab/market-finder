package com.tsasaki.marketfinder.service;

import com.tsasaki.marketfinder.client.GitHubApiClient;
import com.tsasaki.marketfinder.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    private static final int MAX_KEYWORD_LENGTH = 50;

    private final GitHubApiClient gitHubApiClient;
    private final SearchSummaryService searchSummaryService;
    private final IssueSummaryService issueSummaryService;
    private final TrendAnalysisService trendAnalysisService;

    public SearchService(
            GitHubApiClient gitHubApiClient,
            SearchSummaryService searchSummaryService,
            IssueSummaryService issueSummaryService,
            TrendAnalysisService trendAnalysisService
    ) {
        this.gitHubApiClient = gitHubApiClient;
        this.searchSummaryService = searchSummaryService;
        this.issueSummaryService = issueSummaryService;
        this.trendAnalysisService = trendAnalysisService;
    }

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
                    emptyTrendAnalysis()
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
                    emptyTrendAnalysis()
            );
        }

        try {
            List<GitHubRepositoryDto> results =
                    gitHubApiClient.searchRepositories(normalizedKeyword, normalizedLanguage);

            List<GitHubRepositoryDto> sortedResults = sortResults(results, sort);

            List<GitHubIssueDto> issues =
                    gitHubApiClient.searchIssues(normalizedKeyword, normalizedLanguage);

            SearchSummaryDto summary = searchSummaryService.summarize(sortedResults);

            IssueSummaryDto issueSummary = issueSummaryService.summarize(issues);

            TrendAnalysisDto trendAnalysis =
                    trendAnalysisService.analyze(
                            sortedResults,
                            issues
                    );

            return new SearchResponseDto(
                    sortedResults,
                    issues,
                    null,
                    null,
                    summary,
                    issueSummary,
                    trendAnalysis
            );
        } catch (IllegalStateException e) {
            return new SearchResponseDto(
                    List.of(),
                    List.of(),
                    "検索中にエラーが発生しました。時間をおいて再度お試しください。",
                    null,
                    emptySearchSummary(),
                    emptyIssueSummary(),
                    emptyTrendAnalysis()
            );
        }
    }

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
        return new TrendAnalysisDto(
                0.0,
                0,
                0,
                0,

                0,
                0,
                0,
                0,

                "N/A"
        );
    }

}