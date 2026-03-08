# carlos-spring-boot-starter-docking

第三方系统对接组件，提供与钉钉、融政通等第三方平台的集成功能。

## 功能特性

- **钉钉集成**: 支持钉钉消息推送、组织架构同步、审批流程等
- **融政通集成**: 支持融政通消息推送、组织架构同步
- **事件通知**: 支持事件驱动的消息通知
- **统一接口**: 提供统一的对接接口，简化集成复杂度
- **自动配置**: Spring Boot自动配置，开箱即用

## 快速开始

### Maven依赖

```xml
<dependency>
    <groupId>com.carlos</groupId>
    <artifactId>carlos-spring-boot-starter-docking</artifactId>
    <version>${carlos.version}</version>
</dependency>
```

### 钉钉配置

```yaml
carlos:
  docking:
    dingtalk:
      enabled: true
      host: https://oapi.dingtalk.com
      appkey: ${DINGTALK_APPKEY}
      appsecret: ${DINGTALK_APPSECRET}
      agent-id: your-agent-id
      corp-id: your-corp-id
```

### 融政通配置

```yaml
carlos:
  docking:
    rzt:
      enabled: true
      debug: false
      api:
        host: https://rzt.example.com
        path: /ebus/api
        name: rzt
        context-id: rzt
      corpid: ${RZT_CORPID}
      secret: ${RZT_SECRET}
      agentid: ${RZT_AGENTID}
      paasid: ${RZT_PAASID}
      paastoken: ${RZT_PAASTOKEN}
```

## 使用示例

### 钉钉消息推送

```java
@Autowired
private DingtalkService dingtalkService;

public void sendMessage() {
    DingtalkMessageRequest request = new DingtalkMessageRequest();
    request.setUserIds(Arrays.asList("user1", "user2"));
    request.setMsgType("text");
    request.setContent("这是一条测试消息");

    dingtalkService.sendMessage(request);
}
```

### 组织架构同步

```java
@Autowired
private DingtalkOrgService dingtalkOrgService;

public void syncOrganization() {
    // 同步部门
    List<Department> departments = dingtalkOrgService.getDepartments();

    // 同步用户
    List<User> users = dingtalkOrgService.getUsers(departmentId);
}
```

### 融政通消息推送

```java
@Autowired
private RztService rztService;

public void sendRztMessage() {
    RztMessageRequest request = new RztMessageRequest();
    request.setToUser("user@example.com");
    request.setMsgType("text");
    request.setContent("融政通消息内容");

    rztService.sendMessage(request);
}
```

## 支持的第三方平台

| 平台  | 功能             | 状态    |
|-----|----------------|-------|
| 钉钉  | 消息推送、组织架构、审批流程 | ✅ 已支持 |
| 融政通 | 消息推送、组织架构      | ✅ 已支持 |
| 大联动 | 事件通知           | ✅ 已支持 |

## 依赖模块

- **carlos-spring-boot-core**: 核心基础功能
- **carlos-spring-boot-starter-json**: JSON序列化支持

## 注意事项

- 敏感信息（appkey、secret等）建议使用环境变量配置
- 注意API调用频率限制，避免触发限流
- 生产环境建议启用消息发送失败重试机制
- 定期检查access_token有效性，及时刷新

## 版本要求

- JDK 17+
- Spring Boot 3.x
