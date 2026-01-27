package com.carlos.tool;

import com.carlos.tool.ui.ToolHome;

import java.awt.*;

public class ToolsApplication {

    public static void start() {
        EventQueue.invokeLater(() -> {
            new ToolHome().setVisible(true);
        });
    }
}
