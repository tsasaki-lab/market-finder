package com.tsasaki.marketfinder.service;

import com.tsasaki.marketfinder.dto.SearchResponseDto;
import org.springframework.stereotype.Component;

/**
 * AI市場分析用のプロンプト生成を担当するビルダーです。
 *
 * <p>
 * GitHubリポジトリ情報、Issue情報、Market Finder独自スコアをもとに、
 * OpenAI APIへ渡す市場分析用プロンプトを生成します。
 * </p>
 */
@Component
public class AiSummaryPromptBuilder {

    /**
     * OpenAI APIへ渡す市場分析用プロンプトを生成します。
     *
     * @param response 検索結果
     * @return 市場分析用プロンプト
     */
    public String build(SearchResponseDto response) {

        StringBuilder prompt = new StringBuilder();

        prompt.append("""
                あなたは日本国内向けSaaS市場分析の専門家です。

                以下のGitHubリポジトリ情報、Issue情報、Market Finder独自スコアをもとに、
                開発者向けSaaSの市場機会を分析してください。

                分析方針:
                - 単にStars数が多いRepositoryだけでなく、Market Scoreが高いRepositoryを優先してSaaS機会を判断してください。
                - Pain Level Scoreが高い場合は、開発者課題が強い領域として扱ってください。
                - Demand Scoreが高い場合は、SaaS需要が見込める領域として扱ってください。
                - Activity Scoreが高い場合は、現在も開発や議論が活発な市場として扱ってください。
                - Issue Keywordsは、開発者が頻繁に言及している課題・関心語として扱ってください。
                - 断定しすぎず、仮説として記述してください。

                Market Finder独自スコアの意味:
                - Market Score: Stars ScoreとFreshness Scoreをもとにした総合的な市場性スコア
                - Stars Score: GitHub Starsをもとにした人気度スコア
                - Freshness Score: 最終更新日時をもとにした鮮度・継続性スコア

                Trend Analysisの意味:
                - Popularity Score: GitHub Starsなどから見た人気度
                - Activity Score: 更新状況やIssue状況から見た活動度
                - Demand Score: Issue数などから見た需要の強さ
                - Pain Level Score: 未解決Issueなどから見た開発者課題の強さ
                - Trend Level: 総合的なトレンド評価

                Issue Summary / Issue Keywordsの意味:
                - Issue Summaryは、検索対象に関連するIssue全体の件数や状態を示します。
                - Open Countが多い場合は、未解決課題が多い可能性があります。
                - Recently Updated Countが多い場合は、現在も議論や改善が続いている可能性があります。
                - Issue Keywordsは、開発者がIssueで頻繁に言及している課題・関心語です。

                出力条件:
                - 必ず自然な日本語で出力する
                - 中国語、簡体字、繁体字を使わない
                - 英語表現を不必要に混在させない
                - 技術用語は日本国内で一般的な表現を使う
                - 箇条書きは禁止
                - Markdownは禁止
                - JSON以外の文章、見出し、コードブロックは禁止

                出力形式:
                必ず次のJSON形式のみで出力してください。

                {
                  "marketSummary": "市場全体の要約を120文字以内で記述",
                  "developerPain": "開発者が抱える課題を80文字以内で記述",
                  "technologyTrend": "技術トレンドを80文字以内で記述",
                  "saasOpportunity": "SaaSとしての事業機会を80文字以内で記述"
                }

                JSONのキー名は必ず変更しないでください。
                値は必ず自然な日本語で記述してください。

                用語ルール:
                - 「多租户」は使わず「マルチテナント」と書く
                - 「微服务」は使わず「マイクロサービス」と書く
                - 「离线优先」は使わず「オフラインファースト」と書く
                - 「工具」は使わず「ツール」と書く
                - 「知识」は使わず「ナレッジ」と書く
                - 「自动化」は使わず「自動化」と書く

                """);

        prompt.append("検索対象情報\n");
        prompt.append("------------------------------\n");

        prompt.append("リポジトリ件数: ")
                .append(response.results().size())
                .append("\n");

        prompt.append("Issue件数: ")
                .append(response.issues().size())
                .append("\n\n");

        prompt.append("Trend Analysis:\n");
        prompt.append("- Average Market Score: ")
                .append(response.trendAnalysis().averageMarketScore())
                .append("\n");
        prompt.append("- Popularity Score: ")
                .append(response.trendAnalysis().popularityScore())
                .append("\n");
        prompt.append("- Activity Score: ")
                .append(response.trendAnalysis().activityScore())
                .append("\n");
        prompt.append("- Demand Score: ")
                .append(response.trendAnalysis().demandScore())
                .append("\n");
        prompt.append("- Pain Level Score: ")
                .append(response.trendAnalysis().painLevelScore())
                .append("\n");
        prompt.append("- Trend Level: ")
                .append(response.trendAnalysis().trendLevel())
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

        prompt.append("Issue Summary:\n");
        prompt.append("- Issue Count: ")
                .append(response.issueSummary().issueCount())
                .append("\n");
        prompt.append("- Open Count: ")
                .append(response.issueSummary().openCount())
                .append("\n");
        prompt.append("- Closed Count: ")
                .append(response.issueSummary().closedCount())
                .append("\n");
        prompt.append("- Recently Updated Count: ")
                .append(response.issueSummary().recentlyUpdatedCount())
                .append("\n\n");

        prompt.append("Issue Keywords:\n");
        response.issueKeywords().stream()
                .limit(8)
                .forEach(keyword -> prompt.append("- ")
                        .append(keyword.keyword())
                        .append(": ")
                        .append(keyword.count())
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
