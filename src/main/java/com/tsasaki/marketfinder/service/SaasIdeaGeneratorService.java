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
    private final SaasIdeaPromptBuilder saasIdeaPromptBuilder;

    public SaasIdeaGeneratorService(
            OpenAiSummaryClient openAiSummaryClient,
            ObjectMapper objectMapper,
            SaasIdeaPromptBuilder saasIdeaPromptBuilder
    ) {
        this.openAiSummaryClient = openAiSummaryClient;
        this.objectMapper = objectMapper;
        this.saasIdeaPromptBuilder = saasIdeaPromptBuilder;
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

        String content;

        try {
            String prompt = saasIdeaPromptBuilder.build(topOpportunities);
            content = openAiSummaryClient.generate(prompt);
        } catch (Exception e) {
            return List.of();
        }

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
