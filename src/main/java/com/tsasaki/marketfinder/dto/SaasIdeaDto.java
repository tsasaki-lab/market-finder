package com.tsasaki.marketfinder.dto;

/**
 * AIが生成したSaaSアイデアを保持するDTOです。
 *
 * @param ideaName     SaaSアイデア名
 * @param targetUser   想定ターゲットユーザー
 * @param problem      解決する課題
 * @param coreFeature  主要機能
 * @param monetization 収益化案
 */
public record SaasIdeaDto(
        String ideaName,
        String targetUser,
        String problem,
        String coreFeature,
        String monetization
) {
}
