package com.tsasaki.marketfinder.service;

import com.tsasaki.marketfinder.dto.GitHubRepositoryDto;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * リポジトリ検索結果の並び替えを担当するサービスです。
 *
 * <p>
 * stars、marketScore、updated などの条件に応じて、
 * GitHubリポジトリ検索結果を並び替えます。
 * </p>
 */
@Service
public class RepositorySortService {

    /**
     * 指定された条件でリポジトリ検索結果を並び替えます。
     *
     * @param repositories リポジトリ検索結果
     * @param sort         並び替え条件
     * @return 並び替え後のリポジトリ検索結果
     */
    public List<GitHubRepositoryDto> sort(
            List<GitHubRepositoryDto> repositories,
            String sort
    ) {
        if (repositories == null || repositories.isEmpty()) {
            return List.of();
        }

        if (sort == null || sort.isBlank() || sort.equals("stars")) {
            return repositories.stream()
                    .sorted((a, b) -> Integer.compare(b.stars(), a.stars()))
                    .toList();
        }

        if (sort.equals("marketScore")) {
            return repositories.stream()
                    .sorted((a, b) -> Integer.compare(b.marketScore(), a.marketScore()))
                    .toList();
        }

        if (sort.equals("updated")) {
            return repositories.stream()
                    .sorted((a, b) -> b.updatedAt().compareTo(a.updatedAt()))
                    .toList();
        }

        return repositories;
    }
}
