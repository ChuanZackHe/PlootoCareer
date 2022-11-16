# XIU服务平台
云效部署步骤
1.本地编译
2.运行build-and-push-docker.sh脚本
3.去云效点击部署

基于Java8,Spring Boot 2.x,WebFlux,Netty,Vert.x,Reactor等开发, 
是一个开箱即用,可二次开发的企业级物联网基础平台。平台实现了物联网相关的众多基础功能,
能帮助你快速建立物联网相关业务系统。
 

## 核心特性

支持统一物模型管理,多种设备,多种厂家,统一管理。
统一设备连接管理,多协议适配(MQTT,TCP),屏蔽网络编程复杂性,灵活接入不同厂家不同协议的设备。
灵活的规则引擎,设备告警,消息通知,数据转发.
强大的ReactorQL引擎,使用SQL来处理实时数据.


## 技术栈

1. [Spring Boot 2.3.x](https://spring.io/projects/spring-boot)
2. [Spring WebFlux](https://spring.io/) 响应式Web支持
3. [R2DBC](https://r2dbc.io/) 响应式关系型数据库驱动
4. [Project Reactor](https://projectreactor.io/) 响应式编程框架
4. [Netty](https://netty.io/) ,[Vert.x](https://vertx.io/) 高性能网络编程框架
5. [ElasticSearch](https://www.elastic.co/cn/products/enterprise-search) 全文检索，日志，时序数据存储
6. [PostgreSQL](https://www.postgresql.org) 业务功能数据管理

## 架构

![platform](./platform.png)

## 设备接入流程

![flow](./flow.svg)

## 模块

```bash
--xiu-service-platform
------|----docker
------|------|----dev-env       # 启动开发环境
------|------|----run-all       # 启动全部,通过http://localhost:9000 访问系统.
------|----service-platform-app  # 启动模块
------|----simulator            # 设备模拟器
```
