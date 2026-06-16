package com.tsasaki.marketfinder.service;

import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class MarketScoreService {

    public int calculate(int stars, String updatedAt) {
        int score = 0;

        score += calculateStarScore(stars);
        score += calculateFreshnessScore(updatedAt);

        return Math.min(score, 100);
    }

    private int calculateStarScore(int stars) {
        if (stars >= 100_000) {
            return 50;
        }
        if (stars >= 50_000) {
            return 40;
        }
        if (stars >= 10_000) {
            return 30;
        }
        if (stars >= 1_000) {
            return 20;
        }
        if (stars >= 100) {
            return 10;
        }
        return 0;
    }

    private int calculateFreshnessScore(String updatedAt) {
        if (updatedAt == null || updatedAt.isBlank()) {
            return 0;
        }

        try {
            OffsetDateTime updatedDateTime = OffsetDateTime.parse(updatedAt);
            long months = ChronoUnit.MONTHS.between(
                    updatedDateTime,
                    OffsetDateTime.now()
            );

            if (months <= 3) {
                return 40;
            }
            if (months <= 12) {
                return 30;
            }
            if (months <= 36) {
                return 10;
            }
            return 0;

        } catch (Exception e) {
            return 0;
        }
    }
}