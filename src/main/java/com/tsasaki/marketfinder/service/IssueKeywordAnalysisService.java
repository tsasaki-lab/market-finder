package com.tsasaki.marketfinder.service;

import com.tsasaki.marketfinder.dto.GitHubIssueDto;
import com.tsasaki.marketfinder.dto.IssueKeywordDto;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * GitHub Issueのタイトルから頻出キーワードを抽出するサービスです。
 *
 * <p>
 * Issueタイトルに含まれる単語を集計することで、
 * 開発者がどのような課題に直面しているかを把握しやすくします。
 * </p>
 */
@Service
public class IssueKeywordAnalysisService {

    private static final Set<String> STOP_WORDS = Set.of(
            "the", "a", "an", "is", "are", "was", "were",
            "and", "or", "for", "to", "of", "in", "on",
            "with", "by", "from", "as", "at", "be",
            "this", "that", "it", "not", "can", "cannot",
            "error", "issue", "problem", "bug", "fix",
            "add", "update", "support", "use", "using"
    );

    /**
     * Issueタイトルから頻出キーワードを抽出します。
     *
     * @param issues GitHub Issue検索結果
     * @return 頻出キーワード一覧
     */
    public List<IssueKeywordDto> analyze(List<GitHubIssueDto> issues) {
        if (issues == null || issues.isEmpty()) {
            return List.of();
        }

        Map<String, Long> keywordCounts = issues.stream()
                .map(GitHubIssueDto::title)
                .filter(title -> title != null && !title.isBlank())
                .flatMap(title -> Arrays.stream(normalize(title).split("\\s+")))
                .filter(word -> !word.isBlank())
                .filter(word -> word.length() >= 3)
                .filter(word -> !STOP_WORDS.contains(word))
                .collect(Collectors.groupingBy(
                        word -> word,
                        Collectors.counting()
                ));

        return keywordCounts.entrySet().stream()
                .sorted(
                        Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                                .thenComparing(Map.Entry.comparingByKey())
                )
                .limit(10)
                .map(entry -> new IssueKeywordDto(
                        entry.getKey(),
                        entry.getValue().intValue()
                ))
                .toList();
    }

    private String normalize(String text) {
        return text.toLowerCase()
                .replaceAll("[^a-z0-9#+.\\- ]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }
}
