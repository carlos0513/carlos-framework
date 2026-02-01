/*
 * Created by JFormDesigner on Fri Oct 29 15:32:12 CST 2021
 */

package com.carlos.tool.codege.ui;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.carlos.tool.codege.config.DatabaseInfo;
import com.carlos.tool.codege.entity.TableInfo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;

/**
 * @author carlos
 */
public class TableList extends JFrame {


    private final DatabaseInfo database;

    public TableList(DatabaseInfo database) {
        this.database = database;
        this.setTitle(database.getDbName() + "  表列表");
        initComponents();

    }


    private void okButtonActionPerformed(ActionEvent e) {
        // TODO add your code here
        int[] selectedIndices = tableList.getSelectedIndices();

        if (ArrayUtil.isEmpty(selectedIndices)) {
            this.setVisible(false);
            return;
        }
        List<TableInfo> selectTables = new LinkedList<>();
        for (int selectedIndex : selectedIndices) {
            selectTables.add(database.getTables().get(selectedIndex));
        }
        database.setTables(selectTables);
        this.setVisible(false);
    }


    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JScrollPane scrollPane1;
    private JList tableList;
    private JPanel buttonBar;
    private JLabel label1;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    private void initComponents() {
        List<TableInfo> tables = database.getTables();
        if (CollUtil.isEmpty(tables)) {
            JOptionPane.showMessageDialog(null, "当前数据库中没有表！", "表获取失败", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DefaultListModel<String> model = new DefaultListModel<>();
        for (TableInfo table : tables) {
            if (StrUtil.isNotBlank(table.getComment())) {
                model.addElement(table.getName() + "[" + table.getComment() + "]");
            } else {
                model.addElement(table.getName() + "                                                                 " +
                        "                                                         ");
            }
        }

        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JPanel();
        scrollPane1 = new JScrollPane();
        tableList = new JList();
        buttonBar = new JPanel();
        label1 = new JLabel();
        okButton = new JButton();
        tableList.setCellRenderer(new TableListRender());
        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== scrollPane1 ========
            {

                //---- tableList ----
                tableList.setVisibleRowCount(20);
                tableList.setModel(model);
                scrollPane1.setViewportView(tableList);
            }
            dialogPane.add(scrollPane1, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout) buttonBar.getLayout()).columnWidths = new int[]{0, 80};
                ((GridBagLayout) buttonBar.getLayout()).columnWeights = new double[]{1.0, 0.0};

                //---- label1 ----
                label1.setText("\u9ed8\u8ba4\u5168\u9009\uff0c \u53ef\u4f7f\u7528ctrl \u6216 shift \u6309\u952e\u8fdb\u884c\u591a\u9009");
                label1.setForeground(new Color(204, 0, 0));
                buttonBar.add(label1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 5), 0, 0));

                //---- okButton ----
                okButton.setText("\u786e  \u5b9a");
                okButton.addActionListener(e -> okButtonActionPerformed(e));
                buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }


}
