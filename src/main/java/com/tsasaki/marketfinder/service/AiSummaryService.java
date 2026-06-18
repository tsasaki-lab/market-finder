package com.tsasaki.marketfinder.service;

import com.tsasaki.marketfinder.client.OpenAiSummaryClient;
import com.tsasaki.marketfinder.dto.AiSummaryDto;
import com.tsasaki.marketfinder.dto.SearchResponseDto;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

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
    private final ObjectMapper objectMapper;

    public AiSummaryService(OpenAiSummaryClient openAiSummaryClient, ObjectMapper objectMapper) {
        this.openAiSummaryClient = openAiSummaryClient;
        this.objectMapper = objectMapper;
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
        String content = openAiSummaryClient.generate(prompt);

        if (content == null || content.isBlank()) {
            return AiSummaryDto.unavailable();
        }

        try {
            Map<String, String> result = objectMapper.readValue(
                    content,
                    new TypeReference<>() {
                    }
            );

            return AiSummaryDto.of(
                    result.getOrDefault("marketSummary", ""),
                    result.getOrDefault("developerPain", ""),
                    result.getOrDefault("technologyTrend", ""),
                    result.getOrDefault("saasOpportunity", ""),
                    List.of(),
                    List.of()
            );

        } catch (Exception e) {
            return AiSummaryDto.of(
                    content,
                    "AI分析結果の構造化に失敗しました。",
                    "AI分析結果の構造化に失敗しました。",
                    "AI分析結果の構造化に失敗しました。",
                    List.of(),
                    List.of()
            );
        }
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

                Market Finder独自スコアの意味:
                - Market Score: Stars ScoreとFreshness Scoreをもとにした総合的な市場性スコア
                - Stars Score: GitHub Starsをもとにした人気度スコア
                - Freshness Score: 最終更新日時をもとにした鮮度・継続性スコア

                分析では、単にStars数が多いRepositoryだけでなく、
                Market Scoreが高いRepositoryを優先してSaaS機会を判断してください。

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
                必ず次のJSON形式のみで出力してください。
                JSON以外の文章、見出し、Markdown、コードブロックは禁止です。

                {
                  "marketSummary": "市場全体の要約を120文字以内で記述",
                  "developerPain": "開発者が抱える課題を80文字以内で記述",
                  "technologyTrend": "技術トレンドを80文字以内で記述",
                  "saasOpportunity": "SaaSとしての事業機会を80文字以内で記述"
                }

                JSONのキー名は必ず変更しないでください。
                値は必ず自然な日本語で記述してください。
                中国語、簡体字、繁体字は禁止です。
                Markdownは禁止です。

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
                .forEach(repository -> prompt.append("- ")
                        .append(repository.fullName())
                        .append(" | Stars=")
                        .append(repository.stars())
                        .append(" | Language=")
                        .append(repository.language())
                        .append(" | Market Score=")
                        .append(repository.marketScore())
                        .append(" | Stars Score=")
                        .append(repository.starsScore())
                        .append(" | Freshness Score=")
                        .append(repository.freshnessScore())
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
