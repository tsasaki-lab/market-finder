package com.tsasaki.marketfinder.client;

import com.tsasaki.marketfinder.config.OpenAiProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

/**
 * OpenAI APIを呼び出してAI市場分析テキストを生成するClientです。
 */
@Component
public class OpenAiSummaryClient {

    private final OpenAiProperties openAiProperties;
    private final RestClient restClient;

    public OpenAiSummaryClient(OpenAiProperties openAiProperties) {
        this.openAiProperties = openAiProperties;
        this.restClient = RestClient.create();
    }

    /**
     * 指定されたプロンプトをOpenAI APIへ送信し、生成テキストを取得します。
     *
     * @param prompt AIへ渡す市場分析用プロンプト
     * @return OpenAI APIが生成したテキスト
     */
    public String generate(String prompt) {
        Map<String, Object> requestBody = Map.of(
                "model", openAiProperties.getModel(),
                "input", prompt
        );

        Map response = restClient.post()
                .uri(openAiProperties.getBaseUrl())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + openAiProperties.getApiKey())
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(Map.class);

        return extractOutputText(response);
    }

    /**
     * OpenAI APIレスポンスから出力テキストを取り出します。
     *
     * @param response OpenAI APIレスポンス
     * @return 出力テキスト
     */
    private String extractOutputText(Map response) {
        if (response == null || response.get("output") == null) {
            return "";
        }

        List output = (List) response.get("output");
        if (output.isEmpty()) {
            return "";
        }

        Map firstOutput = (Map) output.get(0);
        List content = (List) firstOutput.get("content");
        if (content == null || content.isEmpty()) {
            return "";
        }

        Map firstContent = (Map) content.get(0);
        Object text = firstContent.get("text");

        return text == null ? "" : text.toString();
    }
}
