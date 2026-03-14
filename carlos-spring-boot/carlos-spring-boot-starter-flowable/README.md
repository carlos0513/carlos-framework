# carlos-spring-boot-starter-flowable

Flowable工作流引擎集成组件，提供BPMN 2.0工作流引擎的Spring Boot集成支持。

## 功能特性

- **BPMN 2.0支持**: 完整的BPMN 2.0流程定义和执行
- **流程部署管理**: 支持文件、ZIP包、Classpath方式部署流程定义
- **流程实例管理**: 流程启动、挂起、恢复、终止等操作
- **任务管理**: 用户任务的签收、完成、委托、转办、驳回等操作
- **历史记录**: 完整的流程执行历史追踪
- **流程跳转**: 支持任意节点跳转（自由流）
- **Spring Boot集成**: 开箱即用的自动配置

## 版本信息

- **JDK**: 17+
- **Spring Boot**: 3.5.9
- **Flowable**: 7.0.1

## 快速开始

### Maven依赖

```xml
<dependency>
    <groupId>com.carlos</groupId>
    <artifactId>carlos-spring-boot-starter-flowable</artifactId>
    <version>${carlos.version}</version>
</dependency>
```

### 配置示例

```yaml
carlos:
  flowable:
    # 是否启用Flowable
    enabled: true
    # 是否自动部署流程定义
    auto-deploy: true
    # 流程定义文件位置
    process-definition-location: classpath*:/processes/**/*.bpmn20.xml
    # 是否启用历史记录
    history-enabled: true
    # 历史记录级别: none, activity, audit, full
    history-level: audit
    # 是否启用异步执行器
    async-executor-enabled: true
    # 异步执行器配置
    async-executor:
      core-pool-size: 2
      max-pool-size: 10
      queue-size: 100
      keep-alive-time: 60

spring:
  # 数据库配置（Flowable会自动创建所需表）
  datasource:
    url: jdbc:mysql://localhost:3306/flowable?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: root
    password: password
    driver-class-name: com.mysql.cj.jdbc.Driver
```

## 核心服务

### FlowableService - 综合服务入口

```java
@Autowired
private FlowableService flowableService;

// 使用流程定义服务
flowableService.process().deploy(file, name, category, description);

// 使用流程实例服务
flowableService.runtime().startProcessInstance(param);

// 使用任务服务
flowableService.task().completeTask(param);

// 使用历史记录服务
flowableService.history().getDoneList(assignee, pageNum, pageSize);
```

### 流程定义服务 (FlowableProcessService)

```java
@Autowired
private FlowableProcessService processService;

// 部署流程（通过文件）
String deploymentId = processService.deploy(file, "请假流程", "oa", "员工请假审批流程");

// 部署流程（通过ZIP）
String deploymentId = processService.deployByZip(zipFile, "请假流程", "oa");

// 部署流程（通过Classpath）
String deploymentId = processService.deployByClasspath("processes/leave.bpmn20.xml", "请假流程", "oa");

// 查询流程定义列表
List<ProcessDefinitionDTO> list = processService.listProcessDefinitions(null, null, null);

// 获取流程定义详情
ProcessDefinitionDTO definition = processService.getProcessDefinitionById(processDefinitionId);

// 挂起/激活流程定义
processService.suspendProcessDefinition(processDefinitionId);
processService.activateProcessDefinition(processDefinitionId);

// 删除部署
processService.deleteDeployment(deploymentId, true);

// 获取流程定义XML
String xml = processService.getProcessDefinitionXml(processDefinitionId);

// 获取流程图
InputStream diagram = processService.getProcessDiagram(processDefinitionId);
```

### 流程实例服务 (FlowableRuntimeService)

