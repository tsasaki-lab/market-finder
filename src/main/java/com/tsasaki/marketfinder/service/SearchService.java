package com.tsasaki.marketfinder.service;

import com.tsasaki.marketfinder.client.GitHubApiClient;
import com.tsasaki.marketfinder.dto.GitHubRepositoryDto;
import com.tsasaki.marketfinder.dto.SearchResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    private static final int MAX_KEYWORD_LENGTH = 50;

    private final GitHubApiClient gitHubApiClient;

    public SearchService(GitHubApiClient gitHubApiClient) {
        this.gitHubApiClient = gitHubApiClient;
    }

    public SearchResponseDto search(String keyword, String language) {
        String normalizedKeyword = keyword == null ? "" : keyword.trim();
        String normalizedLanguage = language == null ? "" : language.trim();

        if (normalizedKeyword.isBlank()) {
            return new SearchResponseDto(
                    List.of(),
                    null,
                    "検索キーワードを入力してください。"
            );
        }

        if (normalizedKeyword.length() > MAX_KEYWORD_LENGTH) {
            return new SearchResponseDto(
                    List.of(),
                    null,
                    "検索キーワードは50文字以内で入力してください。"
            );
        }

        try {
            List<GitHubRepositoryDto> results =
                    gitHubApiClient.searchRepositories(normalizedKeyword, normalizedLanguage);

            return new SearchResponseDto(results, null, null);

        } catch (IllegalStateException e) {
            return new SearchResponseDto(
                    List.of(),
                    "検索中にエラーが発生しました。時間をおいて再度お試しください。",
                    null
            );
        }
    }
}