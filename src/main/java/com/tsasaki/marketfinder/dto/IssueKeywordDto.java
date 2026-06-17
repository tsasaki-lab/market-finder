package com.tsasaki.marketfinder.dto;

/**
 * Issueタイトルから抽出した頻出キーワードを保持するDTOです。
 *
 * @param keyword キーワード
 * @param count   出現回数
 */
public record IssueKeywordDto(
        String keyword,
        int count
) {
}
