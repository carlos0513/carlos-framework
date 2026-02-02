/*
 * Created by JFormDesigner on Fri Oct 29 15:32:12 CST 2021
 */

package com.carlos.fx.encrypt.ui;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * @author carlos
 */
public class ListContainer extends JFrame {


    private final List<String> items;

    public List<String> getSelect() {
        return select;
    }

    public List<String> select;

    public ListContainer(List<String> items, String title) {
        this.items = items;
        this.setTitle(title);
        initComponents();
    }


    private void okButtonActionPerformed(ActionEvent e) {
        // TODO add your code here
        List<String> selectValue = tableList.getSelectedValuesList();

        if (CollUtil.isEmpty(selectValue)) {
            this.setVisible(false);
            return;
        }
        select = selectValue;
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

        if (CollUtil.isEmpty(items)) {
            JOptionPane.showMessageDialog(null, "当前列表中无可用选项！", "内容异常", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DefaultListModel<String> model = new DefaultListModel<>();
        for (String item : items) {
            if (StrUtil.isNotBlank(item)) {
                model.addElement(item);
            }
        }

        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JPanel();
        scrollPane1 = new JScrollPane();
        tableList = new JList();
        buttonBar = new JPanel();
        label1 = new JLabel();
        okButton = new JButton();

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
                label1.setText("\u53ef\u4f7f\u7528ctrl \u6216 shift \u6309\u952e\u8fdb\u884c\u591a\u9009");
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
