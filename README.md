# Market Finder

Market Finder は、GitHub上のリポジトリ情報をもとに、技術テーマや市場ニーズの調査を支援するWebアプリです。

## Features

- GitHubリポジトリ検索
- 言語フィルタ
- 並び替え
    - Stars順
    - Market Score順
    - 更新日順
- リポジトリメタ情報表示
    - Stars
    - Language
    - Updated At
- Market Score算出
- Market Score内訳表示
    - Stars Score
    - Freshness Score
- 検索サマリー表示
    - 検索結果件数
    - 平均Market Score
    - 最高Market Score
    - 合計Stars

## Tech Stack

- Java 21
- Spring Boot 4.1
- Maven
- Thymeleaf
- GitHub API

## Setup

環境変数に GitHub Personal Access Token を設定します。

```bash
export GITHUB_TOKEN="your_token"