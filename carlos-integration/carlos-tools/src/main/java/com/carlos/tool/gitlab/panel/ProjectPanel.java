/*
 * Created by JFormDesigner on Mon May 06 11:44:43 CST 2024
 */

package com.carlos.tool.gitlab.panel;

import javax.swing.*;

/**
 * @author carlos
 */
public class ProjectPanel extends JPanel {
    public ProjectPanel() {
        initComponents();
    }

    private void createUIComponents() {
        // TODO: add custom component creation code here
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        createUIComponents();

        projectTree = new JTree();
        JCheckBox check = new JCheckBox();
        JTextField projectName = new JTextField();
        JComboBox sourceBranch = new JComboBox();
        JComboBox targetBranch = new JComboBox();

        //======== this ========

        //======== selectProjectPane ========
        {

            //======== selectProjectItem ========
            {

                GroupLayout selectProjectItemLayout = new GroupLayout(selectProjectItem);
                selectProjectItem.setLayout(selectProjectItemLayout);
                selectProjectItemLayout.setHorizontalGroup(
                        selectProjectItemLayout.createParallelGroup()
                                .addGroup(selectProjectItemLayout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(check)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(projectName, GroupLayout.PREFERRED_SIZE, 183, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(sourceBranch, GroupLayout.PREFERRED_SIZE, 203, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(targetBranch, GroupLayout.PREFERRED_SIZE, 203, GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
                selectProjectItemLayout.setVerticalGroup(
                        selectProjectItemLayout.createParallelGroup()
                                .addGroup(selectProjectItemLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(selectProjectItemLayout.createParallelGroup()
                                                .addGroup(selectProjectItemLayout.createSequentialGroup()
                                                        .addGroup(selectProjectItemLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                .addComponent(projectName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(sourceBranch, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(targetBranch, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                        .addGap(0, 0, Short.MAX_VALUE))
                                                .addComponent(check, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addContainerGap())
                );
            }

            GroupLayout selectProjectPaneLayout = new GroupLayout(selectProjectPane);
            selectProjectPane.setLayout(selectProjectPaneLayout);
            selectProjectPaneLayout.setHorizontalGroup(
                    selectProjectPaneLayout.createParallelGroup()
                            .addGroup(selectProjectPaneLayout.createSequentialGroup()
                                    .addGap(15, 15, 15)
                                    .addComponent(selectProjectItem, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addContainerGap(215, Short.MAX_VALUE))
            );
            selectProjectPaneLayout.setVerticalGroup(
                    selectProjectPaneLayout.createParallelGroup()
                            .addGroup(selectProjectPaneLayout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(selectProjectItem, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addContainerGap(251, Short.MAX_VALUE))
            );
        }

        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup()
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup()
                                        .addComponent(projectTree, GroupLayout.DEFAULT_SIZE, 883, Short.MAX_VALUE)
                                        .addComponent(selectProjectPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup()
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(projectTree, GroupLayout.PREFERRED_SIZE, 272, GroupLayout.PREFERRED_SIZE)
                                .addGap(39, 39, 39)
                                .addComponent(selectProjectPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(28, Short.MAX_VALUE))
        );
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JTree projectTree;
    private JPanel selectProjectPane;
    private JPanel selectProjectItem;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
