package com.tsasaki.marketfinder.client;

import com.tsasaki.marketfinder.dto.GitHubIssueDto;
import com.tsasaki.marketfinder.dto.GitHubRepositoryDto;
import com.tsasaki.marketfinder.dto.MarketScoreDto;
import com.tsasaki.marketfinder.service.MarketScoreService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;

/**
 * GitHub REST API を呼び出すクライアントです。
 *
 * <p>
 * Repository検索とIssue検索を担当します。
 * APIトークンは環境変数から読み込まれるため、ソースコードには直接保持しません。
 * </p>
 */
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

    /**
     * 指定されたキーワードと言語でGitHubリポジトリを検索します。
     *
     * @param keyword 検索キーワード
     * @param language 絞り込み対象のプログラミング言語
     * @return GitHubリポジトリ検索結果
     */
    public List<GitHubRepositoryDto> searchRepositories(String keyword, String language) {
        try {
            // GitHub Search APIでは language:Java のような修飾子で言語を絞り込む。
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

                        MarketScoreDto marketScore =
                                marketScoreService.calculate(stars, updatedAt);

                        return new GitHubRepositoryDto(
                                (String) item.get("name"),
                                (String) item.get("full_name"),
                                (String) item.get("html_url"),
                                (String) item.get("description"),
                                stars,
                                (String) item.get("language"),
                                updatedAt,
                                marketScore.totalScore(),
                                marketScore.starsScore(),
                                marketScore.freshnessScore()
                        );
                    })
                    .toList();

        } catch (RestClientException e) {
            throw new IllegalStateException("GitHub APIの呼び出しに失敗しました。", e);
        }
    }

    /**
     * 指定されたキーワードと言語でGitHub Issueを検索します。
     *
     * @param keyword 検索キーワード
     * @param language 絞り込み対象のプログラミング言語
     * @return GitHub Issue検索結果
     */
    public List<GitHubIssueDto> searchIssues(String keyword, String language) {
        try {
            String query = StringUtils.hasText(language)
                    ? keyword + " language:" + language + " type:issue"
                    : keyword + " type:issue";

            Map response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search/issues")
                            .queryParam("q", query)
                            .queryParam("sort", "updated")
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
                        Map<String, Object> repository =
                                (Map<String, Object>) item.get("repository");

                        String repositoryName = repository == null
                                ? "Unknown"
                                : (String) repository.get("full_name");

                        return new GitHubIssueDto(
                                (String) item.get("title"),
                                (String) item.get("html_url"),
                                repositoryName,
                                (String) item.get("state"),
                                (String) item.get("created_at"),
                                (String) item.get("updated_at")
                        );
                    })
                    .toList();

        } catch (RestClientException e) {
            throw new IllegalStateException("GitHub Issues APIの呼び出しに失敗しました。", e);
        }
    }
}
