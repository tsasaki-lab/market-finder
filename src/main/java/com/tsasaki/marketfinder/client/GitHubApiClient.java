package com.tsasaki.marketfinder.client;

import com.tsasaki.marketfinder.dto.GitHubRepositoryDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import com.tsasaki.marketfinder.service.MarketScoreService;

import java.util.List;
import java.util.Map;

@Component
public class GitHubApiClient {

    private final RestClient restClient;
    private final String token;
    private final MarketScoreService marketScoreService;

    public GitHubApiClient(
            @Value("${github.api.token}") String token,
            MarketScoreService marketScoreService
    ) {
        this.token = token;
        this.marketScoreService = marketScoreService;
        this.restClient = RestClient.builder()
                .baseUrl("https://api.github.com")
                .build();
    }

    public List<GitHubRepositoryDto> searchRepositories(String keyword, String language) {
        try {
            String query = StringUtils.hasText(language)
                    ? keyword + " language:" + language
                    : keyword;

            Map response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search/repositories")
                            .queryParam("q", query)
                            .queryParam("sort", "stars")
                            .queryParam("order", "desc")
                            .queryParam("per_page", 5)
                            .build())
                    .header("Authorization", "Bearer " + token)
                    .header("Accept", "application/vnd.github+json")
                    .retrieve()
                    .body(Map.class);

            if (response == null || response.get("items") == null) {
                return List.of();
            }

            List<Map<String, Object>> items =
                    (List<Map<String, Object>>) response.get("items");

            return items.stream()
                    .map(item -> {
                        int stars = (Integer) item.get("stargazers_count");
                        String updatedAt = (String) item.get("updated_at");

                        return new GitHubRepositoryDto(
                                (String) item.get("name"),
                                (String) item.get("full_name"),
                                (String) item.get("html_url"),
                                (String) item.get("description"),
                                stars,
                                (String) item.get("language"),
                                updatedAt,
                                marketScoreService.calculate(stars, updatedAt)
                        );
                    })
                    .toList();

        } catch (RestClientException e) {
            throw new IllegalStateException("GitHub APIの呼び出しに失敗しました。", e);
        }
    }
}