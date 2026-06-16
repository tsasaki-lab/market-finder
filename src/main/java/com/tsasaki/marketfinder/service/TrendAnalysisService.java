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

        String trendLevel = calculateTrendLevel(
                averageScore,
                averageStars,
                openIssues
        );

        return new TrendAnalysisDto(
                averageScore,
                averageStars,
                openIssues,
                recentIssues,
                trendLevel
        );
    }

    private String calculateTrendLevel(
            double averageScore,
            int averageStars,
            int openIssues
    ) {

        if (averageScore >= 80 && averageStars >= 10000) {
            return "HOT 🔥";
        }

        if (averageScore >= 60) {
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
}