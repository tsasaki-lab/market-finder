package com.tsasaki.marketfinder.service;

import com.tsasaki.marketfinder.client.GitHubApiClient;
import com.tsasaki.marketfinder.dto.GitHubRepositoryDto;
import com.tsasaki.marketfinder.dto.SearchResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    private final GitHubApiClient gitHubApiClient;

    public SearchService(GitHubApiClient gitHubApiClient) {
        this.gitHubApiClient = gitHubApiClient;
    }

    public SearchResponseDto search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return new SearchResponseDto(List.of(), null);
        }

        try {
            List<GitHubRepositoryDto> results =
                    gitHubApiClient.searchRepositories(keyword);

            return new SearchResponseDto(results, null);

        } catch (IllegalStateException e) {
            return new SearchResponseDto(
                    List.of(),
                    "検索中にエラーが発生しました。時間をおいて再度お試しください。"
            );
        }
    }
}