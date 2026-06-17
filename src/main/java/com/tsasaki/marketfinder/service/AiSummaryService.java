package com.tsasaki.marketfinder.service;

import com.tsasaki.marketfinder.dto.AiSummaryDto;
import com.tsasaki.marketfinder.dto.SearchResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * GitHub検索結果をもとに市場インサイトを生成するサービスです。
 *
 * <p>
 * 現在は仮実装として固定値を返し、
 * AI連携（OpenAI API）は次フェーズで実装します。
 * </p>
 */
@Service
public class AiSummaryService {

    /**
     * 検索結果からAI市場分析を生成します。
     *
     * @param response GitHub検索結果
     * @return AI市場分析結果
     */
    public AiSummaryDto generate(SearchResponseDto response) {

        // 仮実装（OpenAI未接続）
        return AiSummaryDto.of(
                "GitHub上では開発者向けツール領域の需要が継続的に増加しています。",
                List.of(
                        "開発者体験（DX）改善ツールの需要が高い",
                        "CI/CD・自動化関連リポジトリが活発",
                        "軽量SaaS系ツールが増加傾向"
                ),
                List.of(
                        "Issue分析ツールのSaaS化",
                        "GitHubデータ可視化ダッシュボード",
                        "開発者向けナレッジ自動生成ツール"
                )
        );
    }
}
