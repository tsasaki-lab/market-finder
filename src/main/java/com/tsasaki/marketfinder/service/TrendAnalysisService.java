package com.tsasaki.marketfinder.service;

import com.tsasaki.marketfinder.dto.GitHubIssueDto;
import com.tsasaki.marketfinder.dto.GitHubRepositoryDto;
import com.tsasaki.marketfinder.dto.TrendAnalysisDto;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * リポジトリ情報とIssue情報をもとに、市場トレンドを分析するサービスです。
 *
 * <p>
 * Popularity、Activity、Demand、Pain Level の4指標から
 * HOT / WARM / COLD のトレンドレベルを判定します。
 * </p>
 */
@Service
public class TrendAnalysisService {

    /**
     * GitHubリポジトリ検索結果とIssue検索結果からトレンド分析を行います。
     *
     * @param repositories リポジトリ検索結果
     * @param issues Issue検索結果
     * @return トレンド分析結果
     */
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
                .filter(issue -> "open".equalsIgnoreCase(issue.state()))
                .count();

        int recentIssues = (int) issues.stream()
                .filter(this::isRecent)
                .count();

        int popularityScore = calculatePopularityScore(averageStars);
        int activityScore = calculateActivityScore(averageScore);
        int demandScore = calculateDemandScore(recentIssues);
        int painLevelScore = calculatePainLevelScore(openIssues);

        String trendLevel = calculateTrendLevel(
                popularityScore,
                activityScore,
                demandScore,
                painLevelScore
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

    private int calculatePopularityScore(int averageStars) {
        return Math.min(averageStars / 500, 100);
    }

    private int calculateActivityScore(double averageMarketScore) {
        return (int) averageMarketScore;
    }

    private int calculateDemandScore(int recentIssues) {
        return Math.min(recentIssues * 10, 100);
    }

    private int calculatePainLevelScore(int openIssues) {
        return Math.min(openIssues * 5, 100);
    }

    private String calculateTrendLevel(
            int popularityScore,
            int activityScore,
            int demandScore,
            int painLevelScore
    ) {
        int trendScore = (
                popularityScore +
                        activityScore +
                        demandScore +
                        painLevelScore
        ) / 4;

        if (trendScore >= 80) {
            return "HOT 🔥";
        }

        if (trendScore >= 60) {
            return "WARM ☀️";
        }

        return "COLD ❄️";
    }

    private boolean isRecent(GitHubIssueDto issue) {
        if (issue.updatedAt() == null || issue.updatedAt().isBlank()) {
            return false;
        }

        try {
            OffsetDateTime updated = OffsetDateTime.parse(issue.updatedAt());
            long days = ChronoUnit.DAYS.between(
                    updated,
                    OffsetDateTime.now()
            );

            return days <= 30;

        } catch (Exception e) {
            return false;
        }
    }
}
