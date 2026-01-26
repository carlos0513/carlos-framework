/*
 * Created by JFormDesigner on Sat Dec 28 00:49:26 CST 2019
 */

package com.yunjin.tool.choose.ui;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelFileUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 *
 * </p>
 *
 * @author Carlos
 * @date 2019/12/28 1:36
 */
@Slf4j
public class ChooseTool extends JFrame {

    public ChooseTool() {
        initComponents();
    }

    /**
     * 打开代码生成路径
     *
     * @author Carlos
     * @date 2019/12/28 1:36
     */
    private void openButtonActionPerformed() {
        textField1.setText(selectPath());
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
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileHidingEnabled(false);
        chooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (!FileUtil.isFile(f)) {
                    return false;
                }
                return ExcelFileUtil.isXlsx(f);
            }

            @Override
            public String getDescription() {
                return null;
            }
        });
        chooser.showDialog(new JLabel(), "选择文件路径");
        File file = chooser.getSelectedFile();
        if (file == null) {
            JOptionPane.showMessageDialog(null, "请选择excel文件！", "请选择文件",
                    JOptionPane.INFORMATION_MESSAGE);
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
        // excel文件路径
        String path = textField1.getText();
        if (StrUtil.isEmpty(path)) {
            JOptionPane.showMessageDialog(null, "请选择excel文件！", "请选择文件",
                    JOptionPane.INFORMATION_MESSAGE);
        }


        String chooseNumStr = textField2.getText();
        if (StrUtil.isEmpty(chooseNumStr)) {
            JOptionPane.showConfirmDialog(null, "请输入数据选择条数", "提示",
                    JOptionPane.INFORMATION_MESSAGE);

        }
        String headerNumStr = textField4.getText();
        if (StrUtil.isEmpty(headerNumStr)) {
            JOptionPane.showConfirmDialog(null, "请输入表头行数？", "提示",
                    JOptionPane.INFORMATION_MESSAGE);
        }


        button2.setEnabled(false);
        button2.setText("正在选择...");

        int chooseNum = Integer.parseInt(chooseNumStr);

        int headerNum = Integer.parseInt(headerNumStr);

        ExcelReader reader = null;
        try {
            reader = ExcelUtil.getReader(path);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "无法识别当前文件，请确认是否选择正确的excel文件！", "文件错误",
                    JOptionPane.INFORMATION_MESSAGE);
            button2.setEnabled(true);
            button2.setText("开始选择");
        }

        // 读取包含头的数据
        List<List<Object>> allData = reader.read();
        int size = allData.size();

        // 选取数据
        Set<Integer> indexs = new HashSet<>();
        do {
            indexs.add(RandomUtil.randomInt(headerNum, size - headerNum));
        } while (indexs.size() < chooseNum);

        List<List<Object>> chooseTableData = new ArrayList<>();
        for (Integer index : indexs) {
            log.info("选取第" + index + "行数据");
            chooseTableData.add(allData.get(index));
        }

        // 处理表头
        String[] columnNames = new String[0];
        if (headerNum <= 0) {

        } else {
            List<Object> header = allData.get(headerNum - 1);
            columnNames = header.stream().map(i -> (String) i).collect(Collectors.toList()).toArray(new String[0]);
        }

        MyTableModel chooseModel = new MyTableModel(chooseTableData, columnNames);
        JTable chooseTable = new JTable(chooseModel);

        MyTableModel dataModel = new MyTableModel(allData, columnNames);
        JTable dataTable = new JTable(dataModel);

        JFrame frame = new JFrame("数据情况");

        // 初始化一个可以滚动的区域
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.add(dataTable);

        scrollPane.add(new JSeparator());
        scrollPane.add(chooseTable);

        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);


        scrollPane.setPreferredSize(new Dimension(1920, 1080));//1.设置最优大小

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //禁止改变大小在调用显示之前
        frame.setResizable(false);
        //2显示JFrame
        frame.setVisible(true);
        //3调用pack()适应子控件大小
        frame.pack();


        try {
            // // 代码生成方法
            // Generator generator = new Generator(projectInfo);
            // generator.createObject();
            // int flag = JOptionPane.showConfirmDialog(null, "创建成功, 是否打开目录？", "提示",
            //         JOptionPane.YES_NO_OPTION);
            // if (flag == 0) {
            //     Runtime.getRuntime().exec("explorer.exe /select, " + finalPath);
            // }
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

        button2.setEnabled(true);
        button2.setText("重新选择");
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
        button2 = new JButton();
        button3 = new JButton();
        label3 = new JLabel();
        textField4 = new JTextField();

        //======== this ========
        setTitle("\u6570\u636e\u9009\u62e9\u5668");
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
        label1.setText("excel\u8def\u5f84\uff1a");
        label1.setHorizontalAlignment(SwingConstants.RIGHT);

        //---- button1 ----
        button1.setText("\u6253    \u5f00");
        button1.addActionListener(e -> openButtonActionPerformed(e));

        //---- label2 ----
        label2.setText("\u9009\u62e9\u6570\u76ee\uff1a");
        label2.setHorizontalAlignment(SwingConstants.RIGHT);

        //---- textField2 ----
        textField2.setText("3");

        //---- button2 ----
        button2.setText("\u5f00\u59cb\u9009\u62e9");
        button2.addActionListener(e -> commitButtonActionPerformed(e));

        //---- button3 ----
        button3.setText("\u5173   \u95ed");
        button3.addActionListener(e -> closeButtonActionPerformed(e));

        //---- label3 ----
        label3.setText("\u8868\u5934\u884c\u6570\uff1a");
        label3.setHorizontalAlignment(SwingConstants.RIGHT);

        //---- textField4 ----
        textField4.setText("1");

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addComponent(label1, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(label3, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addComponent(textField4, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
                                                .addGap(51, 51, 51)
                                                .addComponent(label2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(textField2, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addComponent(textField1, GroupLayout.PREFERRED_SIZE, 243, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(button1)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                                                .addGap(39, 39, 39)
                                                .addComponent(button2)
                                                .addGap(37, 37, 37)
                                                .addComponent(button3, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addContainerGap(35, Short.MAX_VALUE))
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
                                        .addComponent(label3, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(textField4, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(textField2, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(label2, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(button2, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(button3, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
                                .addGap(20, 20, 20))
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
    private JButton button2;
    private JButton button3;
    private JLabel label3;
    private JTextField textField4;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
