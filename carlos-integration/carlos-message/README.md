<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">Yunjin-message v2.0.0</h1>
<h4 align="center">Spring Boot/Spring Cloud & Alibaba 统一消息中心</h4>

## 服务简介

消息中心是一套 。

* 采用客户端和服务端分离模式

## 名词解释

1. 消息类型：用于将消息分组，每一个模板需要绑定一个类型，类型可以是一个业务大类、也可以是按发送渠道分类。
2. 消息模板：消息模板，用于定义消息内容，支持动态参数，统一模板的消息可能发送至多个渠道，如：邮件、短信、微信、钉钉、公众号、小程序、APP等，如果针对特定渠道需要配置，使用channel_config。
3. 消息渠道：消息渠道，如：邮件、短信、微信、钉钉、公众号、小程序、APP等，渠道的配置包含服务参数等，应用启动时，应根据配置信息，动态加载渠道服务，渠道配置也包含和模板对应的配置信息，模板与渠道绑定时，需要完善此部分信息。
4. 消息记录：：消息记录，保存原始消息信息，。

## 模块结构

~~~
carlos-message     
├── yunjin-message-boot              // springboot 启动
├── yunjin-message-cloud             // springcloud 启动
├── yunjin-message-core              // 核心模块
├── yunjin-message-client            // 客户端模块
├── yunjin-message-server            // 消息服务端
│       └── com.msg.controller       // 消息服务端


├──pom.xml                // 公共依赖
~~~

## 架构图

msg_message_template
[//]: # (<img src="https://oscimg.oschina.net/oscnet/up-82e9722ecb846786405a904bafcf19f73f3.png"/>)

## 内置功能

1. 模板管理：用户是系统操作者，该功能主要完成系统用户配置。
2. 类型管理：配置系统组织机构（公司、部门、小组），树结构展现支持数据权限。
3. 渠道管理：配置系统用户所属担任职务。
4. 失败重试：配置系统菜单，操作权限，按钮权限标识等。
5. 发送记录：角色菜单权限分配、设置角色按机构进行数据范围权限划分。

## 演示图

<table>

</table>
