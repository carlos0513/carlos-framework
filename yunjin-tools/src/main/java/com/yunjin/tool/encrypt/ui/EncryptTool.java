package com.yunjin.tool.encrypt.ui;

import cn.hutool.core.util.StrUtil;
import cn.hutool.db.ds.simple.SimpleDataSource;
import com.yunjin.tool.encrypt.config.DatabaseInfo;
import com.yunjin.tool.encrypt.enums.DbTypeEnum;
import com.yunjin.tool.encrypt.service.DatabaseService;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

/**
 * <p>
 * 数据库连接界面
 * </p>
 *
 * @author Carlos
 * @date 2019/12/31 17:34
 */
@Slf4j
public class EncryptTool extends JFrame {

    /**
     * 数据库信息
     */
    private DatabaseInfo database;

    private DatabaseService databaseService;

    public EncryptTool() {
        initComponents();
    }

    /**
     * 测试连接
     *
     * @author Carlos
     * @date 2019/12/26 16:21
     */
    private void testButtonActionPerformed() {
        if (StrUtil.isEmpty(this.textField1.getText())) {
            log.error("数据库ip为空！");
            JOptionPane.showMessageDialog(null, "请输入IP", "提示", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (StrUtil.isEmpty(this.textField2.getText())) {
            log.error("数据库用户名为空！");
            JOptionPane.showMessageDialog(null, "请输入用户名", "提示", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (StrUtil.isEmpty(this.textField3.getText())) {
            log.error("数据库密码为空！");
            JOptionPane.showMessageDialog(null, "请输入数据库密码", "提示", JOptionPane.ERROR_MESSAGE);
            return;
        }
        log.info("开始连接数据库...........");
        // 更改按钮连接状态
        this.button1.setText("连接中...");
        // 禁用按钮
        this.button1.setEnabled(false);
        this.button2.setEnabled(false);
        database = new DatabaseInfo();
        try {
            DbTypeEnum dbType = DbTypeEnum.valueOf(String.valueOf(this.comboBox1.getSelectedItem()));
            database.setDbType(dbType);
            database.setIp(this.textField1.getText());
            database.setPort(this.textField2.getText());
            database.setUser(this.textField3.getText());
            database.setPwd(String.valueOf(this.passwordField1.getPassword()));
            database.setUrl(database.buildUrl());

            SimpleDataSource dataSource = database.getDataSource();
            Connection connection = dataSource.getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            database.setMetaData(metaData);
            databaseService = new DatabaseService(database);
            // 获取数据库列表
            List<String> catalogs = databaseService.getSchemas();
            // 给数据库下拉选项添加选项
            this.comboBox2.removeAllItems();
            comboBox2.addItem("--请选择数据库名称--");
            for (String c : catalogs) {
                this.comboBox2.addItem(c);
            }
            log.debug("成功获取数据库表信息");
            JOptionPane.showMessageDialog(null, "连接成功,请选择对应的数据库！", "连接成功提示", JOptionPane.PLAIN_MESSAGE);
            this.button2.setText("下一步");
        } catch (ClassNotFoundException e) {
            log.error("驱动异常", e);
            JOptionPane.showMessageDialog(null, e.getMessage(), "驱动加载错误提示", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            log.error("获取连接异常", e);
            JOptionPane.showMessageDialog(null, "连接数据库失败，请核对连接信息是否正确", "数据库连接失败提示", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            log.error("异常", e);
            JOptionPane.showMessageDialog(null, "发生错误,请检查是否选择数据库", "错误详情请查看error.log", JOptionPane.INFORMATION_MESSAGE);
        }
        this.button1.setEnabled(true);
        this.button2.setEnabled(true);
        this.button1.setText("测试连接");
    }


    private void comboBox2ItemStateChanged(ItemEvent e) {
        if (comboBox2.getSelectedIndex() == 0) {
            return;
        }
        // 当指定的数据库选中时，获取对应的表信息
        if (e.getStateChange() == ItemEvent.SELECTED) {
            database.setDbName((String) comboBox2.getSelectedItem());
            List<String> tables;
            try {
                tables = databaseService.getTables();
                database.setTables(tables);
                for (String table : tables) {
                    comboBox3.addItem(table);
                }
            } catch (Exception ex) {
                log.error("数据库表获取失败！");
                JOptionPane.showMessageDialog(null, "表获取失败", "表获取失败", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void comboBox3ItemStateChanged(ItemEvent e) {
        if (comboBox3.getSelectedIndex() == 0) {
            return;
        }
        // 获取选中的表信息
        if (e.getStateChange() == ItemEvent.SELECTED) {
            database.setSelectTable((String) comboBox3.getSelectedItem());
        }
    }

    /**
     * 跳过 / 下一步
     *
     * @author Carlos
     * @date 2019/12/26 16:22
     */
    private void skipButtonActionPerformed() {
        if (comboBox2.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "请选择数据库", "提示", JOptionPane.ERROR_MESSAGE);
            return;
        }


        if ("下一步".equals(this.button2.getText())) {
            this.button2.setText("运行中...");
            this.button2.setEnabled(false);

        }
        GeneratorFrame generator = new GeneratorFrame(database, this);
        // 显示代码生成窗口
        generator.setVisible(true);
        // 关闭当前窗口
        this.setVisible(false);
    }

    private void thisWindowOpened(WindowEvent event) {
        // 根据以下情况设置窗口相对于指定组件的位置。 (禁止使用)
        this.setLocationRelativeTo(null);
    }


    private void thisWindowClosed(WindowEvent event) {
        dispose();
        System.exit(0);
    }

    private void thisWindowClosing(WindowEvent event) {
        thisWindowClosed(event);
    }

    private void testButtonActionPerformed(ActionEvent e) {
        testButtonActionPerformed();
    }

    private void skipButtonActionPerformed(ActionEvent e) {
        skipButtonActionPerformed();
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        comboBox1 = new JComboBox<>();
        comboBox2 = new JComboBox<>();
        button1 = new JButton();
        button2 = new JButton();
        textField1 = new JTextField();
        textField2 = new JTextField();
        textField3 = new JTextField();
        label1 = new JLabel();
        label2 = new JLabel();
        label3 = new JLabel();
        label4 = new JLabel();
        label5 = new JLabel();
        label6 = new JLabel();
        passwordField1 = new JPasswordField();
        label7 = new JLabel();
        comboBox3 = new JComboBox<>();

        //======== this ========
        setTitle("\u6570\u636e\u5904\u7406\u5de5\u5177");
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

        //---- comboBox1 ----
        comboBox1.setModel(new DefaultComboBoxModel<>(new String[]{
                "--\u8bf7\u9009\u62e9\u6570\u636e\u5e93\u7c7b\u578b--",
                "MYSQL"
        }));
        comboBox1.setSelectedIndex(1);

        //---- comboBox2 ----
        comboBox2.setModel(new DefaultComboBoxModel<>(new String[]{
                "--\u8bf7\u9009\u62e9\u6570\u636e\u5e93\u540d\u79f0--"
        }));
        comboBox2.setSelectedIndex(0);
        comboBox2.addItemListener(e -> comboBox2ItemStateChanged(e));

        //---- button1 ----
        button1.setText("\u83b7\u53d6\u6570\u636e\u5e93");
        button1.addActionListener(e -> testButtonActionPerformed(e));

        //---- button2 ----
        button2.setText("\u8df3   \u8fc7");
        button2.addActionListener(e -> skipButtonActionPerformed(e));

        //---- label1 ----
        label1.setText("\u6570 \u636e \u5e93 \u7c7b \u578b:");
        label1.setLabelFor(comboBox1);
        label1.setHorizontalAlignment(SwingConstants.RIGHT);

        //---- label2 ----
        label2.setText("\u6570 \u636e \u5e93 IP \u5730 \u5740:");
        label2.setLabelFor(textField1);
        label2.setHorizontalAlignment(SwingConstants.RIGHT);

        //---- label3 ----
        label3.setText("\u7aef       \u53e3:");
        label3.setLabelFor(textField2);
        label3.setHorizontalAlignment(SwingConstants.RIGHT);

        //---- label4 ----
        label4.setText("\u7528  \u6237  \u540d:");
        label4.setLabelFor(textField3);
        label4.setHorizontalAlignment(SwingConstants.RIGHT);

        //---- label5 ----
        label5.setText("\u5bc6       \u7801:");
        label5.setHorizontalAlignment(SwingConstants.RIGHT);

        //---- label6 ----
        label6.setText("\u6570  \u636e  \u5e93:");
        label6.setLabelFor(comboBox2);
        label6.setHorizontalAlignment(SwingConstants.RIGHT);

        //---- label7 ----
        label7.setText("\u9009  \u62e9  \u8868:");
        label7.setHorizontalAlignment(SwingConstants.RIGHT);

        //---- comboBox3 ----
        comboBox3.setModel(new DefaultComboBoxModel<>(new String[]{
                "--\u8bf7\u9009\u62e9\u6570\u636e\u8868--"
        }));
        comboBox3.setSelectedIndex(0);
        comboBox3.addItemListener(e -> comboBox3ItemStateChanged(e));

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addGap(121, 121, 121)
                                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                        .addComponent(label2, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(label1, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(label3, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(label4, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(label5, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(label6, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(label7, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE))
                                                .addGap(38, 38, 38)
                                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(textField2, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(textField1, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(passwordField1, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(textField3, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(comboBox1, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(comboBox3, GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)
                                                        .addComponent(comboBox2, GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)))
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addGap(145, 145, 145)
                                                .addComponent(button1, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
                                                .addGap(63, 63, 63)
                                                .addComponent(button2, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(118, Short.MAX_VALUE))
        );
        contentPaneLayout.setVerticalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(label1, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(comboBox1, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(label2, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(textField1, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(label3, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(textField2, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(label4, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(textField3, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(label5, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(passwordField1, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(label6, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(comboBox2, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(label7, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(comboBox3, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(button1, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(button2, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE))
                                .addGap(21, 21, 21))
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JComboBox<String> comboBox1;
    private JComboBox<String> comboBox2;
    private JButton button1;
    private JButton button2;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    private JLabel label6;
    private JPasswordField passwordField1;
    private JLabel label7;
    private JComboBox<String> comboBox3;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

}
