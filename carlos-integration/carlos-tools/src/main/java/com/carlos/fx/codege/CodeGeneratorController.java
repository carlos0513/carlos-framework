package com.carlos.fx.codege;

import com.carlos.fx.codege.config.DatabaseInfo;
import com.carlos.fx.codege.config.ProjectInfo;
import com.carlos.fx.codege.entity.TableBean;
import com.carlos.fx.codege.entity.TemplateConfig;
import com.carlos.fx.codege.enums.DbTypeEnum;
import com.carlos.fx.codege.service.DatabaseService;
import com.carlos.fx.codege.service.Generator;
import com.carlos.fx.common.controller.BaseController;
import com.carlos.fx.common.util.AsyncTaskUtil;
import com.carlos.fx.common.util.DialogUtil;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Code Generator Controller
 *
 * @author Carlos
 * @since 3.0.0
 */
public class CodeGeneratorController extends BaseController {

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
    private TextField packageField;

    @FXML
    private TextField authorField;

    @FXML
    private TextField outputPathField;

    @FXML
    private Button browseButton;

    @FXML
    private CheckBox generateEntityCheck;

    @FXML
    private CheckBox generateMapperCheck;

    @FXML
    private CheckBox generateServiceCheck;

    @FXML
    private CheckBox generateControllerCheck;

    @FXML
    private Button generateButton;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label statusLabel;

    private List<TableBean> allTables;

    @Override
    protected void initializeComponents() {
        allTables = new ArrayList<>();

        // Initialize database type combo
        databaseTypeCombo.setItems(FXCollections.observableArrayList(DbTypeEnum.values()));
        databaseTypeCombo.getSelectionModel().select(DbTypeEnum.MYSQL);

        // Set default values
        hostField.setText("localhost");
        portField.setText("3306");
        authorField.setText("Carlos");
        packageField.setText("com.carlos.demo");

        // Initialize tree view with checkboxes
        CheckBoxTreeItem<String> rootItem = new CheckBoxTreeItem<>("所有表");
        rootItem.setExpanded(true);
        tableTreeView.setRoot(rootItem);
        tableTreeView.setShowRoot(true);

        // Hide progress bar initially
        progressBar.setVisible(false);
        progressBar.setManaged(false);
        statusLabel.setText("");

        // Set all generation options checked by default
        generateEntityCheck.setSelected(true);
        generateMapperCheck.setSelected(true);
        generateServiceCheck.setSelected(true);
        generateControllerCheck.setSelected(true);
    }

    @Override
    protected void setupEventHandlers() {
        testConnectionButton.setOnAction(e -> testConnection());
        loadTablesButton.setOnAction(e -> loadTables());
        browseButton.setOnAction(e -> browseOutputPath());
        generateButton.setOnAction(e -> generateCode());

        // Enable/disable load tables button based on connection info
        loadTablesButton.disableProperty().bind(
                hostField.textProperty().isEmpty()
                        .or(portField.textProperty().isEmpty())
                        .or(usernameField.textProperty().isEmpty())
                        .or(databaseField.textProperty().isEmpty())
        );

        // Enable/disable generate button
        generateButton.disableProperty().bind(
                packageField.textProperty().isEmpty()
                        .or(outputPathField.textProperty().isEmpty())
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
                DatabaseService service = new DatabaseService(dbInfo, new TemplateConfig());
                // Try to get connection
                service.getTablesDetailInfo();
                return true;
            }
        };

