package com.tsasaki.marketfinder.service;

import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 検索リクエストの入力チェックを担当するバリデータです。
 *
 * <p>
 * 検索キーワードの未入力や文字数上限など、
 * 検索実行前に確認すべき条件を検証します。
 * </p>
 */
@Component
public class SearchRequestValidator {

    private static final int MAX_KEYWORD_LENGTH = 50;

    /**
     * 検索キーワードを検証します。
     *
     * @param keyword 検索キーワード
     * @return 入力エラーがある場合はエラーメッセージ、問題ない場合は空
     */
    public Optional<String> validateKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return Optional.of("検索キーワードを入力してください。");
        }

        if (keyword.length() > MAX_KEYWORD_LENGTH) {
            return Optional.of("検索キーワードは50文字以内で入力してください。");
        }

        return Optional.empty();
    }
}
