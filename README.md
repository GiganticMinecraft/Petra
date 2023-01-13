# Petra

Petra Plugin（以下、当プラグイン）は、[ギガンティック☆整地鯖](https://seichi.click)（以下、当サーバー）のSpigotプラグインです。

## Description

- プレイヤーたちにはいくつもの課題が次々と与えられる
- その課題を協働してクリアし、生き残ることが目標

## Auto Release

- mainブランチが更新されると、そのコードを基に実行用jarがビルドされ、テスト環境に配布されます。テスト環境は毎ゲーム毎または日次１回の自動再起動が行われており、次回再起動時に最新のjarを使用して稼働します。
  - 本番環境へは、Minecraft Java Editionで`play.seichi.click`に接続し、`T`キーでチャットを開き、`/server petra`と入力して`Enter`を押すとアクセスできます。
- 今のところ、デバッグ環境はありません。

## Development

### Dependencies

- Java 1.8
- Spigot 1.15.2

### Database

- 当プラグインはMySQLを用いて全データベースを管理しています。
- デバッグサーバー起動時には`gigantic`データベースを作成済であることを確認してください。

### Kotlin Style Guide

基本的には[スマートテック・ベンチャーズ Kotlinコーディング規約](https://github.com/SmartTechVentures/kotlin-style-guide)に準拠します。

### Nullable

`!!`演算子は原則使用禁止としますが、`null`ではないことが明確な場合は使用可能とします。

### Documents

- `public`なメソッドについては、[KDoc](https://kotlinlang.org/docs/kotlin-doc.html)を記載するよう心がけてください。
- その他は各自が必要だと判断した場合のみ記載してください。

### Commit Style

- 1コミットあたりの情報は最小限としてください。
- コミットメッセージは英語の動詞から始めることを推奨しています。

### Branch Model

- [Git-flow](https://qiita.com/KosukeSone/items/514dd24828b485c69a05)を簡略化したものを使用します。
- 新規に機能を開発する際はdevelopブランチからfeatureブランチを作り、そこで作業してください。
- 開発が終了したらdevelopブランチにマージします。
- mainブランチは本番環境に反映されます。
- 本番環境を更新するタイミングでdevelopブランチをmainブランチにマージします。
