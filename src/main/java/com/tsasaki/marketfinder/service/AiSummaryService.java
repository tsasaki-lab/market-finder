package com.tsasaki.marketfinder.service;

import com.tsasaki.marketfinder.client.OpenAiSummaryClient;
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

    private final OpenAiSummaryClient openAiSummaryClient;

    public AiSummaryService(OpenAiSummaryClient openAiSummaryClient) {
        this.openAiSummaryClient = openAiSummaryClient;
    }

    /**
     * 検索結果からAI市場分析を生成します。
     *
     * @param response GitHub検索結果
     * @return AI市場分析結果
     */
    public AiSummaryDto generate(SearchResponseDto response) {
        if (response == null || response.results().isEmpty()) {
            return AiSummaryDto.unavailable();
        }

        String prompt = buildPrompt(response);
        String marketSummary = openAiSummaryClient.generate(prompt);

        if (marketSummary == null || marketSummary.isBlank()) {
            return AiSummaryDto.unavailable();
        }

        return AiSummaryDto.of(
                marketSummary,
                List.of(
                        "AIが市場概要を生成しました",
                        "詳細なトレンド抽出は次フェーズで対応予定です"
                ),
                List.of(
                        "AI SummaryをもとにSaaS仮説を検討できます"
                )
        );
    }

    /**
     * OpenAI APIへ渡す市場分析用プロンプトを生成します。
     *
     * @param response 検索結果
     * @return 市場分析用プロンプト
     */
    private String buildPrompt(SearchResponseDto response) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("あなたはSaaS市場分析の専門家です。\n");
        prompt.append("以下のGitHub検索結果をもとに、日本語で市場分析サマリーを作成してください。\n\n");

        prompt.append("条件:\n");
        prompt.append("- 200文字以内\n");
        prompt.append("- 開発者の課題、技術トレンド、SaaS機会を含める\n");
        prompt.append("- 断定しすぎず、仮説として表現する\n\n");

        prompt.append("リポジトリ件数: ")
                .append(response.results().size())
                .append("\n");

        prompt.append("Issue件数: ")
                .append(response.issues().size())
                .append("\n\n");

        prompt.append("上位リポジトリ:\n");

        response.results().stream()
                .limit(5)
                .forEach(repository -> prompt.append("- ")
                        .append(repository.fullName())
                        .append(" / Stars: ")
                        .append(repository.stars())
                        .append(" / Language: ")
                        .append(repository.language())
                        .append(" / Description: ")
                        .append(repository.description())
                        .append("\n"));

        prompt.append("\n主要Issue:\n");

        response.issues().stream()
                .limit(5)
                .forEach(issue -> prompt.append("- ")
                        .append(issue.title())
                        .append(" / State: ")
                        .append(issue.state())
                        .append("\n"));

        return prompt.toString();
    }
}
