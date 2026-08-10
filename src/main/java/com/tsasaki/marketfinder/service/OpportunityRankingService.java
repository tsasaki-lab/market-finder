package com.tsasaki.marketfinder.service;

import com.tsasaki.marketfinder.dto.CompetitionAnalysisDto;
import com.tsasaki.marketfinder.dto.OpportunityDto;
import com.tsasaki.marketfinder.dto.OpportunityRankingDto;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * SaaS機会の優先度ランキングを生成するサービスです。
 */
@Service
public class OpportunityRankingService {

    /**
     * SaaS機会と競合分析結果をもとに、優先度ランキングを生成します。
     *
     * @param opportunities       SaaS機会一覧
     * @param competitionAnalysis 競合分析結果
     * @return 優先度ランキング一覧
     */
    public List<OpportunityRankingDto> rank(
            List<OpportunityDto> opportunities,
            CompetitionAnalysisDto competitionAnalysis
    ) {
        if (opportunities == null || opportunities.isEmpty()) {
            return List.of();
        }

        int competitionScore = competitionAnalysis == null
                ? 0
                : competitionAnalysis.competitionScore();

        return opportunities.stream()
                .map(opportunity -> toRanking(opportunity, competitionScore))
                .sorted(Comparator.comparingInt(OpportunityRankingDto::rankingScore).reversed())
                .toList();
    }

    private OpportunityRankingDto toRanking(OpportunityDto opportunity, int competitionScore) {
        double opportunityScore = opportunity.opportunityScore();
        int rankingScore = calculateRankingScore(opportunityScore, competitionScore);

        return new OpportunityRankingDto(
                opportunity.repositoryName(),
                determinePriority(rankingScore),
                rankingScore,
                opportunityScore,
                competitionScore,
                buildReason(opportunityScore, competitionScore, rankingScore)
        );
    }

    private int calculateRankingScore(double opportunityScore, int competitionScore) {
        int competitionPenalty = calculateCompetitionPenalty(competitionScore);
        int score = (int) Math.round(opportunityScore - competitionPenalty);

        return Math.max(0, Math.min(100, score));
    }

    private int calculateCompetitionPenalty(int competitionScore) {
        if (competitionScore >= 80) {
            return 30;
        }

        if (competitionScore >= 60) {
            return 20;
        }

        if (competitionScore >= 40) {
            return 10;
        }

        return 0;
    }

    private String determinePriority(int rankingScore) {
        if (rankingScore >= 80) {
            return "High";
        }

        if (rankingScore >= 60) {
            return "Medium";
        }

        if (rankingScore >= 40) {
            return "Low";
        }

        return "Watch";
    }

    private String buildReason(double opportunityScore, int competitionScore, int rankingScore) {
        if (rankingScore >= 80) {
            return "需要が高く、競合リスクを考慮しても優先度が高い市場機会です。";
        }

        if (opportunityScore >= 80 && competitionScore >= 70) {
            return "需要は高い一方で競合も強いため、差別化戦略が重要です。";
        }

        if (opportunityScore >= 60 && competitionScore < 60) {
            return "一定の需要があり、競合も過度に強くないため検証候補になります。";
        }

        if (competitionScore >= 80) {
            return "競合が強いため、参入する場合は明確なニッチ特化が必要です。";
        }

        return "現時点では優先度は高くありませんが、継続観察する価値があります。";
    }
}
