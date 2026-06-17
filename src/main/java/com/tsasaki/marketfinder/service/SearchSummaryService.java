package com.tsasaki.marketfinder.service;

import com.tsasaki.marketfinder.dto.GitHubRepositoryDto;
import com.tsasaki.marketfinder.dto.SearchSummaryDto;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * リポジトリ検索結果全体のサマリーを作成するサービスです。
 */
@Service
public class SearchSummaryService {

    /**
     * 検索結果件数、平均Market Score、最高Market Score、合計Starsを算出します。
     *
     * @param results リポジトリ検索結果
     * @return 検索結果サマリー
     */
    public SearchSummaryDto summarize(List<GitHubRepositoryDto> results) {
        if (results == null || results.isEmpty()) {
            return new SearchSummaryDto(0, 0.0, 0, 0);
        }

        int resultCount = results.size();

        double averageMarketScore = results.stream()
                .mapToInt(GitHubRepositoryDto::marketScore)
                .average()
                .orElse(0.0);

        int maxMarketScore = results.stream()
                .mapToInt(GitHubRepositoryDto::marketScore)
                .max()
                .orElse(0);

        int totalStars = results.stream()
                .mapToInt(GitHubRepositoryDto::stars)
                .sum();

        return new SearchSummaryDto(
                resultCount,
                averageMarketScore,
                maxMarketScore,
                totalStars
        );
    }
}
