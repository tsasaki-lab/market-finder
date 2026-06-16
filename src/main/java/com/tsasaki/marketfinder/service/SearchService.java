package com.tsasaki.marketfinder.service;

import com.tsasaki.marketfinder.client.GitHubApiClient;
import com.tsasaki.marketfinder.dto.GitHubRepositoryDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    private final GitHubApiClient gitHubApiClient;

    public SearchService(GitHubApiClient gitHubApiClient) {
        this.gitHubApiClient = gitHubApiClient;
    }

    public List<GitHubRepositoryDto> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return List.of();
        }

        return gitHubApiClient.searchRepositories(keyword);
    }
}