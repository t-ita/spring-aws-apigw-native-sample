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

* `spring-cloud-function-context` を `spring-cloud-function-adapter-aws` に変更
* Native Image 化に Build Pack を使わないので、`spring-boot-maven-plugin` から build　pack の記述をはずす
* `<classifier>exec</classifier>` を追加
* `native-iamge` 用のプラグインを追加
* `native-image-maven-plugin` > `configuration` の、以下の項目を指定
    * `mainClass` … main メソッドを持つクラスを指定
    * `imageName` … わかりやすい名前を指定
    * `buildArgs` … AWS Lambda で動かす場合、`--enable-http` `--enable-https` が必要（詳細は未確認）





