package com.yunjin.tool;

import com.yunjin.tool.ui.ToolHome;

import java.awt.*;

public class ToolsApplication {

    public static void start() {
        EventQueue.invokeLater(() -> {
            new ToolHome().setVisible(true);
        });
    }
}
