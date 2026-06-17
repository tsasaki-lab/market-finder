package com.tsasaki.marketfinder.service;

import com.tsasaki.marketfinder.dto.MarketScoreDto;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

/**
 * GitHubリポジトリの市場スコアを算出するサービスです。
 *
 * <p>
 * 現時点では、人気度を示すStarsと、更新頻度を示すupdatedAtを使って
 * 簡易的なMarket Scoreを算出します。
 * </p>
 */
@Service
public class MarketScoreService {

    /**
     * Starsと更新日時からMarket Scoreを算出します。
     *
     * @param stars GitHub Stars数
     * @param updatedAt GitHubリポジトリの最終更新日時
     * @return Market Scoreとその内訳
     */
    public MarketScoreDto calculate(int stars, String updatedAt) {
        int starsScore = calculateStarScore(stars);
        int freshnessScore = calculateFreshnessScore(updatedAt);
        int totalScore = Math.min(starsScore + freshnessScore, 100);

        return new MarketScoreDto(
                totalScore,
                starsScore,
                freshnessScore
        );
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
