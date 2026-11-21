[English](README.md) | [簡體中文](README.zh-CN.md) | [繁體中文](README.zh-TW.md)

# Http 客戶端 Spring Boot Starter

讓 Spring Boot 應用快速整合 `HttpClient`。

## 快速開始

- 匯入相依性

```xml
    <dependency>
        <groupId>com.jiovan.spring.starter</groupId>
        <artifactId>http-client-spring-boot-starter</artifactId>
        <version>LATEST</version>
    </dependency>
```

> 預設情況下，此 starter 會自動生效，你可以透過 `jiovan.http-client.enabled = false` 關閉它。

- 使用字首 `jiovan.http-client` 設定 Spring Boot 的 `application.yml`

```yml
jiovan:
    http-client:
        sync-client:
            user-agent: 'Apache-HttpClient'
        async-client:
            user-agent: 'Apache-HttpClient'
```

- 此 starter 建立如下兩個 Bean，隨後你可以透過建構函式或 `@Autowired`/`@Resource` 註解設定自己的 Bean，然後即可訪問它。

| Bean 名稱 | Bean 類 |
|-----------------|--------------------------------------------------|
| syncHttpClient | org.apache.hc.client5.http.classic.HttpClient |
| asyncHttpClient | org.apache.hc.client5.http.async.HttpAsyncClient |

## 文件

- GitHub：https://github.com/jiovan-labs/http-client-spring-boot-starter
- HttpClient GitHub：https://github.com/apache/httpcomponents-client
- HttpClient 主頁：https://hc.apache.org

## 環境要求

- jdk 17+

## 許可證

本專案採用 [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0) 許可證。

請參閱 `NOTICE.txt` 檔案瞭解所需的宣告與歸屬資訊。

## 捐贈

喜歡這個工具包嗎？歡迎[向我們捐贈](https://jiovan.com/donation)以支援開發。

## 版權

北京玖漫科技有限公司

## 網站

- 玖漫：https://jiovan.com
