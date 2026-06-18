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

        prompt.append("""
                あなたは日本国内向けSaaS市場分析の専門家です。

                以下のGitHubリポジトリ情報とIssue情報をもとに、
                開発者向けSaaSの市場機会を分析してください。

                出力条件:
                - 必ず自然な日本語で出力する
                - 中国語、簡体字、繁体字を使わない
                - 英語表現を不必要に混在させない
                - 技術用語は日本国内で一般的な表現を使う
                - 断定しすぎず、仮説として記述する
                - 200文字以上350文字以内
                - 箇条書きは禁止
                - 自然な文章として出力する

                必ず以下を含める:
                1. 開発者が抱えている課題
                2. 技術トレンド
                3. SaaSとしての事業機会

                表現ルール:
                - 「多租户」は使わず「マルチテナント」と書く
                - 「微服务」は使わず「マイクロサービス」と書く
                - 「工具」は使わず「ツール」と書く
                - 「知识」は使わず「ナレッジ」と書く
                - 「自动化」は使わず「自動化」と書く

                出力形式:
                市場インサイト本文のみを出力してください。
                見出しは禁止です。
                箇条書きは禁止です。
                Markdownは禁止です。
                コードブロックは禁止です。

                """);
        
        prompt.append("""
                用語ルール:

                - 中国語は禁止
                - 簡体字は禁止
                - 繁体字は禁止

                技術用語は日本国内で一般的な表現を使うこと

                NG -> OK

                多租户 -> マルチテナント
                微服务 -> マイクロサービス
                离线优先 -> オフラインファースト
                工具 -> ツール
                知识 -> ナレッジ
                自动化 -> 自動化

                """);

        prompt.append("""
                出力条件:

                - 必ず自然な日本語
                - 中国語禁止
                - 英語混在禁止
                - 120〜220文字以内
                - 2〜3文でまとめる
                - 冗長な説明は禁止
                - 市場分析の要点のみ記載

                必ず以下を含める

                1. 開発者課題
                2. 技術トレンド
                3. SaaS機会

                """);

        prompt.append("検索対象情報\n");
        prompt.append("------------------------------\n");

        prompt.append("リポジトリ件数: ")
                .append(response.results().size())
                .append("\n");

        prompt.append("Issue件数: ")
                .append(response.issues().size())
                .append("\n\n");

        prompt.append("上位リポジトリ:\n");

        response.results().stream()
                .limit(5)
                .forEach(repository ->
                        prompt.append("- ")
                                .append(repository.fullName())
                                .append(" | Stars=")
                                .append(repository.stars())
                                .append(" | Language=")
                                .append(repository.language())
                                .append(" | Description=")
                                .append(repository.description())
                                .append("\n")
                );

        prompt.append("\n");

        prompt.append("主要Issue:\n");

        response.issues().stream()
                .limit(5)
                .forEach(issue ->
                        prompt.append("- ")
                                .append(issue.title())
                                .append(" | State=")
                                .append(issue.state())
                                .append("\n")
                );

        return prompt.toString();
    }
}
