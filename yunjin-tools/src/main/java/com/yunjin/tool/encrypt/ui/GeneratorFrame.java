/*
 * Created by JFormDesigner on Sat Dec 28 00:49:26 CST 2019
 */

package com.yunjin.tool.encrypt.ui;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import com.yunjin.tool.encrypt.config.DatabaseInfo;
import com.yunjin.tool.encrypt.config.ToolInfo;
import com.yunjin.tool.encrypt.config.ToolInfo.Encrypt;
import com.yunjin.tool.encrypt.enums.ToolType;
import com.yunjin.tool.encrypt.service.DatabaseService;
import com.yunjin.tool.encrypt.service.Executor;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author Carlos
 * @date 2019/12/28 1:36
 */
@Slf4j
public class GeneratorFrame extends JFrame {

    private DatabaseInfo database;
    private DatabaseService databaseService;


    // TODO: Carlos 2021/11/22  上一步功能待开发
    private EncryptTool encryptTool;

    public GeneratorFrame(DatabaseInfo database, EncryptTool encryptTool) {
        initComponents();
        this.database = database;
        this.encryptTool = encryptTool;
        this.databaseService = new DatabaseService(database);
    }


    /**
     * 关闭窗口
     *
     * @author Carlos
     * @date 2019/12/28 1:46
     */
    private void closeButtonActionPerformed() {
        System.exit(0);
    }

