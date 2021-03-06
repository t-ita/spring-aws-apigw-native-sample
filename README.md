# Spring Cloud Function を使って、AWS Lambda 関数を Native Image で作り、Amazon API Gateway で API 公開する

* コードは前回と同じ
* 今回は、Spring Initilizr を使って Spring Native を導入することも試す

## Spring Initializr でプロジェクトを作成

* Spring Boot のバージョンを `2.4.4` とする
* 以下の Dependencies を選択
    * Developer Tools > Spring Native
    * Developer Tools > Lombok
    * Spring Cloud > Function
    
## pom.xml の編集

* `spring-boot-starter` を `spring-boot-starter-webflux` に変更
* `spring-cloud-function-context` を `spring-cloud-function-web` に変更
* `spring-cloud-function-adapter-aws` を dependencies に追加
* Native Image 化に Build Pack を使わないので、`spring-boot-maven-plugin` から build pack の記述をはずす
* `<classifier>exec</classifier>` を追加
* `native-iamge` 用のプラグインを追加
* `native-image-maven-plugin` > `configuration` の、以下の項目を指定
  * `mainClass` … main メソッドを持つクラスを指定
  * `imageName` … わかりやすい名前を指定
  * `buildArgs` … AWS Lambda で動かす場合、`--enable-http` , `--enable-https` が必要（詳細は未確認）

## application.properties の設定

* 下記設定を追加

```
spring.cloud.function.web.export.enabled=true
spring.cloud.function.web.export.debug=false
spring.main.web-application-type=none
```

## 関数コードの作成

* API Gateway とメッセージをやりとりするので、関数は以下を Implement するように実装
  * `Function<Message<xxx>, Message<xxx>>`
* 関数クラスには `@Component` を付ける
* apply メソッドを実装

## ビルド環境を構築

* Amazon Linux2 上でコンパイルする Docker image をビルドする
  * Dockerfile を追加（わかりやすいように名称を `Dockerfile.builder` とする）
  * Docker build
  ```
  docker build -f Dockerfile.builder -t awslambda-native-builder:latest .
  ```
* bootstrap を配置
* Native Image ビルドシェルを作成

## ビルド

build-native.sh を実行してビルド

## 動作確認

Docker コンテナ起動・ログイン

```
docker run -v "$(pwd)":/work/build -p 8080:8080 --rm -it awslambda-native-builder:latest /bin/bash
```

アプリ起動

```
./target/SpringAwsApigwNativeSampleApplication
```

別ターミナルから下記コマンドを投げる

```
curl -H "Content-Type: application/json" localhost:8080/greeting -d '{"body": "{\"name\": \"t-ita\",\"message\": \"Hello\"}"}'
```

結果。Messaege.getPayload() がうまく処理出来ておらず、name / message に null が入ってくる

```
{"message":"null,null","name":"Spring Cloud Function Native"}%
```

Spring Cloud のバージョン 2020.0.2 だと上手くいかない？

GitHub でコードを確認したところ、2020.0.1 以降で、Message を変換する部分に修正がはいっており、その影響で AWS Adapter が上手く動作していないように見える。

苦肉の策として、APIGatewayProxyRequestEvent から直接 body を取得して、ObjectMapper で変換する方法をとる。

Native Image 化して実行するも、APIGatewyProxyRequestEvent に値が入ってこない
