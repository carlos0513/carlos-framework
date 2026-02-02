package com.carlos.fx;

import com.carlos.fx.common.controller.BaseController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;

/**
 * Main Window Controller
 *
 * @author Carlos
 * @since 3.0.0
 */
public class MainController extends BaseController {

    @FXML
    private ListView<ToolItem> toolListView;

    @FXML
    private StackPane contentPane;

    @Override
    protected void initializeComponents() {
        // Setup tool list
        toolListView.getItems().addAll(
                new ToolItem("代码生成器", "Code Generator", FontAwesomeSolid.CODE, "/fxml/codegenerator.fxml"),
                new ToolItem("项目脚手架", "Project Scaffold", FontAwesomeSolid.PROJECT_DIAGRAM, "/fxml/projectgenerator.fxml"),
                new ToolItem("加密工具", "Encrypt Tool", FontAwesomeSolid.LOCK, "/fxml/encrypttool.fxml"),
                new ToolItem("GitLab工具", "GitLab Tools", FontAwesomeSolid.CODE_BRANCH, "/fxml/gitlabmain.fxml"),
                new ToolItem("设置", "Settings", FontAwesomeSolid.COG, null)
        );

        // Custom cell factory with icons
        toolListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(ToolItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    VBox vbox = new VBox(5);
                    FontIcon icon = new FontIcon(item.getIcon());
                    icon.setIconSize(24);
                    Label nameLabel = new Label(item.getName());
                    nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
                    Label descLabel = new Label(item.getDescription());
                    descLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #757575;");

                    vbox.getChildren().addAll(icon, nameLabel, descLabel);
                    setGraphic(vbox);
                }
            }
        });

        // Select first item by default
        if (!toolListView.getItems().isEmpty()) {
            toolListView.getSelectionModel().select(0);
        }
    }

    @Override
    protected void setupEventHandlers() {
        // Handle tool selection
        toolListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadTool(newVal);
            }
        });
    }

    @Override
    protected void loadData() {
        // Load first tool
        ToolItem firstItem = toolListView.getSelectionModel().getSelectedItem();
        if (firstItem != null) {
            loadTool(firstItem);
        }
    }

    /**
     * Load tool content
     */
    private void loadTool(ToolItem toolItem) {
        if (toolItem.getFxmlPath() == null) {
            // Settings or other tools without FXML
            contentPane.getChildren().clear();
            Label label = new Label("功能开发中...");
            label.setStyle("-fx-font-size: 24px; -fx-text-fill: #757575;");
            contentPane.getChildren().add(label);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(toolItem.getFxmlPath()));
            contentPane.getChildren().clear();
            contentPane.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
            Label errorLabel = new Label("加载失败: " + e.getMessage());
            errorLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #f44336;");
            contentPane.getChildren().clear();
            contentPane.getChildren().add(errorLabel);
        }
    }

    /**
     * Tool Item Model
     */
    public static class ToolItem {
        private final String name;
        private final String description;
        private final FontAwesomeSolid icon;
        private final String fxmlPath;

        public ToolItem(String name, String description, FontAwesomeSolid icon, String fxmlPath) {
            this.name = name;
            this.description = description;
            this.icon = icon;
            this.fxmlPath = fxmlPath;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public FontAwesomeSolid getIcon() {
            return icon;
        }

        public String getFxmlPath() {
            return fxmlPath;
        }
    }
}
