# Market Finder

Market Finder は、GitHub Repository と Issue を分析し、

* 開発者の課題
* 技術トレンド
* SaaS機会

を発見するための市場分析ツールです。

GitHub上のOSS活動やIssue情報をもとに、市場ニーズや事業機会を可視化します。

## Demo

https://market-finder-k8ag.onrender.com

---

## Features

### Repository Search

GitHub Repositoryを検索し、市場性を分析できます。

* GitHubリポジトリ検索
* 言語フィルタ
* 並び替え

    * Stars順
    * Market Score順
    * 更新日順

表示情報

* Repository Name
* Description
* Stars
* Language
* Updated At

---

### Market Score

Repositoryの人気度と更新頻度から市場性をスコア化します。

表示内容

* Market Score
* Stars Score
* Freshness Score

---

### Search Summary

検索結果全体を集計します。

表示内容

* 検索結果件数
* 平均Market Score
* 最高Market Score
* 合計Stars

---

### Issue Analysis

GitHub Issueを分析し、開発者が抱える課題を可視化します。

表示内容

* Issue検索
* Issue Summary
* Top Issue Keywords

---

### Trend Analysis

検索結果から市場トレンドを分析します。

表示内容

* Popularity Score
* Activity Score
* Demand Score
* Pain Level
* Trend Summary

---

### AI Summary

OpenAI APIを利用した市場分析機能です。

GitHub Repository、Issue、Market Finder独自スコアをもとに、

* 市場インサイト
* 開発者課題
* 技術トレンド
* SaaS機会

を自動生成します。

AI分析では以下の情報を活用しています。

* Market Score
* Stars Score
* Freshness Score
* Trend Analysis
* Issue Summary
* Issue Keywords

---

## Tech Stack

* Java 21
* Spring Boot 4.1
* Maven
* Thymeleaf
* Bootstrap
* GitHub REST API
* OpenAI Responses API

---

## Setup

### GitHub Token

環境変数を設定します。

```bash
export GITHUB_TOKEN="your_github_token"
```

### OpenAI API Key

AI Summary機能を利用する場合はOpenAI APIキーを設定します。

```bash
export OPENAI_API_KEY="your_openai_api_key"
```

### Run

```bash
./mvnw spring-boot:run
```

ブラウザでアクセスします。

```text
http://localhost:8080
```

---

## Releases

### v0.4.1

* AI分析品質向上
* Market ScoreをAI Promptへ追加
* Stars Score / Freshness ScoreをAI分析へ反映
* Trend AnalysisをAI分析へ反映
* Issue Summary / Issue KeywordsをAI分析へ反映
* AI Prompt最適化

### v0.4.0

* AI Summary追加
* OpenAI API連携
* 構造化AI分析
* 日本語品質改善

### v0.3.1

* Repository Search
* Issue Search
* Market Score
* Trend Analysis
* Issue Keyword Analysis
* Render Deployment

---

## Roadmap

### v0.5.0

* Opportunity Score
* SaaS機会ランキング
* 市場比較分析

### v0.6.0

* AI Opportunity Ranking
* 市場成長性分析
* GitHub競合分析
* SaaSアイデア自動生成
