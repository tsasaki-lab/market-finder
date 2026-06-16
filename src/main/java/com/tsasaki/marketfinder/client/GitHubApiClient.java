package com.tsasaki.marketfinder.client;

import com.tsasaki.marketfinder.dto.GitHubRepositoryDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
public class GitHubApiClient {

    private final RestClient restClient;
    private final String token;

    public GitHubApiClient(@Value("${github.api.token}") String token) {
        this.token = token;
        this.restClient = RestClient.builder()
                .baseUrl("https://api.github.com")
                .build();
    }

    public List<GitHubRepositoryDto> searchRepositories(String keyword) {
        Map response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/repositories")
                        .queryParam("q", keyword)
                        .queryParam("sort", "stars")
                        .queryParam("order", "desc")
                        .queryParam("per_page", 5)
                        .build())
                .header("Authorization", "Bearer " + token)
                .header("Accept", "application/vnd.github+json")
                .retrieve()
                .body(Map.class);

        List<Map<String, Object>> items = (List<Map<String, Object>>) response.get("items");

        return items.stream()
                .map(item -> new GitHubRepositoryDto(
                        (String) item.get("name"),
                        (String) item.get("full_name"),
                        (String) item.get("html_url"),
                        (String) item.get("description"),
                        (Integer) item.get("stargazers_count")
                ))
                .toList();
    }
}