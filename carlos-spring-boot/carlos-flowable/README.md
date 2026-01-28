# carlos-flowable

Flowable工作流引擎集成组件，提供BPMN 2.0工作流引擎的Spring Boot集成支持。

## 功能特性

- **BPMN 2.0支持**: 完整的BPMN 2.0流程定义和执行
- **流程管理**: 流程部署、启动、挂起、恢复等操作
- **任务管理**: 用户任务、服务任务的创建和处理
- **历史记录**: 完整的流程执行历史追踪
- **Spring Boot集成**: 开箱即用的自动配置

## 快速开始

### Maven依赖

```xml
<dependency>
    <groupId>com.carlos</groupId>
    <artifactId>carlos-flowable</artifactId>
    <version>${carlos.version}</version>
</dependency>
```

### 配置示例

```yaml
carlos:
  flowable:
    enabled: true
    # 是否自动部署流程定义
    auto-deploy: true
    # 流程定义文件位置
    process-definition-location: classpath*:/processes/**/*.bpmn20.xml

spring:
  flowable:
    # 数据库配置
    database-schema-update: true
    # 异步执行器
    async-executor-activate: true
```

## 使用示例

### 启动流程实例

```java
@Autowired
private RuntimeService runtimeService;

public void startProcess() {
    Map<String, Object> variables = new HashMap<>();
    variables.put("applicant", "张三");
    variables.put("amount", 1000);

    ProcessInstance processInstance = runtimeService
        .startProcessInstanceByKey("leaveProcess", variables);
}
```

### 完成任务

```java
@Autowired
private TaskService taskService;

public void completeTask(String taskId) {
    Map<String, Object> variables = new HashMap<>();
    variables.put("approved", true);

    taskService.complete(taskId, variables);
}
```

## 依赖模块

- **carlos-core**: 核心基础功能
- **Flowable**: BPMN工作流引擎

## 注意事项

- 需要配置数据库连接，Flowable会自动创建所需表结构
- 建议在生产环境使用独立的数据库schema
- 流程定义文件需要符合BPMN 2.0规范

## 版本要求

- JDK 17+
- Spring Boot 3.x
- Flowable 7.x
