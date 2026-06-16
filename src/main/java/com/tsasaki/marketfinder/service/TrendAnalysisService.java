package com.tsasaki.marketfinder.service;

import com.tsasaki.marketfinder.dto.GitHubIssueDto;
import com.tsasaki.marketfinder.dto.GitHubRepositoryDto;
import com.tsasaki.marketfinder.dto.TrendAnalysisDto;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class TrendAnalysisService {

    public TrendAnalysisDto analyze(
            List<GitHubRepositoryDto> repositories,
            List<GitHubIssueDto> issues
    ) {

        double averageScore = repositories.stream()
                .mapToInt(GitHubRepositoryDto::marketScore)
                .average()
                .orElse(0);

        int averageStars = (int) repositories.stream()
                .mapToInt(GitHubRepositoryDto::stars)
                .average()
                .orElse(0);

        int openIssues = (int) issues.stream()
                .filter(i -> "open".equalsIgnoreCase(i.state()))
                .count();

        int recentIssues = (int) issues.stream()
                .filter(this::isRecent)
                .count();

        int popularityScore =
                calculatePopularityScore(
                        averageStars
                );

        int demandScore =
                calculateDemandScore(
                        recentIssues
                );

        int painLevelScore =
                calculatePainLevelScore(
                        openIssues
                );

        int activityScore =
                calculateActivityScore(
                        averageScore
                );

        int trendScore =
                (
                        popularityScore +
                                activityScore +
                                demandScore +
                                painLevelScore
                ) / 4;

        String trendLevel = calculateTrendLevel(
                trendScore
        );

        return new TrendAnalysisDto(
                averageScore,
                averageStars,
                openIssues,
                recentIssues,

                popularityScore,
                activityScore,
                demandScore,
                painLevelScore,

                trendLevel
        );
    }

    private String calculateTrendLevel(
            int trendScore
    ) {

        if (trendScore >= 80) {
            return "HOT 🔥";
        }

        if (trendScore >= 60) {
            return "WARM ☀️";
        }

        return "COLD ❄️";
    }

    private boolean isRecent(GitHubIssueDto issue) {

        try {
            OffsetDateTime updated =
                    OffsetDateTime.parse(issue.updatedAt());

            long days =
                    ChronoUnit.DAYS.between(
                            updated,
                            OffsetDateTime.now()
                    );

            return days <= 30;

        } catch (Exception e) {
            return false;
        }
    }

    private int calculatePopularityScore(
            int averageStars
    ) {
        return Math.min(
                averageStars / 500,
                100
        );
    }

    private int calculateDemandScore(
            int recentIssues
    ) {
        return Math.min(
                recentIssues * 10,
                100
        );
    }

    private int calculatePainLevelScore(
            int openIssues
    ) {
        return Math.min(
                openIssues * 5,
                100
        );
    }

    private int calculateActivityScore(
            double averageMarketScore
    ) {
        return (int) averageMarketScore;
    }
}