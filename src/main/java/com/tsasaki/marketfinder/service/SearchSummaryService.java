package com.tsasaki.marketfinder.service;

import com.tsasaki.marketfinder.dto.GitHubRepositoryDto;
import com.tsasaki.marketfinder.dto.SearchSummaryDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchSummaryService {

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