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

### GitHub Competition Analysis

GitHub Repositoryの競合状況を分析します。

検索結果をもとに、市場が成熟しているか、新規参入しやすいかを可視化します。

表示内容

* Competition Score
* Competition Level
* Market Maturity
* Entry Difficulty
* Major Competitors
* Differentiation Hints

Competition Analysisでは以下の情報を利用して分析します。

* Stars
* Repository Count
* Market Score

---

### Opportunity Score

Market Finder独自の指標をもとに、
SaaS化しやすい市場機会をスコア化します。

表示内容

* Opportunity Score
* Opportunity Level
* SaaS Opportunity Ranking

Opportunity Scoreは以下の情報を利用して算出します。

* Market Score
* Demand Score
* Pain Level Score
* Activity Score
* Open Issue Ratio

---

### AI SaaS Idea Generator

Opportunity Score上位の候補をもとに、
AIが具体的なSaaSアイデアを生成します。

表示内容

* SaaSアイデア名
* ターゲットユーザー
* 解決する課題
* 主要機能
* 収益化案

AI生成では以下の情報を活用しています。

* Opportunity Score
* Opportunity Level
* SaaS Opportunity Ranking
* Market Score
* Trend Analysis
* Issue Analysis

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

### v0.6.0

* GitHub Competition Analysis追加
* Competition Score追加
* Competition Level追加
* Market Maturity追加
* Entry Difficulty追加
* Major Competitors表示追加
* Differentiation Hints追加
* Competition Analysis画面追加

### v0.5.2

* SearchServiceの責務整理
* SearchResponseFactoryによるレスポンス生成処理の共通化
* SearchRequestValidatorによる入力チェック処理の分離
* RepositorySortServiceによる並び替え処理の分離
* AI Summary Prompt Builderの分離
* SaaS Idea Prompt Builderの分離
* search.htmlのThymeleaf Fragment化
* 保守性向上

### v0.5.1

* AI SaaS Idea Generator追加
* Opportunity Score上位候補からSaaSアイデアを生成
* ターゲットユーザー自動生成
* 解決する課題自動生成
* 主要機能自動生成
* 収益化案自動生成

### v0.5.0

* Opportunity Score追加
* SaaS Opportunity Ranking追加
* Market Scoreを利用した市場機会評価
* Trend Analysisを利用した機会分析
* Issue分析を利用した需要評価

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

### v0.7.0

* AI Opportunity Ranking
* 市場成長性分析
* 市場比較分析
* カテゴリ別ランキング
* トレンド履歴分析
