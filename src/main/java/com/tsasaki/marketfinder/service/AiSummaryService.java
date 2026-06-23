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
 * OpenAI APIを利用して、検索結果から市場要約、
 * 開発者課題、技術トレンド、SaaS機会を生成します。
 * </p>
 */
@Service
public class AiSummaryService {

    private final OpenAiSummaryClient openAiSummaryClient;
    private final ObjectMapper objectMapper;
    private final AiSummaryPromptBuilder aiSummaryPromptBuilder;

    public AiSummaryService(
            OpenAiSummaryClient openAiSummaryClient,
            ObjectMapper objectMapper,
            AiSummaryPromptBuilder aiSummaryPromptBuilder
    ) {
        this.openAiSummaryClient = openAiSummaryClient;
        this.objectMapper = objectMapper;
        this.aiSummaryPromptBuilder = aiSummaryPromptBuilder;
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

        String content;

        try {
            String prompt = aiSummaryPromptBuilder.build(response);
            content = openAiSummaryClient.generate(prompt);
        } catch (Exception e) {
            return AiSummaryDto.unavailable();
        }

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
}
