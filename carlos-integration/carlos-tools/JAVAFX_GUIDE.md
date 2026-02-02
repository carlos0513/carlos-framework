# JavaFX 开发指南

本文档为 Carlos Tools 项目的 JavaFX 开发提供详细的中文指南，帮助开发者理解和使用 JavaFX 进行 GUI 开发。

## 目录

1. [JavaFX 基础概念](#javafx-基础概念)
2. [FXML 文件说明](#fxml-文件说明)
3. [Controller 控制器](#controller-控制器)
4. [常用 UI 组件](#常用-ui-组件)
5. [事件处理](#事件处理)
6. [属性绑定](#属性绑定)
7. [异步任务](#异步任务)
8. [项目初始化示例](#项目初始化示例)

---

## JavaFX 基础概念

### 什么是 JavaFX？

JavaFX 是 Java 的现代 GUI 框架，用于构建桌面应用程序。它提供了丰富的 UI 组件、CSS 样式支持、动画效果等功能。

### MVC 架构

JavaFX 应用通常采用 MVC（Model-View-Controller）架构：

- **Model（模型）**：数据模型，如 `GitlabBranch`、`GitlabIssue` 等实体类
- **View（视图）**：FXML 文件定义的 UI 界面
- **Controller（控制器）**：Java 类，处理用户交互和业务逻辑

### 项目结构

```
carlos-tools/
├── src/main/java/com/carlos/tool/fx/
│   ├── gitlab/                    # GitLab 工具控制器
│   │   ├── GitlabMainController.java
│   │   ├── BranchManagementController.java
│   │   ├── IssueManagementController.java
│   │   ├── MergeRequestController.java
│   │   └── UserManagementController.java
│   └── common/                    # 公共组件和工具类
│       ├── controller/BaseController.java
│       └── util/DialogUtil.java
├── src/main/resources/
│   ├── fxml/                      # FXML 界面文件
│   │   ├── gitlabmain.fxml
│   │   ├── branchmanagement.fxml
│   │   └── ...
│   └── css/                       # CSS 样式文件
│       ├── main.css
│       ├── light-theme.css
│       └── dark-theme.css
```

---

## FXML 文件说明

### 什么是 FXML？

FXML 是一种基于 XML 的标记语言，用于定义 JavaFX 界面。它将 UI 布局与业务逻辑分离，使代码更清晰。

### FXML 基本结构

```xml
<?xml version="1.0" encoding="UTF-8"?>

<!-- 导入需要的 JavaFX 组件 -->
<?import javafx.scene.control.*?>
<?import javafx.scene.layout.*?>

<!-- 根容器：ScrollPane 支持滚动 -->
<ScrollPane xmlns="http://javafx.com/javafx"
            xmlns:fx="http://javafx.com/fxml"
            fitToWidth="true"
            fitToHeight="true">

    <!-- 主容器：VBox 垂直布局 -->
    <VBox fx:controller="com.carlos.fx.gitlab.controller.BranchManagementController"
          spacing="15" style="-fx-padding: 15;">

        <!-- UI 组件 -->
        <Button fx:id="refreshButton" text="刷新"/>

    </VBox>

</ScrollPane>
```

### 重要属性说明

- **fx:id**：组件的唯一标识符，用于在 Controller 中引用
- **fx:controller**：指定控制器类的完整路径
- **fitToWidth/fitToHeight**：ScrollPane 自适应宽度/高度
- **spacing**：子组件之间的间距
- **style**：内联 CSS 样式
- **styleClass**：CSS 类名

### 常用布局容器

| 容器             | 说明   | 使用场景            |
|----------------|------|-----------------|
| **VBox**       | 垂直布局 | 从上到下排列组件        |
| **HBox**       | 水平布局 | 从左到右排列组件        |
| **BorderPane** | 边界布局 | 分为上、下、左、右、中五个区域 |
| **GridPane**   | 网格布局 | 表格形式排列组件        |
| **StackPane**  | 堆叠布局 | 组件层叠显示          |
| **ScrollPane** | 滚动面板 | 内容超出时显示滚动条      |

---

## Controller 控制器

### 控制器基本结构

```java
public class BranchManagementController extends BaseController {

    // 1. FXML 注入的 UI 组件
    @FXML
    private Button refreshButton;

    @FXML
    private TableView<BranchItem> branchTable;

    // 2. 业务逻辑字段
    private BranchService branchService;
    private ObservableList<BranchItem> branches;

    // 3. 初始化组件
    @Override
    protected void initializeComponents() {
        // 初始化数据和 UI 状态
        branches = FXCollections.observableArrayList();
        branchTable.setItems(branches);
    }

    // 4. 设置事件处理器
    @Override
    protected void setupEventHandlers() {
        // 绑定按钮点击事件
        refreshButton.setOnAction(e -> loadBranches());
    }

    // 5. 业务逻辑方法
    private void loadBranches() {
        // 加载分支列表
    }
}
```

### @FXML 注解

`@FXML` 注解用于标记需要从 FXML 文件注入的字段和方法：

```java
// 注入 UI 组件
@FXML
private Button myButton;

@FXML
private TextField myTextField;

// 注入事件处理方法（在 FXML 中使用 onAction="#handleClick"）
@FXML
private void handleClick(ActionEvent event) {
    System.out.println("Button clicked!");
}
```

**注意**：

- 字段必须声明为 `private`
- 字段名必须与 FXML 中的 `fx:id` 完全一致
- 不需要手动赋值，JavaFX 会自动注入

---

## 常用 UI 组件

### Button（按钮）

```xml
<!-- FXML -->
<Button fx:id="saveButton" text="保存" styleClass="primary-button"/>
```

```java
// Controller
@FXML
private Button saveButton;

// 设置点击事件
saveButton.setOnAction(e -> save());

// 禁用/启用按钮
saveButton.setDisable(true);
saveButton.setDisable(false);
```

### TextField（文本输入框）

```xml
<!-- FXML -->
<TextField fx:id="searchField" promptText="搜索..."/>
```

```java
// Controller
@FXML
private TextField searchField;

// 获取文本
String text = searchField.getText();

// 设置文本
searchField.setText("Hello");

// 监听文本变化
searchField.textProperty().addListener((obs, oldVal, newVal) -> {
    System.out.println("Text changed: " + newVal);
});
```

### ComboBox（下拉框）

```xml
<!-- FXML -->
<ComboBox fx:id="projectCombo" prefWidth="200"/>
```

```java
// Controller
@FXML
private ComboBox<String> projectCombo;

// 设置选项
projectCombo.setItems(FXCollections.observableArrayList(
    "Project 1", "Project 2", "Project 3"
));

// 选中第一项
projectCombo.getSelectionModel().select(0);

// 获取选中项
String selected = projectCombo.getValue();

// 监听选择变化
projectCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
    System.out.println("Selected: " + newVal);
});
```

### TableView（表格）

```xml
<!-- FXML -->
<TableView fx:id="branchTable">
    <columns>
        <TableColumn fx:id="nameColumn" text="名称" prefWidth="200"/>
        <TableColumn fx:id="dateColumn" text="日期" prefWidth="150"/>
    </columns>
</TableView>
```

```java
// Controller
@FXML
private TableView<BranchItem> branchTable;

@FXML
private TableColumn<BranchItem, String> nameColumn;

@FXML
private TableColumn<BranchItem, Date> dateColumn;

// 初始化表格
ObservableList<BranchItem> data = FXCollections.observableArrayList();
branchTable.setItems(data);

// 绑定列到数据模型
nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

// 自定义单元格显示
dateColumn.setCellFactory(column -> new TableCell<>() {
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void updateItem(Date item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
        } else {
            setText(format.format(item));
        }
    }
});

// 获取选中项
BranchItem selected = branchTable.getSelectionModel().getSelectedItem();
```

### ProgressIndicator（进度指示器）

```xml
<!-- FXML -->
<ProgressIndicator fx:id="progressIndicator" prefWidth="24" prefHeight="24"/>
```

```java
// Controller
@FXML
private ProgressIndicator progressIndicator;

// 显示/隐藏
progressIndicator.setVisible(true);
progressIndicator.setVisible(false);

// 设置进度（0.0 到 1.0）
progressIndicator.setProgress(0.5);

// 不确定进度（旋转动画）
progressIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
```

---

## 事件处理

### 按钮点击事件

```java
// 方式 1：Lambda 表达式
button.setOnAction(e -> {
    System.out.println("Button clicked!");
});

// 方式 2：方法引用
button.setOnAction(e -> handleButtonClick());

// 方式 3：在 FXML 中指定
// FXML: <Button onAction="#handleButtonClick"/>
@FXML
private void handleButtonClick(ActionEvent event) {
    System.out.println("Button clicked!");
}
```

### 文本变化监听

```java
textField.textProperty().addListener((observable, oldValue, newValue) -> {
    System.out.println("Old: " + oldValue + ", New: " + newValue);
});
```

### 选择变化监听

```java
comboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
    System.out.println("Selected: " + newVal);
});
```

### 表格行选择监听

```java
tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
    if (newVal != null) {
        System.out.println("Selected row: " + newVal);
    }
});
```

---

## 属性绑定

### 什么是属性绑定？

JavaFX 的属性绑定允许一个属性自动跟随另一个属性的变化，无需手动更新。

### 单向绑定

```java
// 按钮禁用状态绑定到表格选择状态
// 当表格没有选中项时，按钮自动禁用
deleteButton.disableProperty().bind(
    tableView.getSelectionModel().selectedItemProperty().isNull()
);

// 标签文本绑定到文本框
label.textProperty().bind(textField.textProperty());
```

### 双向绑定

```java
// 两个文本框内容同步
textField1.textProperty().bindBidirectional(textField2.textProperty());
```

### ObservableList

`ObservableList` 是 JavaFX 的可观察列表，当列表内容变化时会自动通知 UI 更新：

```java
// 创建可观察列表
ObservableList<String> items = FXCollections.observableArrayList();

// 绑定到 UI 组件
listView.setItems(items);
tableView.setItems(items);

// 修改列表，UI 自动更新
items.add("New Item");
items.remove(0);
items.clear();
```

### Property 类型

JavaFX 提供了多种 Property 类型：

```java
// 字符串属性
StringProperty nameProperty = new SimpleStringProperty("John");

// 整数属性
IntegerProperty ageProperty = new SimpleIntegerProperty(25);

// 布尔属性
BooleanProperty selectedProperty = new SimpleBooleanProperty(false);

// 对象属性
ObjectProperty<Date> dateProperty = new SimpleObjectProperty<>(new Date());

// 获取值
String name = nameProperty.get();

// 设置值
nameProperty.set("Jane");

// 监听变化
nameProperty.addListener((obs, oldVal, newVal) -> {
    System.out.println("Name changed: " + oldVal + " -> " + newVal);
});
```

---

## 异步任务

### 为什么需要异步任务？

JavaFX 的 UI 更新必须在主线程（JavaFX Application Thread）执行。如果在主线程执行耗时操作（如网络请求、文件读写），会导致界面卡顿。

### Task 类

`Task` 是 JavaFX 提供的异步任务类：

```java
// 创建异步任务
Task<List<GitlabBranch>> task = new Task<>() {
    @Override
    protected List<GitlabBranch> call() throws Exception {
        // 这里的代码在后台线程执行
        return branchService.listBranches(projectId);
    }
};

// 成功回调（在主线程执行）
task.setOnSucceeded(event -> {
    List<GitlabBranch> result = task.getValue();
    branches.clear();
    branches.addAll(result);
});

// 失败回调（在主线程执行）
task.setOnFailed(event -> {
    Throwable error = task.getException();
    error.printStackTrace();
});

// 启动任务
new Thread(task).start();
```

### AsyncTaskUtil 工具类

项目中封装了 `AsyncTaskUtil` 简化异步任务的使用：

```java
// 显示进度指示器
progressIndicator.setVisible(true);

// 创建任务
Task<List<GitlabBranch>> task = new Task<>() {
    @Override
    protected List<GitlabBranch> call() throws Exception {
        return branchService.listBranches(projectId);
    }
};

// 执行任务
AsyncTaskUtil.execute(task,
    result -> {
        // 成功回调
        branches.clear();
        branches.addAll(result);
        progressIndicator.setVisible(false);
        DialogUtil.showSuccessNotification("加载成功", "成功加载 " + result.size() + " 个分支");
    },
    error -> {
        // 失败回调
        progressIndicator.setVisible(false);
        DialogUtil.showError("加载错误", "加载分支失败", error);
    }
);
```

### Platform.runLater

如果需要在后台线程更新 UI，使用 `Platform.runLater`：

```java
// 在后台线程中
new Thread(() -> {
    // 执行耗时操作
    String result = doSomething();

    // 更新 UI（必须在主线程）
    Platform.runLater(() -> {
        label.setText(result);
    });
}).start();
```

---

## 项目初始化示例

### 示例 1：初始化项目下拉框

```java
/**
 * 初始化项目下拉框
 * 从 GitLab API 加载项目列表
 */
private void initializeProjectCombo() {
    // 显示进度指示器
    progressIndicator.setVisible(true);

    // 创建异步任务
    Task<List<Project>> task = new Task<>() {
        @Override
        protected List<Project> call() throws Exception {
            // 调用 GitLab API 获取项目列表
            return gitLabApi.getProjectApi().getProjects();
        }
    };

    // 执行任务
    AsyncTaskUtil.execute(task,
        projects -> {
            // 成功：填充下拉框
            ObservableList<String> projectNames = FXCollections.observableArrayList();
            projects.forEach(project -> projectNames.add(project.getName()));
            projectCombo.setItems(projectNames);

            // 默认选中第一个项目
            if (!projectNames.isEmpty()) {
                projectCombo.getSelectionModel().select(0);
            }

            progressIndicator.setVisible(false);
        },
        error -> {
            // 失败：显示错误
            progressIndicator.setVisible(false);
            DialogUtil.showError("加载错误", "加载项目列表失败", error);
        }
    );
}
```

### 示例 2：初始化用户列表

```java
/**
 * 初始化用户列表
 * 从 GitLab API 加载所有用户
 */
private void initializeUserList() {
    if (userService == null) {
        return;
    }

    progressIndicator.setVisible(true);

    Task<List<GitlabUser>> task = new Task<>() {
        @Override
        protected List<GitlabUser> call() throws Exception {
            return userService.listUsers();
        }
    };

    AsyncTaskUtil.execute(task,
        users -> {
            // 转换为 UserItem 并添加到列表
            ObservableList<UserItem> userItems = FXCollections.observableArrayList();
            users.forEach(user -> userItems.add(new UserItem(user)));
            userTable.setItems(userItems);

            progressIndicator.setVisible(false);
            DialogUtil.showSuccessNotification("加载成功", "成功加载 " + users.size() + " 个用户");
        },
        error -> {
            progressIndicator.setVisible(false);
            DialogUtil.showError("加载错误", "加载用户列表失败", error);
        }
    );
}
```

### 示例 3：从配置文件加载服务器列表

```java
/**
 * 从配置文件加载 GitLab 服务器列表
 */
private void loadServersFromConfig() {
    try {
        // 配置文件路径
        File configFile = new File(System.getProperty("user.home"), ".carlos-tools/gitlab-servers.json");

        if (!configFile.exists()) {
            // 配置文件不存在，使用默认值
            serverCombo.getItems().addAll("http://gitlab.example.com");
            return;
        }

        // 读取配置文件
        ObjectMapper mapper = new ObjectMapper();
        GitLabServerConfig config = mapper.readValue(configFile, GitLabServerConfig.class);

        // 填充下拉框
        ObservableList<String> serverNames = FXCollections.observableArrayList();
        config.getServers().forEach(server -> serverNames.add(server.getName()));
        serverCombo.setItems(serverNames);

        // 默认选中第一个服务器
        if (!serverNames.isEmpty()) {
            serverCombo.getSelectionModel().select(0);
        }

    } catch (IOException e) {
        e.printStackTrace();
        DialogUtil.showError("配置错误", "加载服务器配置失败", e);
    }
}

/**
 * 服务器配置类
 */
public static class GitLabServerConfig {
    private List<GitLabServer> servers;

    // Getter and Setter
    public List<GitLabServer> getServers() {
        return servers;
    }

    public void setServers(List<GitLabServer> servers) {
        this.servers = servers;
    }
}

/**
 * GitLab 服务器信息
 */
public static class GitLabServer {
    private String name;
    private String url;
    private String token;

    // Getter and Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
```

### 示例 4：保存配置到文件

```java
/**
 * 保存服务器配置到文件
 */
private void saveServerConfig(GitLabServer server) {
    try {
        // 配置文件路径
        File configDir = new File(System.getProperty("user.home"), ".carlos-tools");
        if (!configDir.exists()) {
            configDir.mkdirs();
        }

        File configFile = new File(configDir, "gitlab-servers.json");

        // 读取现有配置
        GitLabServerConfig config;
        if (configFile.exists()) {
            ObjectMapper mapper = new ObjectMapper();
            config = mapper.readValue(configFile, GitLabServerConfig.class);
        } else {
            config = new GitLabServerConfig();
            config.setServers(new ArrayList<>());
        }

        // 添加新服务器
        config.getServers().add(server);

        // 保存到文件
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(configFile, config);

        DialogUtil.showSuccessNotification("保存成功", "服务器配置已保存");

    } catch (IOException e) {
        e.printStackTrace();
        DialogUtil.showError("保存错误", "保存服务器配置失败", e);
    }
}
```

---

## 总结

本指南涵盖了 JavaFX 开发的核心概念和常用技术。在实际开发中：

1. **FXML 定义界面**：使用 FXML 文件定义 UI 布局，保持代码清晰
2. **Controller 处理逻辑**：在控制器中处理用户交互和业务逻辑
3. **属性绑定**：利用 JavaFX 的属性绑定简化 UI 更新
4. **异步任务**：使用 Task 执行耗时操作，避免界面卡顿
5. **代码注释**：为关键代码添加详细的中文注释，方便维护

更多信息请参考：

- [JavaFX 官方文档](https://openjfx.io/)
- [JavaFX API 文档](https://openjfx.io/javadoc/17/)
