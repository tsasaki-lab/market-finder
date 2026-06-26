package com.tsasaki.marketfinder.dto;

import java.util.List;

/**
 * GitHub競合分析の結果を表すDTO。
 *
 * @param competitionScore     競合の強さを表すスコア
 * @param competitionLevel     競争レベル
 * @param marketMaturity       市場成熟度
 * @param entryDifficulty      新規参入難易度
 * @param competitors          主要競合リポジトリ一覧
 * @param differentiationHints 差別化の方向性
 */
public record CompetitionAnalysisDto(
        int competitionScore,
        String competitionLevel,
        String marketMaturity,
        String entryDifficulty,
        List<CompetitorDto> competitors,
        List<String> differentiationHints
) {

    /**
     * 競合分析結果が存在しない場合の空DTOを生成する。
     *
     * @return 空の競合分析DTO
     */
    public static CompetitionAnalysisDto empty() {
        return new CompetitionAnalysisDto(
                0,
                "Unknown",
                "Unknown",
                "Unknown",
                List.of(),
                List.of()
        );
    }
}
