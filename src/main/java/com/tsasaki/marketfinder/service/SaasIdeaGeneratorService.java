package com.tsasaki.marketfinder.service;

import com.tsasaki.marketfinder.client.OpenAiSummaryClient;
import com.tsasaki.marketfinder.dto.OpportunityDto;
import com.tsasaki.marketfinder.dto.SaasIdeaDto;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

/**
 * Opportunity Score上位候補をもとにSaaSアイデアを生成するサービスです。
 *
 * <p>
 * Market Finderが算出したSaaS機会ランキングをもとに、
 * OpenAI APIを利用して具体的なSaaSアイデア、
 * ターゲットユーザー、解決する課題、主要機能、収益化案を生成します。
 * </p>
 */
@Service
public class SaasIdeaGeneratorService {

    private final OpenAiSummaryClient openAiSummaryClient;
    private final ObjectMapper objectMapper;

    public SaasIdeaGeneratorService(
            OpenAiSummaryClient openAiSummaryClient,
            ObjectMapper objectMapper
    ) {
        this.openAiSummaryClient = openAiSummaryClient;
        this.objectMapper = objectMapper;
    }

    /**
     * SaaS機会ランキングからSaaSアイデアを生成します。
     *
     * @param opportunities SaaS機会ランキング
     * @return AIが生成したSaaSアイデア一覧
     */
    public List<SaasIdeaDto> generate(List<OpportunityDto> opportunities) {
        if (opportunities == null || opportunities.isEmpty()) {
            return List.of();
        }

        List<OpportunityDto> topOpportunities =
                opportunities.stream()
                        .limit(3)
                        .toList();

        String prompt = buildPrompt(topOpportunities);
        String content = openAiSummaryClient.generate(prompt);

        if (content == null || content.isBlank()) {
            return List.of();
        }

        try {
            Map<String, List<Map<String, String>>> result = objectMapper.readValue(
                    content,
                    new TypeReference<>() {
                    }
            );

            return result.getOrDefault("ideas", List.of())
                    .stream()
                    .map(this::toSaasIdea)
                    .toList();

        } catch (Exception e) {
            return List.of();
        }
    }

    /**
     * AIへ渡すSaaSアイデア生成用プロンプトを作成します。
     *
     * @param opportunities SaaS機会ランキング上位候補
     * @return SaaSアイデア生成用プロンプト
     */
    private String buildPrompt(List<OpportunityDto> opportunities) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("""
                あなたは日本国内向けSaaSプロダクト企画の専門家です。

                以下のSaaS機会ランキングをもとに、
                実現可能なSaaSアイデアを3件生成してください。

                分析方針:
                - Opportunity Scoreが高い候補を優先してください。
                - 開発者が実際に困っていそうな課題に絞ってください。
                - 個人開発または小規模チームでも検証しやすいSaaS案にしてください。
                - 抽象的な案ではなく、具体的なプロダクト案にしてください。
                - 断定しすぎず、仮説として記述してください。

                出力条件:
                - 必ず自然な日本語で出力する
                - 中国語、簡体字、繁体字を使わない
                - 英語表現を不必要に混在させない
                - 技術用語は日本国内で一般的な表現を使う
                - Markdownは禁止
                - JSON以外の文章、見出し、コードブロックは禁止
                - ideasは必ず3件以内にしてください

                出力形式:
                必ず次のJSON形式のみで出力してください。

                {
                  "ideas": [
                    {
                      "ideaName": "SaaSアイデア名を30文字以内で記述",
                      "targetUser": "想定ターゲットユーザーを60文字以内で記述",
                      "problem": "解決する課題を80文字以内で記述",
                      "coreFeature": "主要機能を80文字以内で記述",
                      "monetization": "収益化案を80文字以内で記述"
                    }
                  ]
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

                SaaS機会ランキング:
                """);

        opportunities.forEach(opportunity -> prompt.append("""

                - Repository: %s
                  Opportunity Score: %.1f
                  Opportunity Level: %s
                  Reason: %s
                """.formatted(
                opportunity.repositoryName(),
                opportunity.opportunityScore(),
                opportunity.opportunityLevel(),
                opportunity.reason()
        )));

        return prompt.toString();
    }

    /**
     * AIが返却したMap形式のSaaSアイデアをDTOへ変換します。
     *
     * @param idea AIが返却したSaaSアイデア情報
     * @return SaaSアイデアDTO
     */
    private SaasIdeaDto toSaasIdea(Map<String, String> idea) {
        return new SaasIdeaDto(
                idea.getOrDefault("ideaName", ""),
                idea.getOrDefault("targetUser", ""),
                idea.getOrDefault("problem", ""),
                idea.getOrDefault("coreFeature", ""),
                idea.getOrDefault("monetization", "")
        );
    }
}
