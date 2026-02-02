package com.carlos.fx.gitlab.ui;


import javax.swing.*;

public class ComponentManager {

    public JPanel newProjectItemPanel() {
        JPanel selectProjectItem = new JPanel();
        JCheckBox check = new JCheckBox();
        JTextField projectName = new JTextField();
        JComboBox sourceBranch = new JComboBox();
        JComboBox targetBranch = new JComboBox();
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
        return selectProjectItem;
    }


}
