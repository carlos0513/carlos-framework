package com.carlos.tool.codege.ui;


import javax.swing.*;
import java.awt.*;

/**
 * <p>
 * 设置每行渲染效果
 * </p>
 *
 * @author Carlos
 * @date 2021/11/1 12:00
 */
public class TableListRender extends DefaultListCellRenderer {


    public TableListRender() {
        super();
    }


    @Override
    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        return this;
    }
}