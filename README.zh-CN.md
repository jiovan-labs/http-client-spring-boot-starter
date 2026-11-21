[English](README.md) | [简体中文](README.zh-CN.md) | [繁體中文](README.zh-TW.md)

# Http 客户端 Spring Boot Starter

让 Spring Boot 应用快速集成 `HttpClient`。

## 快速开始

- 导入依赖

```xml
    <dependency>
        <groupId>com.jiovan.spring.starter</groupId>
        <artifactId>http-client-spring-boot-starter</artifactId>
        <version>LATEST</version>
    </dependency>
```

> 默认情况下，此 starter 会自动生效，你可以通过 `jiovan.http-client.enabled = false` 关闭它。

- 使用前缀 `jiovan.http-client` 配置 Spring Boot 的 `application.yml`

```yml
jiovan:
    http-client:
        sync-client:
            user-agent: 'Apache-HttpClient'
        async-client:
            user-agent: 'Apache-HttpClient'
```

- 此 starter 创建如下两个 Bean，随后你可以通过构造函数或 `@Autowired`/`@Resource` 注解配置自己的 Bean，然后即可访问它。

| Bean 名称 | Bean 类 |
|-----------------|--------------------------------------------------|
| syncHttpClient | org.apache.hc.client5.http.classic.HttpClient |
| asyncHttpClient | org.apache.hc.client5.http.async.HttpAsyncClient |

## 文档

- GitHub：https://github.com/jiovan-labs/http-client-spring-boot-starter
- HttpClient GitHub：https://github.com/apache/httpcomponents-client
- HttpClient 主页：https://hc.apache.org

## 环境要求

- jdk 17+

## 许可证

本项目采用 [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0) 许可证。

请参阅 `NOTICE.txt` 文件了解所需的声明与归属信息。

## 捐赠

喜欢这个工具包吗？欢迎[向我们捐赠](https://jiovan.com/donation)以支持开发。

## 版权

北京玖漫科技有限公司

## 网站

- 玖漫：https://jiovan.com
