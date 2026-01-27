/*
 * Created by JFormDesigner on Mon Apr 29 16:30:19 CST 2024
 */

package com.carlos.tool.gitlab.ui;

import com.carlos.tool.gitlab.config.GitLabServerInfo;
import com.carlos.tool.gitlab.service.GitlabService;
import lombok.Setter;
import org.gitlab4j.api.models.Group;
import org.gitlab4j.api.models.Project;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

/**
 * @author carlos
 */
@Setter
public class CreateMergeRequest extends JFrame {

    private GitLabServerInfo serverInfo;
    private GitlabService gitlabService = new GitlabService();

    public CreateMergeRequest() {
        initComponents();
    }

    private void thisWindowClosed(WindowEvent e) {
        // TODO add your code here
    }

    private void thisWindowClosing(WindowEvent e) {
        // TODO add your code here
    }

    private void thisWindowOpened(WindowEvent e) {
        List<Group> groups = gitlabService.loadProject(serverInfo);
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("请选择项目");

        for (Group group : groups) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(group.getFullName());
            List<Project> projects = group.getProjects();
            for (Project project : projects) {
                node.add(new DefaultMutableTreeNode(project.getName()));
            }
            top.add(node);
        }
        projectTree.setModel(new DefaultTreeModel(top));
    }

    private void closeButtonActionPerformed(ActionEvent e) {
        // TODO add your code here
    }


    private void newServerButtonActionPerformed(ActionEvent e) {
        // TODO add your code here
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        cancelBotton = new JButton();
        serverBotton = new JButton();
        scrollPane1 = new JScrollPane();
        projectTree = new JTree();

        //======== this ========
        setTitle("\u521b\u5efa\u5408\u5e76\u8bf7\u6c42");
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

        //---- cancelBotton ----
        cancelBotton.setText("\u5173   \u95ed");
        cancelBotton.addActionListener(e -> closeButtonActionPerformed(e));

        //---- serverBotton ----
        serverBotton.setText("\u65b0\u589e\u670d\u52a1");
        serverBotton.addActionListener(e -> newServerButtonActionPerformed(e));

        //======== scrollPane1 ========
        {
            scrollPane1.setViewportView(projectTree);
        }

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addGap(179, 179, 179)
                                                .addComponent(serverBotton)
                                                .addGap(99, 99, 99)
                                                .addComponent(cancelBotton, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addGap(50, 50, 50)
                                                .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 715, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(53, Short.MAX_VALUE))
        );
        contentPaneLayout.setVerticalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 412, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 122, Short.MAX_VALUE)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(serverBotton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cancelBotton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
                                .addGap(44, 44, 44))
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JButton cancelBotton;
    private JButton serverBotton;
    private JScrollPane scrollPane1;
    private JTree projectTree;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
