/*
 * Created by JFormDesigner on Mon Apr 29 16:00:48 CST 2024
 */

package com.carlos.tool.ui;

import com.carlos.tool.codege.ui.CodeGeneratorTool;
import com.carlos.tool.encrypt.ui.EncryptTool;
import com.carlos.tool.gitlab.ui.GitlabTool;
import com.carlos.tool.projectge.ui.ProjectTool;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author carlos
 */
public class ToolHome extends JFrame {
    public ToolHome() {
        initComponents();
    }

    private void closeButtonActionPerformed(ActionEvent e) {
        dispose();
        System.exit(0);
    }

    private void codeTool(ActionEvent e) {
        new CodeGeneratorTool().setVisible(true);
    }

    private void projectTool(ActionEvent e) {
        new ProjectTool().setVisible(true);
    }

    private void encryptTool(ActionEvent e) {
        new EncryptTool().setVisible(true);
    }

    private void gitlabTool(ActionEvent e) {
        new GitlabTool().setVisible(true);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        codeTool = new JButton();
        encryptTool = new JButton();
        projectTool = new JButton();
        cancelButton = new JButton();
        gitlabTool = new JButton();

        //======== this ========
        setTitle("\u4e91\u6d25\u5f00\u53d1\u5de5\u5177");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {

                //---- codeTool ----
                codeTool.setText("\u4ee3\u7801\u751f\u6210\u5668");
                codeTool.addActionListener(e -> codeTool(e));

                //---- encryptTool ----
                encryptTool.setText("\u5e93\u8868\u6570\u636e\u52a0\u89e3\u5bc6");
                encryptTool.addActionListener(e -> encryptTool(e));

                //---- projectTool ----
                projectTool.setText("\u5de5\u7a0b\u811a\u624b\u67b6");
                projectTool.addActionListener(e -> projectTool(e));

                //---- cancelButton ----
                cancelButton.setText("\u5173   \u95ed");
                cancelButton.addActionListener(e -> closeButtonActionPerformed(e));

                //---- gitlabTool ----
                gitlabTool.setText("Gitlab\u5de5\u5177");
                gitlabTool.addActionListener(e -> gitlabTool(e));

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(GroupLayout.Alignment.TRAILING, contentPanelLayout.createSequentialGroup()
                            .addContainerGap(95, Short.MAX_VALUE)
                            .addGroup(contentPanelLayout.createParallelGroup()
                                .addComponent(codeTool, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE)
                                .addComponent(projectTool, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE)
                                .addComponent(encryptTool, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE)
                                .addComponent(gitlabTool, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE))
                            .addGap(96, 96, 96))
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addGap(137, 137, 137)
                            .addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                            .addContainerGap(142, Short.MAX_VALUE))
                );
                contentPanelLayout.setVerticalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(codeTool, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(projectTool, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(encryptTool, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                            .addComponent(gitlabTool, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                            .addGap(15, 15, 15))
                );
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);
        }
        contentPane.add(dialogPane, BorderLayout.EAST);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JButton codeTool;
    private JButton encryptTool;
    private JButton projectTool;
    private JButton cancelButton;
    private JButton gitlabTool;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
