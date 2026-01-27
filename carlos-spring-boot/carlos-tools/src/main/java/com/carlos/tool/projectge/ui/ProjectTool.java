/*
 * Created by JFormDesigner on Sat Dec 28 00:49:26 CST 2019
 */

package com.carlos.tool.projectge.ui;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import com.carlos.tool.projectge.config.ProjectInfo;
import com.carlos.tool.projectge.entity.SelectTemplate;
import com.carlos.tool.projectge.service.Generator;
import com.carlos.tool.projectge.utils.TemplateUtils;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
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
public class ProjectTool extends JFrame {


    private List<SelectTemplate> templateInfos;


    public ProjectTool() {
        initComponents();
    }

    /**
     * 打开代码生成路径
     *
     * @author Carlos
     * @date 2019/12/28 1:36
     */
    private void openButtonActionPerformed() {
        this.textField1.setText(selectPath());
    }


    /**
     * 路径选择弹窗（点击选择结构文档路径和代码路径时弹出弹框）
     *
     * @return String 如果选择了文件路径，返回文件的路径否则返回原路径
     * @author Carlos
     * @date 2019/5/8 13:41
     */
    private String selectPath() {
        JFileChooser chooser = new JFileChooser();
        // 设置只允许选择目录
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.showDialog(new JLabel(), "选择保存路径");
        File file = chooser.getSelectedFile();
        if (file == null) {
            return this.textField1.getText();
        }
        return file.getPath();
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
        String groupId = textField3.getText();
        if (StrUtil.isEmpty(groupId)) {
            JOptionPane.showMessageDialog(null, "GroupId不能为空！", "请完善项目信息",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        if (comboBox2.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "请选择模板！", "请选择模板",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        String artifactId = textField2.getText();
        if (StrUtil.isEmpty(artifactId)) {
            int option = JOptionPane.showConfirmDialog(null, "是否使用项目名作为ArtifactId？", "提示",
                    JOptionPane.YES_NO_OPTION);
            if (option == 0) {
                artifactId = textField2.getText();
                textField4.setText(artifactId);
            }
        }
        String projectName = textField4.getText();
        if (StrUtil.isEmpty(projectName)) {
            int option = JOptionPane.showConfirmDialog(null, "是否使用ArtifactId作为项目名？", "提示",
                    JOptionPane.YES_NO_OPTION);
            if (option == 0) {
                projectName = textField2.getText();
                textField4.setText(projectName);
            }
        }
        // 代码保存路径
        String path = textField1.getText();
        if (StrUtil.isEmpty(path)) {

            path = ResourceUtil.getResource("/", this.getClass()).getPath();
        }
        // path = path + File.separator + projectName;

        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.setProjectName(projectName);
        projectInfo.setDescribe(textField5.getText());
        projectInfo.setGroupId(groupId);
        projectInfo.setGroupItems(StrUtil.split(groupId, CharUtil.DOT));
        projectInfo.setArtifactId(artifactId);
        projectInfo.setPath(path);
        projectInfo.setAuthor(textField6.getText());
        projectInfo.setEmail(textField7.getText());
        // 获取当前选择的模板
        SelectTemplate selectTemplate = templateInfos.get(comboBox2.getSelectedIndex() - 1);
        TemplateUtils.loadTemplateConfig(selectTemplate);
        projectInfo.setSelectTemplate(selectTemplate);
        this.button2.setEnabled(false);
        this.button2.setText("结构生成中...");
        String finalPath = path;
        try {
            // 代码生成方法
            Generator generator = new Generator(projectInfo);
            generator.createObject();
            int flag = JOptionPane.showConfirmDialog(null, "创建成功, 是否打开目录？", "提示",
                    JOptionPane.YES_NO_OPTION);
            if (flag == 0) {
                Runtime.getRuntime().exec("explorer.exe /select, " + finalPath);
            }
        } catch (Exception exception) {
            log.error("生成失败！", exception);

            int flag = JOptionPane.showConfirmDialog(null, "生成失败, 是否打开日志？", "提示",
                    JOptionPane.YES_NO_OPTION);
            if (flag == 0) {
                try {
                    Runtime.getRuntime().exec("notepad" + " " + System.getProperty("user.dir") + File.separator + "logs" +
                            File.separator + "application.log");
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "文件打开失败:" + e.getMessage(), "文件打开失败",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        this.button2.setEnabled(true);
        this.button2.setText("生成工程");
    }

    private void thisWindowClosed(WindowEvent e) {
        dispose();
        System.exit(0);
    }

    private void thisWindowClosing(WindowEvent e) {
        thisWindowClosed(e);
    }

    private void thisWindowOpened(WindowEvent e) {
        textField1.setText(new File("").getAbsolutePath());
        templateInfos = TemplateUtils.getTemplatesBaseInfo();
        this.comboBox2.removeAllItems();
        comboBox2.addItem("--请选择结构模板--");
        // 给数据库下拉选项添加选项
        templateInfos.forEach(templateInfo -> this.comboBox2.addItem(templateInfo.getName() + "(" + templateInfo.getDescribe() + ")"));
    }

    private void openButtonActionPerformed(ActionEvent e) {
        openButtonActionPerformed();
    }

    private void commitButtonActionPerformed(ActionEvent e) {
        commitButtonActionPerformed();
    }

    private void closeButtonActionPerformed(ActionEvent e) {
        closeButtonActionPerformed();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        label1 = new JLabel();
        textField1 = new JTextField();
        button1 = new JButton();
        label2 = new JLabel();
        textField2 = new JTextField();
        textField3 = new JTextField();
        label4 = new JLabel();
        label5 = new JLabel();
        textField5 = new JTextField();
        label6 = new JLabel();
        textField6 = new JTextField();
        textField7 = new JTextField();
        label7 = new JLabel();
        button2 = new JButton();
        button3 = new JButton();
        label10 = new JLabel();
        comboBox2 = new JComboBox();
        label3 = new JLabel();
        textField4 = new JTextField();

        //======== this ========
        setTitle("Maven\u5de5\u7a0b\u811a\u624b\u67b6");
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

        //---- label1 ----
        label1.setText("\u4fdd\u5b58\u8def\u5f84\uff1a");
        label1.setHorizontalAlignment(SwingConstants.RIGHT);

        //---- button1 ----
        button1.setText("\u6253    \u5f00");
        button1.addActionListener(e -> openButtonActionPerformed(e));

        //---- label2 ----
        label2.setText("ArtifactId\uff1a");
        label2.setHorizontalAlignment(SwingConstants.RIGHT);

        //---- textField2 ----
        textField2.setText("yunjin-test");

        //---- textField3 ----
        textField3.setText("com.carlos.test");

        //---- label4 ----
        label4.setText("GroupId\uff1a");
        label4.setHorizontalAlignment(SwingConstants.RIGHT);

        //---- label5 ----
        label5.setText("\u63cf      \u8ff0\uff1a");
        label5.setHorizontalAlignment(SwingConstants.RIGHT);

        //---- textField5 ----
        textField5.setText("\u6a21\u5757\u63cf\u8ff0");

        //---- label6 ----
        label6.setText("\u4f5c      \u8005\uff1a");
        label6.setHorizontalAlignment(SwingConstants.RIGHT);

        //---- textField6 ----
        textField6.setText("carlos");

        //---- textField7 ----
        textField7.setText("yunjin@gmail.com");

        //---- label7 ----
        label7.setText("Email\uff1a");
        label7.setHorizontalAlignment(SwingConstants.RIGHT);

        //---- button2 ----
        button2.setText("\u751f\u6210\u5de5\u7a0b");
        button2.addActionListener(e -> commitButtonActionPerformed(e));

        //---- button3 ----
        button3.setText("\u5173   \u95ed");
        button3.addActionListener(e -> closeButtonActionPerformed(e));

        //---- label10 ----
        label10.setText("\u4ee3\u7801\u7ed3\u6784\uff1a");
        label10.setHorizontalAlignment(SwingConstants.RIGHT);

        //---- label3 ----
        label3.setText("\u9879  \u76ee  \u540d\uff1a");
        label3.setHorizontalAlignment(SwingConstants.RIGHT);

        //---- textField4 ----
        textField4.setText("\u6a21\u5757\u4e2d\u6587\u540d\u79f0");

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addContainerGap(41, Short.MAX_VALUE)
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addGroup(GroupLayout.Alignment.TRAILING,
                                                contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(label1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(label5, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                                .addGap(6, 6, 6)
                                                                .addComponent(label6, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
                                                                        Short.MAX_VALUE))
                                                        .addComponent(label2, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addComponent(label3, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 84,
                                                GroupLayout.PREFERRED_SIZE))
                                .addGap(30, 30, 30)
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                                                        .addComponent(textField1, GroupLayout.PREFERRED_SIZE, 371, GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(button1))
                                                .addGroup(contentPaneLayout.createSequentialGroup()
                                                        .addComponent(textField4, GroupLayout.PREFERRED_SIZE, 190, GroupLayout.PREFERRED_SIZE)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(label10)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(comboBox2))
                                                .addComponent(textField5))
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addGroup(contentPaneLayout.createParallelGroup()
                                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                                .addComponent(textField6, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(label7, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                                .addGap(33, 33, 33)
                                                                .addComponent(button2, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(contentPaneLayout.createParallelGroup()
                                                        .addComponent(button3, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(textField7, GroupLayout.PREFERRED_SIZE, 221, GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addComponent(textField2, GroupLayout.PREFERRED_SIZE, 190, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(label4, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(textField3, GroupLayout.PREFERRED_SIZE, 190, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(79, Short.MAX_VALUE))
        );
        contentPaneLayout.setVerticalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(label1, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(textField1, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(button1, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(label10, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(comboBox2, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(textField4, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(label3, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(textField2, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(label2, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(label4, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(textField3, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(label5, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(textField5, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(label6, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(textField6, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(label7, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(textField7, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(button2, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(button3, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(26, Short.MAX_VALUE))
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel label1;
    private JTextField textField1;
    private JButton button1;
    private JLabel label2;
    private JTextField textField2;
    private JTextField textField3;
    private JLabel label4;
    private JLabel label5;
    private JTextField textField5;
    private JLabel label6;
    private JTextField textField6;
    private JTextField textField7;
    private JLabel label7;
    private JButton button2;
    private JButton button3;
    private JLabel label10;
    private JComboBox comboBox2;
    private JLabel label3;
    private JTextField textField4;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
