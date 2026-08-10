package com.tsasaki.marketfinder.service;

import com.tsasaki.marketfinder.dto.AiSummaryDto;
import com.tsasaki.marketfinder.dto.CompetitionAnalysisDto;
import com.tsasaki.marketfinder.dto.IssueSummaryDto;
import com.tsasaki.marketfinder.dto.SearchResponseDto;
import com.tsasaki.marketfinder.dto.SearchSummaryDto;
import com.tsasaki.marketfinder.dto.TrendAnalysisDto;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 検索レスポンスDTOを生成するFactoryです。
 */
@Component
public class SearchResponseFactory {

    /**
     * 入力チェックエラー時の検索レスポンスを生成します。
     *
     * @param validationMessage 入力チェックエラーメッセージ
     * @return 検索レスポンスDTO
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
                CompetitionAnalysisDto.empty(),
                List.of(),
                List.of(),
                List.of()
        );
    }

    /**
     * システムエラー時の検索レスポンスを生成します。
     *
     * @param errorMessage システムエラーメッセージ
     * @return 検索レスポンスDTO
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
                CompetitionAnalysisDto.empty(),
                List.of(),
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