```java
@Autowired
private FlowableRuntimeService runtimeService;

// 启动流程实例
StartProcessParam param = new StartProcessParam();
param.setProcessDefinitionKey("leaveProcess");
param.setBusinessKey("LEAVE-2024-001");
param.setProcessInstanceName("张三的请假申请");
param.setStartUserId("user001");
param.setVariables(Map.of("applicant", "张三", "days", 3));

ProcessInstanceDTO instance = runtimeService.startProcessInstance(param);

// 根据Key启动流程（简化版）
ProcessInstanceDTO instance = runtimeService.startProcessInstanceByKey(
    "leaveProcess", "LEAVE-2024-001", variables);

// 查询流程实例列表
List<ProcessInstanceDTO> list = runtimeService.listProcessInstances(
    "leaveProcess", null, null, null, null);

// 获取流程实例详情
ProcessInstanceDTO instance = runtimeService.getProcessInstanceById(instanceId);

// 根据业务Key查询
List<ProcessInstanceDTO> list = runtimeService.getProcessInstanceByBusinessKey(businessKey);

// 挂起/激活流程实例
runtimeService.suspendProcessInstance(instanceId);
runtimeService.activateProcessInstance(instanceId);

// 终止流程实例
runtimeService.terminateProcessInstance(instanceId, "用户取消申请");

// 删除流程实例
runtimeService.deleteProcessInstance(instanceId, "删除原因", true);

// 操作流程变量
runtimeService.setVariable(instanceId, "approved", true);
Object value = runtimeService.getVariable(instanceId, "approved");
Map<String, Object> variables = runtimeService.getVariables(instanceId);

// 判断流程是否结束
boolean ended = runtimeService.isProcessInstanceEnded(instanceId);
```

### 任务服务 (FlowableTaskService)

```java
@Autowired
private FlowableTaskService taskService;

// 获取待办任务列表
List<TaskDTO> todoList = taskService.getTodoList("user001", new TaskQueryParam());

// 获取待办任务数量
long count = taskService.getTodoCount("user001", new TaskQueryParam());

// 获取候选任务列表
List<String> groups = Arrays.asList("manager", "hr");
List<TaskDTO> candidateList = taskService.getCandidateList("user001", groups, new TaskQueryParam());

// 获取任务详情
TaskDTO task = taskService.getTaskById(taskId);

// 签收任务
taskService.claimTask(taskId, "user001");

// 取消签收
taskService.unclaimTask(taskId);

// 完成任务
CompleteTaskParam param = new CompleteTaskParam();
param.setTaskId(taskId);
param.setAssignee("user001");
param.setComment("同意请假申请");
param.setVariables(Map.of("approved", true, "remark", "情况属实"));
taskService.completeTask(param);

// 委托任务
DelegateTaskParam delegateParam = new DelegateTaskParam();
delegateParam.setTaskId(taskId);
delegateParam.setAssignee("user001");
delegateParam.setDelegateToUserId("user002");
delegateParam.setReason("临时外出，委托处理");
taskService.delegateTask(delegateParam);

// 转办任务
TransferTaskParam transferParam = new TransferTaskParam();
transferParam.setTaskId(taskId);
transferParam.setAssignee("user001");
transferParam.setTransferToUserId("user003");
transferParam.setReason("转交给更合适的人处理");
taskService.transferTask(transferParam);

// 驳回任务（返回上一节点）
taskService.rejectTask(taskId, "user001", "信息不完整，请补充");

// 设置任务处理人
taskService.setAssignee(taskId, "user004");

// 添加/删除候选人
taskService.addCandidateUser(taskId, "user005");
taskService.deleteCandidateUser(taskId, "user005");

// 操作任务变量
taskService.setVariable(taskId, "remark", "补充说明");
Object value = taskService.getVariable(taskId, "remark");

// 获取任务审批意见
List<String> comments = taskService.getTaskComments(taskId);
```

### 历史记录服务 (FlowableHistoryService)

