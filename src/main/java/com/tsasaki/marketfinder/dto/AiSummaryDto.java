package com.tsasaki.marketfinder.dto;

import java.util.List;

/**
 * AIによって生成された市場分析結果を表すDTOです。
 *
 * <p>
 * GitHubのリポジトリ情報やIssue分析結果をもとに生成された、
 * 市場概要、主要トレンド、事業機会を保持します。
 * </p>
 */
public class AiSummaryDto {

    private boolean available;
    private String marketSummary;
    private List<String> keyTrends;
    private List<String> opportunities;

    /**
     * デフォルトコンストラクタです。
     */
    public AiSummaryDto() {
    }

    /**
     * AI分析が利用できない場合のDTOを生成します。
     *
     * @return AI分析が利用できない状態を表すDTO
     */
    public static AiSummaryDto unavailable() {
        AiSummaryDto dto = new AiSummaryDto();
        dto.setAvailable(false);
        dto.setMarketSummary("AI Summary is not available.");
        dto.setKeyTrends(List.of());
        dto.setOpportunities(List.of());
        return dto;
    }

    /**
     * AI分析結果をもとにDTOを生成します。
     *
     * @param marketSummary 市場概要
     * @param keyTrends 主要トレンド
     * @param opportunities 事業機会
     * @return AI分析結果を保持するDTO
     */
    public static AiSummaryDto of(String marketSummary, List<String> keyTrends, List<String> opportunities) {
        AiSummaryDto dto = new AiSummaryDto();
        dto.setAvailable(true);
        dto.setMarketSummary(marketSummary);
        dto.setKeyTrends(keyTrends);
        dto.setOpportunities(opportunities);
        return dto;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getMarketSummary() {
        return marketSummary;
    }

    public void setMarketSummary(String marketSummary) {
        this.marketSummary = marketSummary;
    }

    public List<String> getKeyTrends() {
        return keyTrends;
    }

    public void setKeyTrends(List<String> keyTrends) {
        this.keyTrends = keyTrends;
    }

    public List<String> getOpportunities() {
        return opportunities;
    }

    public void setOpportunities(List<String> opportunities) {
        this.opportunities = opportunities;
    }
}
