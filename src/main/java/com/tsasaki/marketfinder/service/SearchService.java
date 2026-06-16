package com.tsasaki.marketfinder.service;

import com.tsasaki.marketfinder.client.GitHubApiClient;
import com.tsasaki.marketfinder.dto.GitHubRepositoryDto;
import com.tsasaki.marketfinder.dto.SearchResponseDto;
import com.tsasaki.marketfinder.dto.SearchSummaryDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    private static final int MAX_KEYWORD_LENGTH = 50;

    private final GitHubApiClient gitHubApiClient;
    private final SearchSummaryService searchSummaryService;

    public SearchService(
            GitHubApiClient gitHubApiClient,
            SearchSummaryService searchSummaryService
    ) {
        this.gitHubApiClient = gitHubApiClient;
        this.searchSummaryService = searchSummaryService;
    }

    public SearchResponseDto search(String keyword, String language) {
        String normalizedKeyword = keyword == null ? "" : keyword.trim();
        String normalizedLanguage = language == null ? "" : language.trim();

        if (normalizedKeyword.isBlank()) {
            return new SearchResponseDto(
                    List.of(),
                    null,
                    "検索キーワードを入力してください。",
                    new SearchSummaryDto(0, 0.0, 0, 0)
            );
        }

        if (normalizedKeyword.length() > MAX_KEYWORD_LENGTH) {
            return new SearchResponseDto(
                    List.of(),
                    null,
                    "検索キーワードは50文字以内で入力してください。",
                    new SearchSummaryDto(0, 0.0, 0, 0)
            );
        }

        try {
            List<GitHubRepositoryDto> results =
                    gitHubApiClient.searchRepositories(normalizedKeyword, normalizedLanguage);

            SearchSummaryDto summary = searchSummaryService.summarize(results);

            return new SearchResponseDto(results, null, null, summary);

        } catch (IllegalStateException e) {
            return new SearchResponseDto(
                    List.of(),
                    "検索中にエラーが発生しました。時間をおいて再度お試しください。",
                    null,
                    new SearchSummaryDto(0, 0.0, 0, 0)
            );
        }
    }
}