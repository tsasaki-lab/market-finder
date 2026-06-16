package com.tsasaki.marketfinder.service;

import com.tsasaki.marketfinder.dto.GitHubIssueDto;
import com.tsasaki.marketfinder.dto.IssueSummaryDto;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class IssueSummaryService {

    public IssueSummaryDto summarize(List<GitHubIssueDto> issues) {
        if (issues == null || issues.isEmpty()) {
            return new IssueSummaryDto(0, 0, 0, 0);
        }

        int issueCount = issues.size();

        int openCount = (int) issues.stream()
                .filter(issue -> "open".equalsIgnoreCase(issue.state()))
                .count();

        int closedCount = (int) issues.stream()
                .filter(issue -> "closed".equalsIgnoreCase(issue.state()))
                .count();

        int recentlyUpdatedCount = (int) issues.stream()
                .filter(this::isRecentlyUpdated)
                .count();

        return new IssueSummaryDto(
                issueCount,
                openCount,
                closedCount,
                recentlyUpdatedCount
        );
    }

    private boolean isRecentlyUpdated(GitHubIssueDto issue) {
        if (issue.updatedAt() == null || issue.updatedAt().isBlank()) {
            return false;
        }

        try {
            OffsetDateTime updatedDateTime = OffsetDateTime.parse(issue.updatedAt());
            long days = ChronoUnit.DAYS.between(
                    updatedDateTime,
                    OffsetDateTime.now()
            );

            return days <= 30;

        } catch (Exception e) {
            return false;
        }
    }
}