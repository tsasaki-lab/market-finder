package com.tsasaki.marketfinder.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    public List<String> search(String keyword) {

        if (keyword == null || keyword.isBlank()) {
            return List.of();
        }

        return List.of(
                keyword + " tutorial",
                keyword + " github",
                keyword + " reddit",
                keyword + " trends",
                keyword + " tools"
        );
    }
}