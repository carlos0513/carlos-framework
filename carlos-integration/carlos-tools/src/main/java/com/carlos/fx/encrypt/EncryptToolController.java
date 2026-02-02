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

import java.util.ArrayList;
import java.util.List;

/**
 * Encrypt Tool Controller
 *
 * @author Carlos
 * @since 3.0.0
 */
public class EncryptToolController extends BaseController {

    @FXML
    private ComboBox<DbTypeEnum> databaseTypeCombo;

    @FXML
    private TextField hostField;

    @FXML
    private TextField portField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField databaseField;

    @FXML
    private Button testConnectionButton;

    @FXML
    private Button loadTablesButton;

    @FXML
    private TreeView<String> tableTreeView;

    @FXML
    private TextField sm4KeyField;

    @FXML
    private TextField sm4IvField;

    @FXML
    private RadioButton encryptRadio;

    @FXML
    private RadioButton decryptRadio;

    @FXML
    private TextField batchSizeField;

    @FXML
    private Button executeButton;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label statusLabel;

    @FXML
    private TextArea logArea;

    private List<TableBean> allTables;
    private ToggleGroup operationGroup;

    @Override
    protected void initializeComponents() {
        allTables = new ArrayList<>();

        // Initialize database type combo
        databaseTypeCombo.setItems(FXCollections.observableArrayList(DbTypeEnum.values()));
        databaseTypeCombo.getSelectionModel().select(DbTypeEnum.MYSQL);

        // Set default values
        hostField.setText("localhost");
        portField.setText("3306");
        batchSizeField.setText("5000");

        // Initialize operation radio buttons
        operationGroup = new ToggleGroup();
        encryptRadio.setToggleGroup(operationGroup);
        decryptRadio.setToggleGroup(operationGroup);
        encryptRadio.setSelected(true);

        // Initialize tree view with checkboxes
        CheckBoxTreeItem<String> rootItem = new CheckBoxTreeItem<>("所有表");
        rootItem.setExpanded(true);
        tableTreeView.setRoot(rootItem);
        tableTreeView.setShowRoot(true);

        // Hide progress bar initially
        progressBar.setVisible(false);
        progressBar.setManaged(false);
        statusLabel.setText("");
    }

    @Override
    protected void setupEventHandlers() {
        testConnectionButton.setOnAction(e -> testConnection());
        loadTablesButton.setOnAction(e -> loadTables());
        executeButton.setOnAction(e -> executeOperation());

        // Enable/disable load tables button based on connection info
        loadTablesButton.disableProperty().bind(
                hostField.textProperty().isEmpty()
                        .or(portField.textProperty().isEmpty())
                        .or(usernameField.textProperty().isEmpty())
                        .or(databaseField.textProperty().isEmpty())
        );

        // Enable/disable execute button
        executeButton.disableProperty().bind(
                sm4KeyField.textProperty().isEmpty()
                        .or(sm4IvField.textProperty().isEmpty())
                        .or(batchSizeField.textProperty().isEmpty())
        );
    }

