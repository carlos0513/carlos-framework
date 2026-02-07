package com.carlos.fx.encrypt;

import com.carlos.fx.common.controller.BaseController;
import com.carlos.fx.common.util.AsyncTaskUtil;
import com.carlos.fx.common.util.DialogUtil;
import com.carlos.fx.encrypt.config.DatabaseInfo;
import com.carlos.fx.encrypt.entity.TableBean;
import com.carlos.fx.encrypt.enums.DbTypeEnum;
import com.carlos.fx.encrypt.enums.ToolType;
import com.carlos.fx.encrypt.service.DatabaseService;
import com.carlos.fx.encrypt.service.Executor;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 加密工具控制器
 * <p>
 * 该控制器负责管理数据库字段加密/解密工具的界面和业务逻辑。
 * 支持对数据库表中的敏感字段进行批量SM4加密和解密操作。
 * </p>
 *
 * <p>主要功能：</p>
 * <ul>
 *   <li>连接数据库并测试连接状态</li>
 *   <li>加载数据库表和字段信息</li>
 *   <li>支持树形结构选择需要加密/解密的表和字段</li>
 *   <li>使用SM4国密算法进行加密/解密操作</li>
 *   <li>支持批量处理，可配置批次大小</li>
 *   <li>实时显示操作进度和日志</li>
 *   <li>异步执行，不阻塞UI线程</li>
 * </ul>
 *
 * <p>使用场景：</p>
 * <ul>
 *   <li>对生产数据库中的敏感字段（如手机号、身份证号）进行加密</li>
 *   <li>在需要时对加密数据进行解密</li>
 *   <li>数据迁移时的加密/解密处理</li>
 * </ul>
 *
 * <p>安全说明：</p>
 * <ul>
 *   <li>使用SM4国密算法（中国国家密码管理局认定的商用密码算法）</li>
 *   <li>密钥长度：32位（128位密钥的十六进制表示）</li>
 *   <li>向量长度：32位（128位IV的十六进制表示）</li>
 *   <li>支持CBC加密模式</li>
 * </ul>
 *
 * @author Carlos
 * @since 3.0.0
 */
@Component
public class EncryptToolController extends BaseController {

    /** 数据库类型下拉框（MySQL、PostgreSQL等） */
    @FXML
    private ComboBox<DbTypeEnum> databaseTypeCombo;

    /** 数据库主机地址输入框 */
    @FXML
    private TextField hostField;

    /** 数据库端口输入框 */
    @FXML
    private TextField portField;

    /** 数据库用户名输入框 */
    @FXML
    private TextField usernameField;

    /** 数据库密码输入框（密码框，输入内容不可见） */
    @FXML
    private PasswordField passwordField;

    /** 数据库名称输入框 */
    @FXML
    private TextField databaseField;

    /** 测试连接按钮 */
    @FXML
    private Button testConnectionButton;

    /** 加载表按钮 */
    @FXML
    private Button loadTablesButton;

    /** 表和字段树形视图（支持复选框选择） */
    @FXML
    private TreeView<String> tableTreeView;

    /** SM4密钥输入框（32位十六进制字符串） */
    @FXML
    private TextField sm4KeyField;

    /** SM4初始化向量输入框（32位十六进制字符串） */
    @FXML
    private TextField sm4IvField;

    /** 加密操作单选按钮 */
    @FXML
    private RadioButton encryptRadio;

    /** 解密操作单选按钮 */
    @FXML
    private RadioButton decryptRadio;

    /** 批次大小输入框（每批处理的记录数） */
    @FXML
    private TextField batchSizeField;

    /** 执行按钮 */
    @FXML
    private Button executeButton;

    /** 进度条（显示操作进度） */
    @FXML
    private ProgressBar progressBar;

    /** 状态标签（显示当前操作状态） */
    @FXML
    private Label statusLabel;

    /** 日志文本区域（显示操作日志） */
    @FXML
    private TextArea logArea;