    /**
     * 提交表单
     *
     * @author Carlos
     * @date 2019/12/28 17:02
     */
    private void commitButtonActionPerformed() {
        String targetTable = textField3.getText();
        if (StrUtil.isEmpty(targetTable)) {
            JOptionPane.showMessageDialog(null, "目标表名不能为空！", "请完善信息", JOptionPane.INFORMATION_MESSAGE);
        }

        int toolType = comboBox2.getSelectedIndex();

        if (toolType == 0) {
            JOptionPane.showMessageDialog(null, "请选择工具类型！", "请选择工具类型", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String iv = textField4.getText();
        String key = textField2.getText();
        if (toolType == 1 || toolType == 2) {
            if (StrUtil.isBlank(key)) {
                JOptionPane.showConfirmDialog(null, "key不能为空？", "提示", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (StrUtil.isBlank(iv)) {
                JOptionPane.showConfirmDialog(null, "IV不能为空？", "提示", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        ToolInfo toolInfo = new ToolInfo();
        toolInfo.setSourceTable(textField8.getText());
        toolInfo.setTargetTable(targetTable);
        ToolType[] values = ToolType.values();
        toolInfo.setToolType(values[toolType - 1]);
        toolInfo.setEncrypt(new Encrypt(key, iv));

        if (listContainer != null) {
            List<String> select = listContainer.getSelect();
            toolInfo.setSelectFields(select);
        }

        this.button2.setEnabled(false);
        this.button2.setText("处理中...");
        try {
            // 代码生成方法
            Executor executor = new Executor(database, toolInfo);
            // 启动定时器，定时获取进度
            createTimer(executor);
            executor.execute();
            CronUtil.stop();
            JOptionPane.showConfirmDialog(null, "处理成功？", "提示", JOptionPane.CANCEL_OPTION);

        } catch (Exception exception) {
            log.error("数据处理失败！", exception);

            int flag = JOptionPane.showConfirmDialog(null, "处理失败, 是否打开日志？", "提示", JOptionPane.YES_NO_OPTION);
            if (flag == 0) {
                try {
                    Runtime.getRuntime().exec(
                            "notepad" + " " + System.getProperty("user.dir") + File.separator + "logs" + File.separator + "application.log");
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "文件打开失败:" + e.getMessage(), "文件打开失败", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        this.button2.setEnabled(true);
        this.button2.setText("开始执行");
    }

    private void createTimer(Executor executor) {
        CronUtil.schedule("*/2 * * * * *", new Task() {
            @Override
            public void execute() {
                String process = executor.getProcess();
                textField5.setText(process);
            }
        });

        // 支持秒级别定时任务
        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }

    private void thisWindowClosed(WindowEvent e) {
        dispose();
        System.exit(0);
    }

    private void thisWindowClosing(WindowEvent e) {
        thisWindowClosed(e);
    }

    private void thisWindowOpened(WindowEvent e) {
        textField8.setText(database.getSelectTable());
        textField8.setEnabled(false);
        textField8.setEditable(false);
        textField3.setText(database.getSelectTable() + StrUtil.UNDERLINE + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN));
        this.comboBox2.removeAllItems();
        comboBox2.addItem("--请选择操作类型--");
        ToolType[] values = ToolType.values();
        for (ToolType value : values) {
            this.comboBox2.addItem(value.getDesc());
        }
    }

    private ListContainer listContainer;

    private void comboBox2ItemStateChanged(ItemEvent e) {
        int selectedIndex = comboBox2.getSelectedIndex();
        if (selectedIndex == 0) {
            return;
        }

        // 当指定的数据库选中时，获取对应的表信息
        if (e.getStateChange() != ItemEvent.SELECTED) {
            return;
        }

        ToolType[] values = ToolType.values();
        ToolType toolType = values[selectedIndex - 1];
        switch (toolType) {
            case SM4_ENCRYPT:
                // 获取表字段
                if (listContainer != null) {
                    // 防止重复打开多个窗口
                    listContainer.dispose();
                }
                listContainer = new ListContainer(databaseService.getColumns(database.getSelectTable()), "请选择加密字段");
                listContainer.setVisible(true);
                break;
            case SM4_DECRYPT:
                // 获取表字段
                if (listContainer != null) {
                    // 防止重复打开多个窗口
                    listContainer.dispose();
                }
                listContainer = new ListContainer(databaseService.getColumns(database.getSelectTable()), "请选择解密字段");
                listContainer.setVisible(true);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + toolType);
        }

    }

    private void commitButtonActionPerformed(ActionEvent e) {
        commitButtonActionPerformed();
    }

    private void closeButtonActionPerformed(ActionEvent e) {
        closeButtonActionPerformed();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        label2 = new JLabel();
        textField2 = new JTextField();
        label3 = new JLabel();
        textField3 = new JTextField();
        label4 = new JLabel();
        textField4 = new JTextField();
        button2 = new JButton();
        button3 = new JButton();
        label10 = new JLabel();
        label11 = new JLabel();
        textField8 = new JTextField();
        label5 = new JLabel();
        textField5 = new JTextField();
        comboBox2 = new JComboBox<>();

        //======== this ========
        setTitle("\u6570\u636e\u5e93\u5904\u7406\u5de5\u5177");
        setResizable(false);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                thisWindowClosed(e);
            }

            @Override
            public void windowClosing(WindowEvent e) {
                thisWindowClosing(e);
            }

            @Override
            public void windowOpened(WindowEvent e) {
                thisWindowOpened(e);
            }
        });
        Container contentPane = getContentPane();

        //---- label2 ----
        label2.setText("key\uff1a");
        label2.setHorizontalAlignment(SwingConstants.RIGHT);

        //---- label3 ----
        label3.setText("iv\uff1a");
        label3.setHorizontalAlignment(SwingConstants.RIGHT);

        //---- label4 ----
        label4.setText("\u76ee\u6807\u8868\u540d\uff1a");
        label4.setHorizontalAlignment(SwingConstants.RIGHT);

        //---- button2 ----
        button2.setText("\u5f00\u59cb\u6267\u884c");
        button2.addActionListener(e -> commitButtonActionPerformed(e));

        //---- button3 ----
        button3.setText("\u5173   \u95ed");
        button3.addActionListener(e -> closeButtonActionPerformed(e));

        //---- label10 ----
        label10.setText("\u5de5\u5177\u7c7b\u578b:");
        label10.setHorizontalAlignment(SwingConstants.RIGHT);

        //---- label11 ----
        label11.setText("\u64cd\u4f5c\u8868\u540d:");
        label11.setHorizontalAlignment(SwingConstants.RIGHT);

        //---- label5 ----
        label5.setText("\u5f53\u524d\u8fdb\u5ea6\uff1a");
        label5.setHorizontalAlignment(SwingConstants.RIGHT);

        //---- comboBox2 ----
        comboBox2.setModel(new DefaultComboBoxModel<>(new String[]{
                "\u8bf7\u9009\u62e9\u5de5\u5177\u7c7b\u578b"
        }));
        comboBox2.setSelectedIndex(0);
        comboBox2.addItemListener(e -> comboBox2ItemStateChanged(e));

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addGap(71, 71, 71)
                                                .addComponent(label11, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
                                                .addGap(6, 6, 6)
                                                .addComponent(textField8, GroupLayout.PREFERRED_SIZE, 190, GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(label4)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(textField3))
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addContainerGap(105, Short.MAX_VALUE)
                                                .addComponent(label2, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)
                                                .addGap(86, 86, 86)
                                                .addComponent(button2, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)
                                                .addGap(127, 127, 127)
                                                .addComponent(button3, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                                                .addGap(101, 101, 101))
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addGap(84, 84, 84)
                                                .addComponent(label5, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(textField5, GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)))
                                .addGap(44, 44, 44))
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addGap(103, 103, 103)
                                .addComponent(label10)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addComponent(textField2, GroupLayout.PREFERRED_SIZE, 190, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(label3, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(textField4, GroupLayout.PREFERRED_SIZE, 190, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(comboBox2, GroupLayout.PREFERRED_SIZE, 211, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(95, Short.MAX_VALUE))
        );
        contentPaneLayout.setVerticalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(label11, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(textField8, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(label4, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(textField3, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addComponent(label10, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                                .addGap(9, 9, 9))
                                        .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                                                .addComponent(comboBox2, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)))
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(textField2, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(label2, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(label3, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(textField4, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 148, Short.MAX_VALUE)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(label5, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(textField5, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(button2, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(button3, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
                                .addGap(22, 22, 22))
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel label2;
    private JTextField textField2;
    private JLabel label3;
    private JTextField textField3;
    private JLabel label4;
    private JTextField textField4;
    private JButton button2;
    private JButton button3;
    private JLabel label10;
    private JLabel label11;
    private JTextField textField8;
    private JLabel label5;
    private JTextField textField5;
    private JComboBox<String> comboBox2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
