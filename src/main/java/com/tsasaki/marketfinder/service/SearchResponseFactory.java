package com.tsasaki.marketfinder.service;

import com.tsasaki.marketfinder.dto.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 検索レスポンス生成を担当するファクトリです。
 *
 * <p>
 * 入力エラー、システムエラー、空の分析結果など、
 * SearchResponseDto の共通生成処理を集約します。
 * </p>
 */
@Component
public class SearchResponseFactory {

    /**
     * 入力チェックエラー用のレスポンスを生成します。
     *
     * @param validationMessage 入力チェックエラーメッセージ
     * @return 入力チェックエラー用レスポンス
     */
    public SearchResponseDto validationError(String validationMessage) {
        return new SearchResponseDto(
                List.of(),
                List.of(),
                null,
                validationMessage,
                emptySearchSummary(),
                emptyIssueSummary(),
                emptyTrendAnalysis(),
                List.of(),
                AiSummaryDto.unavailable(),
                List.of(),
                List.of()
        );
    }

    /**
     * システムエラー用のレスポンスを生成します。
     *
     * @param errorMessage システムエラーメッセージ
     * @return システムエラー用レスポンス
     */
    public SearchResponseDto systemError(String errorMessage) {
        return new SearchResponseDto(
                List.of(),
                List.of(),
                errorMessage,
                null,
                emptySearchSummary(),
                emptyIssueSummary(),
                emptyTrendAnalysis(),
                List.of(),
                AiSummaryDto.unavailable(),
                List.of(),
                List.of()
        );
    }

    private SearchSummaryDto emptySearchSummary() {
        return new SearchSummaryDto(0, 0.0, 0, 0);
    }

    private IssueSummaryDto emptyIssueSummary() {
        return new IssueSummaryDto(0, 0, 0, 0);
    }

    private TrendAnalysisDto emptyTrendAnalysis() {
        return new TrendAnalysisDto(0.0, 0, 0, 0, 0, 0, 0, 0, "N/A");
    }
}