```java
@Autowired
private FlowableHistoryService historyService;

// 获取已办任务列表
List<HistoricTaskDTO> doneList = historyService.getDoneList("user001", 1, 10);

// 获取已办任务数量
long count = historyService.getDoneCount("user001");

// 获取历史任务详情
HistoricTaskDTO task = historyService.getHistoricTaskById(taskId);

// 获取流程实例的历史任务
List<HistoricTaskDTO> tasks = historyService.getHistoricTasksByProcessInstanceId(instanceId);

// 查询历史流程实例
List<ProcessInstanceDTO> list = historyService.getHistoricProcessInstances(
    "leaveProcess", null, "user001", true, 1, 10);

// 获取历史流程实例详情
ProcessInstanceDTO instance = historyService.getHistoricProcessInstanceById(instanceId);

// 获取流程活动历史（审批记录）
List<HistoricActivityVO> activities = historyService.getHistoricActivities(instanceId);

// 获取历史变量
Map<String, Object> variables = historyService.getHistoricVariables(instanceId);

// 删除历史流程实例
historyService.deleteHistoricProcessInstance(instanceId);
```

## 数据对象说明

### DTO（服务层传输对象）

| 类名                   | 说明     |
|----------------------|--------|
| ProcessDefinitionDTO | 流程定义信息 |
| ProcessInstanceDTO   | 流程实例信息 |
| TaskDTO              | 任务信息   |
| HistoricTaskDTO      | 历史任务信息 |

### VO（视图对象）

| 类名                  | 说明     |
|---------------------|--------|
| ProcessDefinitionVO | 流程定义视图 |
| ProcessInstanceVO   | 流程实例视图 |
| TaskVO              | 任务视图   |
| HistoricActivityVO  | 历史活动记录 |

### Param（参数对象）

| 类名                | 说明     |
|-------------------|--------|
| StartProcessParam | 启动流程参数 |
| CompleteTaskParam | 完成任务参数 |
| DelegateTaskParam | 委托任务参数 |
| TransferTaskParam | 转办任务参数 |
| TaskQueryParam    | 任务查询参数 |

### 枚举

| 枚举类               | 说明     |
|-------------------|--------|
| ProcessStatusEnum | 流程状态枚举 |
| TaskStatusEnum    | 任务状态枚举 |

## BPMN流程定义示例

```xml
<?xml version="1.0" encoding="UTF-8"?>
<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xmlns:flowable="http://flowable.org/bpmn"
             targetNamespace="http://www.carlos.com/leave">

    <process id="leaveProcess" name="请假审批流程">
        <startEvent id="start" name="开始"/>
        
        <userTask id="apply" name="提交申请" flowable:assignee="${applicant}"/>
        
        <userTask id="managerApprove" name="部门经理审批" flowable:candidateGroups="manager"/>
        
        <userTask id="hrApprove" name="HR审批" flowable:candidateGroups="hr"/>
        
        <endEvent id="end" name="结束"/>
        
        <sequenceFlow sourceRef="start" targetRef="apply"/>
        <sequenceFlow sourceRef="apply" targetRef="managerApprove"/>
        <sequenceFlow sourceRef="managerApprove" targetRef="hrApprove" 
                      conditionExpression="${approved==true}"/>
        <sequenceFlow sourceRef="managerApprove" targetRef="apply" 
                      conditionExpression="${approved==false}"/>
        <sequenceFlow sourceRef="hrApprove" targetRef="end"/>
    </process>
</definitions>
```

## 注意事项

1. **数据库配置**: Flowable会自动创建所需的34张表，建议在生产环境使用独立的数据库schema
2. **流程定义文件**: 流程定义文件需要符合BPMN 2.0规范，支持`.bpmn20.xml`和`.bpmn`格式
3. **历史记录级别**:
    - `none`: 不记录历史
    - `activity`: 只记录活动历史
    - `audit`: 记录活动历史和任务历史（推荐）
    - `full`: 记录所有历史信息
4. **异步执行器**: 生产环境建议启用异步执行器，提高流程执行性能
5. **事务管理**: Flowable操作默认参与Spring事务，异常时会自动回滚

## 依赖模块

- **carlos-spring-boot-core**: 核心基础功能
- **flowable-spring-boot-starter**: Flowable Spring Boot Starter
- **flowable-spring**: Flowable Spring集成

## 更多信息

- [Flowable官方文档](https://www.flowable.com/open-source/docs/)
- [BPMN 2.0规范](https://www.omg.org/spec/BPMN/2.0/)