    /**
     * Test database connection
     */
    private void testConnection() {
        DatabaseInfo dbInfo = buildDatabaseInfo();

        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                DatabaseService service = new DatabaseService(dbInfo);
                // Try to get schemas to test connection
                service.getSchemas();
                return true;
            }
        };

        AsyncTaskUtil.execute(task,
                success -> {
                    DialogUtil.showSuccessNotification("连接成功", "数据库连接测试成功！");
                    appendLog("数据库连接测试成功");
                },
                error -> {
                    DialogUtil.showError("连接错误", "数据库连接失败", error);
                    appendLog("数据库连接错误: " + error.getMessage());
                }
        );
    }

    /**
     * Load tables from database
     */
    private void loadTables() {
        DatabaseInfo dbInfo = buildDatabaseInfo();

        progressBar.setVisible(true);
        progressBar.setManaged(true);
        statusLabel.setText("正在加载表信息...");
        appendLog("开始加载表信息...");

        Task<List<String>> task = new Task<>() {
            @Override
            protected List<String> call() throws Exception {
                DatabaseService service = new DatabaseService(dbInfo);
                return service.getTables();
            }
        };

        AsyncTaskUtil.execute(task,
                tableNames -> {
                    // Convert table names to TableBean objects
                    allTables.clear();
                    for (String tableName : tableNames) {
                        TableBean table = new TableBean();
                        table.setName(tableName);
                        table.setComment("");
                        allTables.add(table);
                    }
                    populateTableTree(allTables);
                    progressBar.setVisible(false);
                    progressBar.setManaged(false);
                    statusLabel.setText("加载完成，共 " + tableNames.size() + " 张表");
                    appendLog("成功加载 " + tableNames.size() + " 张表");
                    DialogUtil.showSuccessNotification("加载成功", "成功加载 " + tableNames.size() + " 张表");
                },
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
     * Populate table tree view
     */
    private void populateTableTree(List<TableBean> tables) {
        CheckBoxTreeItem<String> rootItem = (CheckBoxTreeItem<String>) tableTreeView.getRoot();
        rootItem.getChildren().clear();

        DatabaseInfo dbInfo = buildDatabaseInfo();
        DatabaseService service = new DatabaseService(dbInfo);

        for (TableBean table : tables) {
            String displayName = table.getName();
            if (table.getComment() != null && !table.getComment().isEmpty()) {
                displayName += " (" + table.getComment() + ")";
            }

            CheckBoxTreeItem<String> tableItem = new CheckBoxTreeItem<>(displayName);

            // Load columns for this table
            try {
                dbInfo.setSelectTable(table.getName());
                List<String> columns = service.getColumns(table.getName());
                for (String column : columns) {
                    CheckBoxTreeItem<String> fieldItem = new CheckBoxTreeItem<>(column);
                    tableItem.getChildren().add(fieldItem);
                }
            } catch (Exception e) {
                // If we can't load columns, just add the table without fields
                e.printStackTrace();
            }

            tableItem.setExpanded(false);
            rootItem.getChildren().add(tableItem);
        }
    }

    /**
     * Execute encryption/decryption operation
     */
    private void executeOperation() {
        // Get selected tables and fields
        List<TableBeanWrapper> selectedTables = getSelectedTablesWithFields();
        if (selectedTables.isEmpty()) {
            DialogUtil.showWarning("未选择表", "请至少选择一张表和字段进行操作！");
            return;
        }

        // Validate SM4 key and IV
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

        // Get operation type
        ToolType operationType = encryptRadio.isSelected() ? ToolType.SM4_ENCRYPT : ToolType.SM4_DECRYPT;
        String operationName = operationType == ToolType.SM4_ENCRYPT ? "加密" : "解密";

        // Confirm operation
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

        progressBar.setVisible(true);
        progressBar.setManaged(true);
        progressBar.setProgress(0);
        statusLabel.setText("正在执行" + operationName + "操作...");
        executeButton.setDisable(true);
        appendLog("========================================");
        appendLog("开始执行" + operationName + "操作");
        appendLog("批次大小: " + batchSize);

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                int totalTables = selectedTables.size();

                for (int i = 0; i < totalTables; i++) {
                    TableBeanWrapper table = selectedTables.get(i);
                    updateMessage("正在处理: " + table.getName());
                    updateProgress(i, totalTables);

                    appendLog("处理表: " + table.getName() + " (字段: " + table.getFields() + ")");

                    // Create ToolInfo for encryption
                    com.carlos.fx.encrypt.config.ToolInfo toolInfo = new com.carlos.fx.encrypt.config.ToolInfo();
                    toolInfo.setToolType(operationType);
                    toolInfo.setSourceTable(table.getName());
                    toolInfo.setSelectFields(table.getFields());

                    // Set encryption config
                    com.carlos.fx.encrypt.config.ToolInfo.Encrypt encrypt = new com.carlos.fx.encrypt.config.ToolInfo.Encrypt();
                    encrypt.setKey(sm4Key);
                    encrypt.setIv(sm4Iv);
                    toolInfo.setEncrypt(encrypt);

                    // Execute encryption/decryption
                    Executor executor = new Executor(dbInfo, toolInfo);
                    executor.execute();

                    appendLog("  完成: " + table.getName());
                }

                updateProgress(totalTables, totalTables);
                return null;
            }
        };

        task.messageProperty().addListener((obs, oldVal, newVal) -> statusLabel.setText(newVal));
        task.progressProperty().addListener((obs, oldVal, newVal) -> progressBar.setProgress(newVal.doubleValue()));

        AsyncTaskUtil.execute(task,
                result -> {
                    progressBar.setVisible(false);
                    progressBar.setManaged(false);
                    statusLabel.setText(operationName + "操作完成！");
                    executeButton.setDisable(false);
                    appendLog(operationName + "操作完成！");
                    appendLog("========================================");
                    DialogUtil.showSuccessNotification("操作成功", operationName + "操作已完成！");
                },
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
     * Get selected tables with fields from tree view
     */
    private List<TableBeanWrapper> getSelectedTablesWithFields() {
        List<TableBeanWrapper> selectedTables = new ArrayList<>();
        CheckBoxTreeItem<String> rootItem = (CheckBoxTreeItem<String>) tableTreeView.getRoot();

        for (int i = 0; i < rootItem.getChildren().size(); i++) {
            CheckBoxTreeItem<String> tableItem = (CheckBoxTreeItem<String>) rootItem.getChildren().get(i);

            // Get selected fields
            List<String> selectedFields = new ArrayList<>();
            for (TreeItem<String> fieldItem : tableItem.getChildren()) {
                CheckBoxTreeItem<String> checkBoxFieldItem = (CheckBoxTreeItem<String>) fieldItem;
                if (checkBoxFieldItem.isSelected()) {
                    selectedFields.add(checkBoxFieldItem.getValue());
                }
            }

            // If table has selected fields, add to list
            if (!selectedFields.isEmpty()) {
                TableBean table = allTables.get(i);
                TableBeanWrapper wrapper = new TableBeanWrapper(table.getName(), table.getComment(), selectedFields);
                selectedTables.add(wrapper);
            }
        }

        return selectedTables;
    }

    /**
     * Wrapper class for TableBean with selected fields
     */
    private static class TableBeanWrapper {
        private final String name;
        private final String comment;
        private final List<String> fields;

        public TableBeanWrapper(String name, String comment, List<String> fields) {
            this.name = name;
            this.comment = comment;
            this.fields = fields;
        }

        public String getName() {
            return name;
        }

        public String getComment() {
            return comment;
        }

        public List<String> getFields() {
            return fields;
        }
    }

    /**
     * Build DatabaseInfo from form fields
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
     * Append log message
     */
    private void appendLog(String message) {
        AsyncTaskUtil.runOnFxThread(() -> {
            logArea.appendText(message + "\n");
        });
    }
}
