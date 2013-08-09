wicket-jpaプロジェクトの動作のさせ方

# 事前準備

JDK6以上を予めインストールしておいて下さい.  

このファイルが見られているということは、既にMavenがインストールされていることでしょう！

6以上が必須です.  

# eclipse対応にするには
次のコマンドで、eclipseプロジェクトが作成されます.  

```
mvn eclipse:eclipse
```

後は、eclipseにインポートして下さい.  

# Webアプリ起動方法

次のコマンドで、Webアプリが起動します.  

```
mvn compile exec:java
```

テストをスキップしたい場合は次のコマンド.  

```
mvn -DskipTests=true compile exec:java
```

```jp.co.city.tear.WebStarter```クラスを実行して下さい.  

コンソールに次の表示が出たら起動しています.  

```
********************************************************************
*** WARNING: Wicket is running in DEVELOPMENT mode.              ***
***                               ^^^^^^^^^^^                    ***
*** Do NOT deploy to your live server(s) without changing this.  ***
*** See Application#getConfigurationType() for more information. ***
********************************************************************
```


次のURLにアクセスすると、Webアプリが使えます.  

<http://localhost:8081>
