package com.tsasaki.marketfinder.dto;

public record IssueSummaryDto(
        int issueCount,
        int openCount,
        int closedCount,
        int recentlyUpdatedCount
) {
}