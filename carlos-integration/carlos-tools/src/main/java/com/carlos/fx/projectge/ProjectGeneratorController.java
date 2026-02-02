package com.carlos.fx.projectge;

import com.carlos.fx.common.controller.BaseController;
import com.carlos.fx.common.util.AsyncTaskUtil;
import com.carlos.fx.common.util.DialogUtil;
import com.carlos.fx.projectge.service.Generator;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;

import java.io.File;

/**
 * Project Generator Controller
 *
 * @author Carlos
 * @since 3.0.0
 */
public class ProjectGeneratorController extends BaseController {

    @FXML
    private TextField projectNameField;

    @FXML
    private TextField groupIdField;

    @FXML
    private TextField artifactIdField;

    @FXML
    private TextField packageField;

    @FXML
    private TextField versionField;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField authorField;

    @FXML
    private TextField outputPathField;

    @FXML
    private Button browseButton;

    @FXML
    private ComboBox<String> templateCombo;

    @FXML
    private TextArea templateDescArea;

    @FXML
    private Button generateButton;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label statusLabel;

    @Override
    protected void initializeComponents() {
        // Set default values
        groupIdField.setText("com.carlos");
        versionField.setText("1.0.0-SNAPSHOT");
        authorField.setText("Carlos");

        // Initialize template combo
        templateCombo.getItems().addAll(
                "Spring Boot 基础项目",
                "Spring Boot + MyBatis-Plus",
                "Spring Boot + Redis",
                "Spring Boot 微服务",
                "Spring Boot 完整项目"
        );
        templateCombo.getSelectionModel().select(0);

        // Hide progress bar initially
        progressBar.setVisible(false);
        progressBar.setManaged(false);
        statusLabel.setText("");
    }