        AsyncTaskUtil.execute(task,
                success -> DialogUtil.showSuccessNotification("连接成功", "数据库连接测试成功！"),
                error -> DialogUtil.showError("连接错误", "数据库连接失败", error)
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

        Task<List<TableBean>> task = new Task<>() {
            @Override
            protected List<TableBean> call() throws Exception {
                DatabaseService service = new DatabaseService(dbInfo, new TemplateConfig());
                return service.getTablesDetailInfo();
            }
        };

        AsyncTaskUtil.execute(task,
                tables -> {
                    allTables = tables;
                    populateTableTree(tables);
                    progressBar.setVisible(false);
                    progressBar.setManaged(false);
                    statusLabel.setText("加载完成，共 " + tables.size() + " 张表");
                    DialogUtil.showSuccessNotification("加载成功", "成功加载 " + tables.size() + " 张表");
                },
                error -> {
                    progressBar.setVisible(false);
                    progressBar.setManaged(false);
                    statusLabel.setText("加载失败");
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

        for (TableBean table : tables) {
            CheckBoxTreeItem<String> tableItem = new CheckBoxTreeItem<>(
                    table.getName() + " (" + table.getComment() + ")"
            );
            rootItem.getChildren().add(tableItem);
        }
    }

    /**
     * Browse output path
     */
    private void browseOutputPath() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("选择输出目录");

        File selectedDir = chooser.showDialog(browseButton.getScene().getWindow());
        if (selectedDir != null) {
            outputPathField.setText(selectedDir.getAbsolutePath());
        }
    }

    /**
     * Generate code
     */
    private void generateCode() {
        // Get selected tables
        List<TableBean> selectedTables = getSelectedTables();
        if (selectedTables.isEmpty()) {
            DialogUtil.showWarning("未选择表", "请至少选择一张表进行代码生成！");
            return;
        }

        // Validate generation options
        if (!generateEntityCheck.isSelected() && !generateMapperCheck.isSelected()
                && !generateServiceCheck.isSelected() && !generateControllerCheck.isSelected()) {
            DialogUtil.showWarning("未选择生成选项", "请至少选择一个代码生成选项！");
            return;
        }

        // Confirm generation
        boolean confirmed = DialogUtil.showConfirm(
                "确认生成",
                "将为 " + selectedTables.size() + " 张表生成代码，是否继续？"
        );

        if (!confirmed) {
            return;
        }

        DatabaseInfo dbInfo = buildDatabaseInfo();
        String packageName = packageField.getText().trim();
        String author = authorField.getText().trim();
        String outputPath = outputPathField.getText().trim();

        progressBar.setVisible(true);
        progressBar.setManaged(true);
        progressBar.setProgress(0);
        statusLabel.setText("正在生成代码...");
        generateButton.setDisable(true);

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                // Create ProjectInfo for code generation
                ProjectInfo projectInfo = new ProjectInfo();
                projectInfo.setGroupId(packageName);
                projectInfo.setArtifactId("generated");
                projectInfo.setPath(outputPath);
                projectInfo.setAuthor(author);

                // Create template config
                TemplateConfig templateConfig = new TemplateConfig();
                projectInfo.setTemplateConfig(templateConfig);

                // Create generator
                Generator generator = new Generator(dbInfo, projectInfo);

                updateMessage("正在生成代码...");
                updateProgress(0, 1);

                // Generate code
                generator.createObject();

                updateProgress(1, 1);
                return null;
            }
        };

        task.messageProperty().addListener((obs, oldVal, newVal) -> statusLabel.setText(newVal));
        task.progressProperty().addListener((obs, oldVal, newVal) -> progressBar.setProgress(newVal.doubleValue()));

        AsyncTaskUtil.execute(task,
                result -> {
                    progressBar.setVisible(false);
                    progressBar.setManaged(false);
                    statusLabel.setText("代码生成完成！");
                    generateButton.setDisable(false);
                    DialogUtil.showSuccessNotification("生成成功", "代码已生成到: " + outputPath);
                },
                error -> {
                    progressBar.setVisible(false);
                    progressBar.setManaged(false);
                    statusLabel.setText("代码生成失败");
                    generateButton.setDisable(false);
                    DialogUtil.showError("生成错误", "代码生成失败", error);
                }
        );
    }

    /**
     * Get selected tables from tree view
     */
    private List<TableBean> getSelectedTables() {
        List<TableBean> selectedTables = new ArrayList<>();
        CheckBoxTreeItem<String> rootItem = (CheckBoxTreeItem<String>) tableTreeView.getRoot();

        for (int i = 0; i < rootItem.getChildren().size(); i++) {
            CheckBoxTreeItem<String> item = (CheckBoxTreeItem<String>) rootItem.getChildren().get(i);
            if (item.isSelected()) {
                selectedTables.add(allTables.get(i));
            }
        }

        return selectedTables;
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
        return dbInfo;
    }
}
