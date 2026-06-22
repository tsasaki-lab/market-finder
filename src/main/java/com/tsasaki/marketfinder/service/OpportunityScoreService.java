package com.tsasaki.marketfinder.service;

import com.tsasaki.marketfinder.dto.GitHubRepositoryDto;
import com.tsasaki.marketfinder.dto.IssueSummaryDto;
import com.tsasaki.marketfinder.dto.OpportunityDto;
import com.tsasaki.marketfinder.dto.TrendAnalysisDto;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * SaaS化機会スコアを算出するサービスです。
 */
@Service
public class OpportunityScoreService {

    /**
     * リポジトリ情報、トレンド分析、Issue概要をもとにSaaS化機会を算出します。
     *
     * @param repositories  リポジトリ一覧
     * @param trendAnalysis トレンド分析結果
     * @param issueSummary  Issue概要
     * @return SaaS化機会ランキング
     */
    public List<OpportunityDto> calculate(
            List<GitHubRepositoryDto> repositories,
            TrendAnalysisDto trendAnalysis,
            IssueSummaryDto issueSummary
    ) {
        if (repositories == null || repositories.isEmpty()) {
            return List.of();
        }

        return repositories.stream()
                .map(repository -> toOpportunity(repository, trendAnalysis, issueSummary))
                .sorted(Comparator.comparingDouble(OpportunityDto::opportunityScore).reversed())
                .toList();
    }

    private OpportunityDto toOpportunity(
            GitHubRepositoryDto repository,
            TrendAnalysisDto trendAnalysis,
            IssueSummaryDto issueSummary
    ) {
        double marketScore = repository.marketScore();
        double demandScore = trendAnalysis.demandScore();
        double painLevelScore = trendAnalysis.painLevelScore();
        double activityScore = trendAnalysis.activityScore();
        double openIssueRatio = calculateOpenIssueRatio(issueSummary);

        double score =
                marketScore * 0.4
                        + demandScore * 0.2
                        + painLevelScore * 0.2
                        + activityScore * 0.1
                        + openIssueRatio * 0.1;

        return new OpportunityDto(
                repository.name(),
                repository.url(),
                round(score),
                toLevel(score),
                buildReason(score, demandScore, painLevelScore, activityScore)
        );
    }

    private double calculateOpenIssueRatio(IssueSummaryDto issueSummary) {
        if (issueSummary == null || issueSummary.issueCount() == 0) {
            return 0;
        }

        return ((double) issueSummary.openCount() / issueSummary.issueCount()) * 100;
    }

    private String toLevel(double score) {
        if (score >= 80) {
            return "High";
        }
        if (score >= 60) {
            return "Medium";
        }
        return "Low";
    }

    private String buildReason(
            double score,
            double demandScore,
            double painLevelScore,
            double activityScore
    ) {
        if (score >= 80) {
            return "市場性、需要、課題の強さが高く、SaaS化候補として有望です。";
        }
        if (demandScore >= 70 || painLevelScore >= 70) {
            return "需要または開発者課題が見られるため、特定課題に絞ればSaaS化の余地があります。";
        }
        if (activityScore >= 70) {
            return "活動は見られますが、需要や課題の強さを追加検証する必要があります。";
        }
        return "現時点ではSaaS化機会は限定的です。";
    }

    private double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}