    @Override
    protected void setupEventHandlers() {
        browseButton.setOnAction(e -> browseOutputPath());
        generateButton.setOnAction(e -> generateProject());

        // Update artifact ID when project name changes
        projectNameField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (artifactIdField.getText().isEmpty() || artifactIdField.getText().equals(oldVal)) {
                artifactIdField.setText(newVal);
            }
        });

        // Update package when group ID or artifact ID changes
        groupIdField.textProperty().addListener((obs, oldVal, newVal) -> updatePackage());
        artifactIdField.textProperty().addListener((obs, oldVal, newVal) -> updatePackage());

        // Update template description
        templateCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            updateTemplateDescription(newVal);
        });

        // Enable/disable generate button
        generateButton.disableProperty().bind(
                projectNameField.textProperty().isEmpty()
                        .or(groupIdField.textProperty().isEmpty())
                        .or(artifactIdField.textProperty().isEmpty())
                        .or(packageField.textProperty().isEmpty())
                        .or(outputPathField.textProperty().isEmpty())
        );
    }

    @Override
    protected void loadData() {
        updateTemplateDescription(templateCombo.getValue());
    }

    /**
     * Update package field
     */
    private void updatePackage() {
        String groupId = groupIdField.getText().trim();
        String artifactId = artifactIdField.getText().trim();

        if (!groupId.isEmpty() && !artifactId.isEmpty()) {
            packageField.setText(groupId + "." + artifactId.replace("-", ""));
        }
    }

    /**
     * Update template description
     */
    private void updateTemplateDescription(String template) {
        if (template == null) {
            return;
        }

        String description = switch (template) {
            case "Spring Boot 基础项目" -> "包含基础的 Spring Boot 项目结构，适合快速开发简单应用。\n\n" +
                    "包含模块:\n" +
                    "- Spring Boot Starter Web\n" +
                    "- Spring Boot Starter Test\n" +
                    "- Lombok\n" +
                    "- 基础配置文件";

            case "Spring Boot + MyBatis-Plus" -> "包含 MyBatis-Plus 数据库访问层，适合开发数据库应用。\n\n" +
                    "包含模块:\n" +
                    "- Spring Boot Starter Web\n" +
                    "- MyBatis-Plus\n" +
                    "- MySQL Driver\n" +
                    "- Druid 连接池\n" +
                    "- 代码生成器配置";

            case "Spring Boot + Redis" -> "包含 Redis 缓存支持，适合需要缓存的应用。\n\n" +
                    "包含模块:\n" +
                    "- Spring Boot Starter Web\n" +
                    "- Spring Boot Starter Data Redis\n" +
                    "- Redisson\n" +
                    "- 缓存配置";

            case "Spring Boot 微服务" -> "包含 Spring Cloud Alibaba 微服务组件。\n\n" +
                    "包含模块:\n" +
                    "- Spring Cloud Gateway\n" +
                    "- Nacos Discovery\n" +
                    "- Nacos Config\n" +
                    "- Sentinel\n" +
                    "- Feign\n" +
                    "- 微服务配置";

            case "Spring Boot 完整项目" -> "包含完整的企业级项目结构和常用组件。\n\n" +
                    "包含模块:\n" +
                    "- Spring Boot Starter Web\n" +
                    "- MyBatis-Plus\n" +
                    "- Redis + Redisson\n" +
                    "- Spring Security\n" +
                    "- JWT 认证\n" +
                    "- Swagger 文档\n" +
                    "- 统一异常处理\n" +
                    "- 统一响应格式\n" +
                    "- 日志配置";

            default -> "请选择项目模板";
        };

        templateDescArea.setText(description);
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
     * Generate project
     */
    private void generateProject() {
        String projectName = projectNameField.getText().trim();
        String groupId = groupIdField.getText().trim();
        String artifactId = artifactIdField.getText().trim();
        String packageName = packageField.getText().trim();
        String version = versionField.getText().trim();
        String description = descriptionField.getText().trim();
        String author = authorField.getText().trim();
        String outputPath = outputPathField.getText().trim();
        String template = templateCombo.getValue();

        // Confirm generation
        boolean confirmed = DialogUtil.showConfirm(
                "确认生成",
                "将在 " + outputPath + " 目录下生成项目 " + projectName + "，是否继续？"
        );

        if (!confirmed) {
            return;
        }

        progressBar.setVisible(true);
        progressBar.setManaged(true);
        progressBar.setProgress(-1); // Indeterminate progress
        statusLabel.setText("正在生成项目...");
        generateButton.setDisable(true);

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                // Create ProjectInfo
                com.carlos.fx.projectge.config.ProjectInfo projectInfo = new com.carlos.fx.projectge.config.ProjectInfo();
                projectInfo.setProjectName(projectName);
                projectInfo.setGroupId(groupId);
                projectInfo.setArtifactId(artifactId);
                projectInfo.setPath(outputPath);
                projectInfo.setAuthor(author);
                projectInfo.setDescribe(description);

                // Set group items (split groupId by dot)
                projectInfo.setGroupItems(java.util.Arrays.asList(groupId.split("\\.")));

                // Create SelectTemplate (simplified - you may need to configure this properly)
                com.carlos.fx.projectge.entity.SelectTemplate selectTemplate = new com.carlos.fx.projectge.entity.SelectTemplate();
                selectTemplate.setName(template);
                // Set template path - this should point to your template directory
                selectTemplate.setPath(System.getProperty("user.dir") + "/templates");
                projectInfo.setSelectTemplate(selectTemplate);

                updateMessage("正在创建项目结构...");

                Generator generator = new Generator(projectInfo);
                generator.createObject();

                updateMessage("项目生成完成！");
                return null;
            }
        };

        task.messageProperty().addListener((obs, oldVal, newVal) -> statusLabel.setText(newVal));

        AsyncTaskUtil.execute(task,
                result -> {
                    progressBar.setVisible(false);
                    progressBar.setManaged(false);
                    statusLabel.setText("项目生成完成！");
                    generateButton.setDisable(false);

                    String projectPath = outputPath + File.separator + projectName;
                    DialogUtil.showSuccessNotification("生成成功", "项目已生成到: " + projectPath);

                    // Ask if user wants to open the project directory
                    boolean openDir = DialogUtil.showConfirm(
                            "打开目录",
                            "是否打开项目目录？"
                    );

                    if (openDir) {
                        try {
                            java.awt.Desktop.getDesktop().open(new File(projectPath));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                error -> {
                    progressBar.setVisible(false);
                    progressBar.setManaged(false);
                    statusLabel.setText("项目生成失败");
                    generateButton.setDisable(false);
                    DialogUtil.showError("生成错误", "项目生成失败", error);
                }
        );
    }
}