    /** 所有表的列表（用于保存从数据库加载的表信息） */
    private List<TableBean> allTables;

    /** 操作类型单选按钮组（加密/解密） */
    private ToggleGroup operationGroup;

    /**
     * 初始化界面组件
     * <p>
     * 设置表单的默认值、初始化数据库类型列表、配置单选按钮组和树形视图。
     * </p>
     */
    @Override
    protected void initializeComponents() {
        // 初始化表列表
        allTables = new ArrayList<>();

        // 初始化数据库类型下拉框，添加所有支持的数据库类型
        databaseTypeCombo.setItems(FXCollections.observableArrayList(DbTypeEnum.values()));
        // 默认选中MySQL
        databaseTypeCombo.getSelectionModel().select(DbTypeEnum.MYSQL);

        // 设置默认值
        hostField.setText("localhost");
        portField.setText("3306");
        batchSizeField.setText("5000"); // 默认每批处理5000条记录

        // 初始化操作类型单选按钮组
        operationGroup = new ToggleGroup();
        encryptRadio.setToggleGroup(operationGroup);
        decryptRadio.setToggleGroup(operationGroup);
        encryptRadio.setSelected(true); // 默认选中加密操作

        // 初始化树形视图，使用复选框树项
        CheckBoxTreeItem<String> rootItem = new CheckBoxTreeItem<>("所有表");
        rootItem.setExpanded(true); // 默认展开根节点
        tableTreeView.setRoot(rootItem);
        tableTreeView.setShowRoot(true); // 显示根节点

        // 初始隐藏进度条
        progressBar.setVisible(false);
        progressBar.setManaged(false);
        statusLabel.setText("");
    }

    /**
     * 设置事件处理器
     * <p>
     * 配置各种用户交互事件的处理逻辑，包括按钮点击和表单验证。
     * </p>
     */
    @Override
    protected void setupEventHandlers() {
        // 测试连接按钮：测试数据库连接是否正常
        testConnectionButton.setOnAction(e -> testConnection());
        // 加载表按钮：从数据库加载所有表和字段信息
        loadTablesButton.setOnAction(e -> loadTables());
        // 执行按钮：执行加密或解密操作
        executeButton.setOnAction(e -> executeOperation());

        // 表单验证：只有填写了必要的数据库连接信息后才启用"加载表"按钮
        loadTablesButton.disableProperty().bind(
                hostField.textProperty().isEmpty()
                        .or(portField.textProperty().isEmpty())
                        .or(usernameField.textProperty().isEmpty())
                        .or(databaseField.textProperty().isEmpty())
        );

        // 表单验证：只有填写了SM4密钥、向量和批次大小后才启用"执行"按钮
        executeButton.disableProperty().bind(
                sm4KeyField.textProperty().isEmpty()
                        .or(sm4IvField.textProperty().isEmpty())
                        .or(batchSizeField.textProperty().isEmpty())
        );
    }

