package com.tsasaki.marketfinder.controller;

import com.tsasaki.marketfinder.dto.SearchResponseDto;
import com.tsasaki.marketfinder.service.SearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 検索画面のリクエストを処理するControllerです。
 */
@Controller
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    /**
     * 入力された検索条件をもとに、GitHubリポジトリ・Issue・市場分析結果を表示します。
     *
     * @param keyword  検索キーワード
     * @param language 絞り込み対象のプログラミング言語
     * @param sort     並び替え条件
     * @param model    Viewへ渡すModel
     * @return 検索結果ページのテンプレート名
     */
    @GetMapping("/search")
    public String search(String keyword, String language, String sort, Model model) {

        SearchResponseDto response = searchService.search(keyword, language, sort);

        model.addAttribute("keyword", keyword);
        model.addAttribute("language", language);
        model.addAttribute("sort", sort);
        model.addAttribute("results", response.results());
        model.addAttribute("issues", response.issues());
        model.addAttribute("errorMessage", response.errorMessage());
        model.addAttribute("validationMessage", response.validationMessage());
        model.addAttribute("summary", response.summary());
        model.addAttribute("issueSummary", response.issueSummary());
        model.addAttribute("trendAnalysis", response.trendAnalysis());
        model.addAttribute("issueKeywords", response.issueKeywords());
        model.addAttribute("aiSummary", response.aiSummary());
        model.addAttribute("opportunities", response.opportunities());

        return "search";
    }
}
