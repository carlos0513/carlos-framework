package com.carlos.fx;

import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Carlos工具集JavaFX应用程序入口类
 * <p>
 * 这是整个Carlos开发工具集的主入口类，负责初始化JavaFX应用程序并加载主界面。
 * 该工具集提供了代码生成、项目脚手架、加密工具、GitLab集成等多种开发辅助功能。
 * </p>
 *
 * <p>主要功能：</p>
 * <ul>
 *   <li>初始化JavaFX应用程序环境</li>
 *   <li>设置应用程序主题（使用AtlantaFX的PrimerLight主题）</li>
 *   <li>加载主窗口FXML布局文件</li>
 *   <li>配置窗口大小和样式</li>
 *   <li>提供全局访问主舞台的静态方法</li>
 * </ul>
 *
 * @author Carlos
 * @since 3.0.0
 */
public class ToolsApplication extends Application {

    /**
     * 主舞台（主窗口）的静态引用
     * <p>
     * 保存应用程序的主窗口引用，以便在其他地方需要访问主窗口时使用。
     * 例如：打开对话框时需要指定父窗口。
     * </p>
     */
    private static Stage primaryStage;

    /**
     * JavaFX应用程序启动方法
     * <p>
     * 这是JavaFX应用程序的入口方法，在调用launch()后会自动被调用。
     * 该方法负责初始化应用程序的UI界面和相关配置。
     * </p>
     *
     * @param stage JavaFX提供的主舞台对象
     * @throws IOException 如果加载FXML文件或CSS文件失败
     */
    @Override
    public void start(Stage stage) throws IOException {
        // 保存主舞台引用，供全局使用
        primaryStage = stage;

        // 设置应用程序主题为AtlantaFX的PrimerLight主题
        // PrimerLight是一个现代化的浅色主题，提供清爽的视觉效果
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        // 加载主窗口的FXML布局文件
        // FXML文件定义了主界面的结构和布局
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Parent root = loader.load();

        // 创建场景，设置初始窗口大小为1200x800像素
        Scene scene = new Scene(root, 1200, 800);
        // 加载自定义CSS样式表，用于美化界面
        scene.getStylesheets().add(getClass().getResource("/css/main.css").toExternalForm());

        // 设置窗口标题
        stage.setTitle("研发高效助手");
        // 将场景设置到舞台上
        stage.setScene(scene);
        // 设置窗口最小宽度为1000像素，防止窗口过小导致界面显示异常
        stage.setMinWidth(1000);
        // 设置窗口最小高度为600像素
        stage.setMinHeight(600);
        // 显示窗口
        stage.show();
        stage.setOnCloseRequest(event -> {
            // TODO: Carlos 2026-02-06 页面关闭事件
            System.exit(0);
        });
    }

    /**
     * 获取主舞台（主窗口）的静态方法
     * <p>
     * 提供全局访问主窗口的方式，主要用于：
     * <ul>
     *   <li>打开对话框时指定父窗口</li>
     *   <li>在子窗口中需要访问主窗口</li>
     *   <li>需要在主窗口上显示通知或提示</li>
     * </ul>
     * </p>
     *
     * @return 主舞台对象
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * 应用程序主方法
     * <p>
     * Java应用程序的标准入口方法。
     * 调用JavaFX的launch()方法启动JavaFX应用程序，
     * 该方法会自动调用start()方法来初始化界面。
     * </p>
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        launch(args);
    }
}