    /**
     * 测试数据库连接
     * <p>
     * 使用用户输入的数据库连接信息尝试连接数据库，验证连接是否成功。
     * 该操作在后台线程中异步执行，不会阻塞UI。
     * </p>
     */
    private void testConnection() {
        // 构建数据库连接信息对象
        DatabaseInfo dbInfo = buildDatabaseInfo();

        // 创建异步任务
        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                DatabaseService service = new DatabaseService(dbInfo);
                // 尝试获取数据库架构列表来测试连接
                service.getSchemas();
                return true;
            }
        };

        // 执行异步任务
        AsyncTaskUtil.execute(task,
                // 成功回调
                success -> {
                    DialogUtil.showSuccessNotification("连接成功", "数据库连接测试成功！");
                    appendLog("数据库连接测试成功");
                },
                // 失败回调
                error -> {
                    DialogUtil.showError("连接错误", "数据库连接失败", error);
                    appendLog("数据库连接错误: " + error.getMessage());
                }
        );
    }

    /**
     * 从数据库加载表信息
     * <p>
     * 连接数据库并获取所有表的列表，然后在树形视图中显示。
     * 该操作在后台线程中异步执行。
     * </p>
     */
    private void loadTables() {
        DatabaseInfo dbInfo = buildDatabaseInfo();

        // 显示进度条和状态信息
        progressBar.setVisible(true);
        progressBar.setManaged(true);
        statusLabel.setText("正在加载表信息...");
        appendLog("开始加载表信息...");

        // 创建异步任务
        Task<List<String>> task = new Task<>() {
            @Override
            protected List<String> call() throws Exception {
                DatabaseService service = new DatabaseService(dbInfo);
                return service.getTables();
            }
        };

        // 执行异步任务
        AsyncTaskUtil.execute(task,
                // 成功回调
                tableNames -> {
                    // 将表名转换为TableBean对象
                    allTables.clear();
                    for (String tableName : tableNames) {
                        TableBean table = new TableBean();
                        table.setName(tableName);
                        table.setComment("");
                        allTables.add(table);
                    }
                    // 填充树形视图
                    populateTableTree(allTables);
                    // 隐藏进度条
                    progressBar.setVisible(false);
                    progressBar.setManaged(false);
                    statusLabel.setText("加载完成，共 " + tableNames.size() + " 张表");
                    appendLog("成功加载 " + tableNames.size() + " 张表");
                    DialogUtil.showSuccessNotification("加载成功", "成功加载 " + tableNames.size() + " 张表");
                },
                // 失败回调
                error -> {
                    progressBar.setVisible(false);
                    progressBar.setManaged(false);
                    statusLabel.setText("加载失败");
                    appendLog("加载表信息失败: " + error.getMessage());
                    DialogUtil.showError("加载错误", "加载表信息失败", error);
                }
        );
    }

    /**
     * 填充表树形视图
     * <p>
     * 将数据库表和字段信息以树形结构显示在界面上。
     * 每个表作为一个父节点，该表的所有字段作为子节点。
     * 所有节点都支持复选框选择。
     * </p>
     *
     * @param tables 表列表
     */
    private void populateTableTree(List<TableBean> tables) {
        CheckBoxTreeItem<String> rootItem = (CheckBoxTreeItem<String>) tableTreeView.getRoot();
        rootItem.getChildren().clear();

        DatabaseInfo dbInfo = buildDatabaseInfo();
        DatabaseService service = new DatabaseService(dbInfo);

        // 遍历所有表
        for (TableBean table : tables) {
            // 构建表的显示名称（表名 + 注释）
            String displayName = table.getName();
            if (table.getComment() != null && !table.getComment().isEmpty()) {
                displayName += " (" + table.getComment() + ")";
            }

            // 创建表节点
            CheckBoxTreeItem<String> tableItem = new CheckBoxTreeItem<>(displayName);

            // 加载该表的所有字段
            try {
                dbInfo.setSelectTable(table.getName());
                List<String> columns = service.getColumns(table.getName());
                // 为每个字段创建子节点
                for (String column : columns) {
                    CheckBoxTreeItem<String> fieldItem = new CheckBoxTreeItem<>(column);
                    tableItem.getChildren().add(fieldItem);
                }
            } catch (Exception e) {
                // 如果无法加载字段，只添加表节点，不添加字段子节点
                e.printStackTrace();
            }

            // 默认不展开表节点（避免界面过于拥挤）
            tableItem.setExpanded(false);
            rootItem.getChildren().add(tableItem);
        }
    }

    /**
     * 执行加密/解密操作
     * <p>
     * 根据用户选择的操作类型（加密或解密），对选中的表和字段执行批量处理。
     * 该操作在后台线程中异步执行，实时更新进度和日志。
     * </p>
     */
    private void executeOperation() {
        // 获取用户选中的表和字段
        List<TableBeanWrapper> selectedTables = getSelectedTablesWithFields();
        if (selectedTables.isEmpty()) {
            DialogUtil.showWarning("未选择表", "请至少选择一张表和字段进行操作！");
            return;
        }

        // 验证SM4密钥和向量
        String sm4Key = sm4KeyField.getText().trim();
        String sm4Iv = sm4IvField.getText().trim();

        if (sm4Key.length() != 32) {
            DialogUtil.showWarning("密钥错误", "SM4密钥长度必须为32位！");
            return;
        }

        if (sm4Iv.length() != 32) {
            DialogUtil.showWarning("向量错误", "SM4向量长度必须为32位！");
            return;
        }

        // 确定操作类型（加密或解密）
        ToolType operationType = encryptRadio.isSelected() ? ToolType.SM4_ENCRYPT : ToolType.SM4_DECRYPT;
        String operationName = operationType == ToolType.SM4_ENCRYPT ? "加密" : "解密";

        // 显示确认对话框
        int totalFields = selectedTables.stream().mapToInt(t -> t.getFields().size()).sum();
        boolean confirmed = DialogUtil.showConfirm(
                "确认" + operationName,
                "将对 " + selectedTables.size() + " 张表的 " + totalFields + " 个字段进行" + operationName + "操作，是否继续？"
        );

        if (!confirmed) {
            return;
        }

        DatabaseInfo dbInfo = buildDatabaseInfo();
        int batchSize = Integer.parseInt(batchSizeField.getText().trim());

        // 显示进度条和状态信息
        progressBar.setVisible(true);
        progressBar.setManaged(true);
        progressBar.setProgress(0);
        statusLabel.setText("正在执行" + operationName + "操作...");
        executeButton.setDisable(true);
        appendLog("========================================");
        appendLog("开始执行" + operationName + "操作");
        appendLog("批次大小: " + batchSize);

        // 创建异步任务
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                int totalTables = selectedTables.size();

                // 遍历所有选中的表
                for (int i = 0; i < totalTables; i++) {
                    TableBeanWrapper table = selectedTables.get(i);
                    // 更新进度信息
                    updateMessage("正在处理: " + table.getName());
                    updateProgress(i, totalTables);

                    appendLog("处理表: " + table.getName() + " (字段: " + table.getFields() + ")");

                    // 创建工具配置对象
                    com.carlos.fx.encrypt.config.ToolInfo toolInfo = new com.carlos.fx.encrypt.config.ToolInfo();
                    toolInfo.setToolType(operationType);
                    toolInfo.setSourceTable(table.getName());
                    toolInfo.setSelectFields(table.getFields());

                    // 设置加密配置
                    com.carlos.fx.encrypt.config.ToolInfo.Encrypt encrypt = new com.carlos.fx.encrypt.config.ToolInfo.Encrypt();
                    encrypt.setKey(sm4Key);
                    encrypt.setIv(sm4Iv);
                    toolInfo.setEncrypt(encrypt);

                    // 执行加密/解密操作
                    Executor executor = new Executor(dbInfo, toolInfo);
                    executor.execute();

                    appendLog("  完成: " + table.getName());
                }

                updateProgress(totalTables, totalTables);
                return null;
            }
        };

        // 监听任务的消息和进度变化
        task.messageProperty().addListener((obs, oldVal, newVal) -> statusLabel.setText(newVal));
        task.progressProperty().addListener((obs, oldVal, newVal) -> progressBar.setProgress(newVal.doubleValue()));

        // 执行异步任务
        AsyncTaskUtil.execute(task,
                // 成功回调
                result -> {
                    progressBar.setVisible(false);
                    progressBar.setManaged(false);
                    statusLabel.setText(operationName + "操作完成！");
                    executeButton.setDisable(false);
                    appendLog(operationName + "操作完成！");
                    appendLog("========================================");
                    DialogUtil.showSuccessNotification("操作成功", operationName + "操作已完成！");
                },
                // 失败回调
                error -> {
                    progressBar.setVisible(false);
                    progressBar.setManaged(false);
                    statusLabel.setText(operationName + "操作失败");
                    executeButton.setDisable(false);
                    appendLog(operationName + "操作失败: " + error.getMessage());
                    appendLog("========================================");
                    DialogUtil.showError("操作错误", operationName + "操作失败", error);
                }
        );
    }

    /**
     * 从树形视图中获取选中的表和字段
     * <p>
     * 遍历树形视图，收集所有被选中的表及其字段。
     * 只有至少选中一个字段的表才会被包含在结果中。
     * </p>
     *
     * @return 选中的表和字段列表
     */
    private List<TableBeanWrapper> getSelectedTablesWithFields() {
        List<TableBeanWrapper> selectedTables = new ArrayList<>();
        CheckBoxTreeItem<String> rootItem = (CheckBoxTreeItem<String>) tableTreeView.getRoot();

        // 遍历所有表节点
        for (int i = 0; i < rootItem.getChildren().size(); i++) {
            CheckBoxTreeItem<String> tableItem = (CheckBoxTreeItem<String>) rootItem.getChildren().get(i);

            // 收集该表中被选中的字段
            List<String> selectedFields = new ArrayList<>();
            for (TreeItem<String> fieldItem : tableItem.getChildren()) {
                CheckBoxTreeItem<String> checkBoxFieldItem = (CheckBoxTreeItem<String>) fieldItem;
                if (checkBoxFieldItem.isSelected()) {
                    selectedFields.add(checkBoxFieldItem.getValue());
                }
            }

            // 如果该表有选中的字段，添加到结果列表
            if (!selectedFields.isEmpty()) {
                TableBean table = allTables.get(i);
                TableBeanWrapper wrapper = new TableBeanWrapper(table.getName(), table.getComment(), selectedFields);
                selectedTables.add(wrapper);
            }
        }

        return selectedTables;
    }

    /**
     * 表包装类
     * <p>
     * 用于封装表名、注释和选中的字段列表。
     * 这是一个内部辅助类，用于在加密/解密操作中传递数据。
     * </p>
     */
    private static class TableBeanWrapper {
        /** 表名 */
        private final String name;

        /** 表注释 */
        private final String comment;

        /** 选中的字段列表 */
        private final List<String> fields;

        /**
         * 构造表包装对象
         *
         * @param name    表名
         * @param comment 表注释
         * @param fields  选中的字段列表
         */
        public TableBeanWrapper(String name, String comment, List<String> fields) {
            this.name = name;
            this.comment = comment;
            this.fields = fields;
        }

        /**
         * 获取表名
         *
         * @return 表名
         */
        public String getName() {
            return name;
        }

        /**
         * 获取表注释
         *
         * @return 表注释
         */
        public String getComment() {
            return comment;
        }

        /**
         * 获取选中的字段列表
         *
         * @return 字段列表
         */
        public List<String> getFields() {
            return fields;
        }
    }

    /**
     * 根据表单字段构建数据库连接信息对象
     * <p>
     * 从界面的输入框中收集数据库连接信息，构建DatabaseInfo对象。
     * </p>
     *
     * @return 数据库连接信息对象
     */
    private DatabaseInfo buildDatabaseInfo() {
        DatabaseInfo dbInfo = new DatabaseInfo();
        dbInfo.setDbType(databaseTypeCombo.getValue());
        dbInfo.setIp(hostField.getText().trim());
        dbInfo.setPort(portField.getText().trim());
        dbInfo.setUser(usernameField.getText().trim());
        dbInfo.setPwd(passwordField.getText());
        dbInfo.setDbName(databaseField.getText().trim());
        dbInfo.setUrl(dbInfo.buildUrl());
        return dbInfo;
    }

    /**
     * 追加日志消息
     * <p>
     * 在日志文本区域追加一条日志消息。
     * 该方法确保在JavaFX UI线程中执行，避免线程安全问题。
     * </p>
     *
     * @param message 日志消息
     */
    private void appendLog(String message) {
        AsyncTaskUtil.runOnFxThread(() -> {
            logArea.appendText(message + "\n");
        });
    }
}
