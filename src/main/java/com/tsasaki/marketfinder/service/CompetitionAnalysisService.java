package com.tsasaki.marketfinder.service;

import com.tsasaki.marketfinder.dto.CompetitionAnalysisDto;
import com.tsasaki.marketfinder.dto.CompetitorDto;
import com.tsasaki.marketfinder.dto.GitHubRepositoryDto;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * GitHubリポジトリの競合状況を分析するサービスです。
 */
@Service
public class CompetitionAnalysisService {

    /**
     * 表示する競合リポジトリ数です。
     */
    private static final int MAX_COMPETITORS = 5;

    /**
     * GitHubリポジトリ一覧から競合分析を行います。
     *
     * @param repositories GitHubリポジトリ一覧
     * @return 競合分析結果
     */
    public CompetitionAnalysisDto analyze(List<GitHubRepositoryDto> repositories) {

        if (repositories == null || repositories.isEmpty()) {
            return CompetitionAnalysisDto.empty();
        }

        List<CompetitorDto> competitors = repositories.stream()
                .sorted(Comparator.comparingInt(GitHubRepositoryDto::stars).reversed())
                .limit(MAX_COMPETITORS)
                .map(this::toCompetitor)
                .toList();

        int competitionScore = calculateCompetitionScore(competitors);

        return new CompetitionAnalysisDto(
                competitionScore,
                determineCompetitionLevel(competitionScore),
                determineMarketMaturity(competitionScore),
                determineEntryDifficulty(competitionScore),
                competitors,
                buildDifferentiationHints(competitionScore)
        );
    }

    /**
     * GitHubRepositoryDtoを競合リポジトリDTOへ変換します。
     *
     * @param repository GitHubリポジトリ
     * @return 競合リポジトリDTO
     */
    private CompetitorDto toCompetitor(GitHubRepositoryDto repository) {

        return new CompetitorDto(
                repository.fullName(),
                repository.url(),
                repository.description(),
                repository.language(),
                repository.stars(),
                repository.marketScore()
        );
    }

    /**
     * 競合スコアを算出します。
     *
     * @param competitors 競合一覧
     * @return 競合スコア
     */
    private int calculateCompetitionScore(List<CompetitorDto> competitors) {

        int topStars = competitors.stream()
                .mapToInt(CompetitorDto::stars)
                .max()
                .orElse(0);

        int totalStars = competitors.stream()
                .mapToInt(CompetitorDto::stars)
                .sum();

        double averageMarketScore = competitors.stream()
                .mapToInt(CompetitorDto::marketScore)
                .average()
                .orElse(0);

        int score = 0;

        score += calculateTopStarsScore(topStars);
        score += calculateTotalStarsScore(totalStars);
        score += calculateRepositoryCountScore(competitors.size());
        score += calculateMarketScore(averageMarketScore);

        return Math.min(score, 100);
    }

    /**
     * 最大Stars数による評価です。
     */
    private int calculateTopStarsScore(int stars) {

        if (stars >= 50_000) return 35;
        if (stars >= 10_000) return 28;
        if (stars >= 3_000) return 20;
        if (stars >= 1_000) return 12;
        if (stars >= 300) return 6;

        return 0;
    }

    /**
     * 合計Stars数による評価です。
     */
    private int calculateTotalStarsScore(int stars) {

        if (stars >= 100_000) return 30;
        if (stars >= 30_000) return 24;
        if (stars >= 10_000) return 18;
        if (stars >= 3_000) return 12;
        if (stars >= 1_000) return 6;

        return 0;
    }

    /**
     * リポジトリ数による評価です。
     */
    private int calculateRepositoryCountScore(int repositoryCount) {

        if (repositoryCount >= 5) return 15;
        if (repositoryCount >= 3) return 10;
        if (repositoryCount >= 2) return 5;

        return 0;
    }

    /**
     * 平均Market Scoreによる評価です。
     */
    private int calculateMarketScore(double marketScore) {

        if (marketScore >= 80) return 20;
        if (marketScore >= 60) return 15;
        if (marketScore >= 40) return 10;

        return 0;
    }

    /**
     * 競争レベルを判定します。
     */
    private String determineCompetitionLevel(int score) {

        if (score >= 90) return "Very High";
        if (score >= 70) return "High";
        if (score >= 40) return "Medium";

        return "Low";
    }

    /**
     * 市場成熟度を判定します。
     */
    private String determineMarketMaturity(int score) {

        if (score >= 80) return "Mature";
        if (score >= 50) return "Growing";

        return "Emerging";
    }

    /**
     * 新規参入難易度を判定します。
     */
    private String determineEntryDifficulty(int score) {

        if (score >= 80) return "High";
        if (score >= 50) return "Medium";

        return "Low";
    }

    /**
     * 差別化の方向性を生成します。
     */
    private List<String> buildDifferentiationHints(int score) {

        if (score >= 80) {
            return List.of(
                    "AI機能を組み合わせる",
                    "運用自動化を提供する",
                    "チーム向け管理機能を追加する"
            );
        }

        if (score >= 50) {
            return List.of(
                    "特定業界向けに特化する",
                    "日本市場向けに最適化する",
                    "既存ツールとの連携を強化する"
            );
        }

        return List.of(
                "小さな課題から検証する",
                "個人開発者向けに提供する",
                "ニッチ市場を狙う"
        );
    }
}
