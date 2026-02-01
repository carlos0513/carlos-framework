package com.carlos.tool.gitlab.ui;

import com.carlos.tool.gitlab.config.GitLabServerInfo;
import com.carlos.tool.gitlab.service.GitlabService;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

/**
 * @author carlos
 */
@Setter
public class GitlabToolBar extends JFrame {

    private GitLabServerInfo serverInfo;

    private GitlabService gitlabService = new GitlabService();

    public GitlabToolBar() {
        initComponents();
    }

    public JComponent makeProjectPane() {
        JPanel projectPane = new JPanel();
        JTree projectTree = new JTree();
        JPanel selectProjectPane = new JPanel();
        GroupLayout projectPaneLayout = new GroupLayout(projectPane);
        projectPane.setLayout(projectPaneLayout);
        projectPaneLayout.setHorizontalGroup(
                projectPaneLayout.createParallelGroup()
                        .addGroup(projectPaneLayout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(projectPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(projectTree, GroupLayout.PREFERRED_SIZE, 703, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(selectProjectPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(142, Short.MAX_VALUE))
        );
        projectPaneLayout.setVerticalGroup(
                projectPaneLayout.createParallelGroup()
                        .addGroup(projectPaneLayout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(projectTree, GroupLayout.PREFERRED_SIZE, 329, GroupLayout.PREFERRED_SIZE)
                                .addGap(64, 64, 64)
                                .addComponent(selectProjectPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(430, Short.MAX_VALUE))
        );
        return projectPane;
    }


    private void createUIComponents() {
        JTabbedPane tabbedPane1 = new JTabbedPane();
        tabbedPane1.addTab("项目操作", makeProjectPane());
        tabbedPane1.addTab("用户操作", makeProjectPane());
        this.add(tabbedPane1, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
    }


    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JTabbedPane tabbedPane1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        createUIComponents();


        //======== this ========
        Container contentPane = getContentPane();

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(tabbedPane1, GroupLayout.DEFAULT_SIZE, 871, Short.MAX_VALUE)
                                .addContainerGap())
        );
        contentPaneLayout.setVerticalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addComponent(tabbedPane1, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 38, Short.MAX_VALUE))
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }
}
