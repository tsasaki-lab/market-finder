package com.tsasaki.marketfinder.controller;

import com.tsasaki.marketfinder.dto.SearchResponseDto;
import com.tsasaki.marketfinder.service.SearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/search")
    public String search(String keyword, String language, String sort, Model model) {

        SearchResponseDto response = searchService.search(keyword, language, sort);

        model.addAttribute("keyword", keyword);
        model.addAttribute("language", language);
        model.addAttribute("sort", sort);
        model.addAttribute("results", response.results());
        model.addAttribute("errorMessage", response.errorMessage());
        model.addAttribute("validationMessage", response.validationMessage());
        model.addAttribute("summary", response.summary());

        return "search";
    }
}