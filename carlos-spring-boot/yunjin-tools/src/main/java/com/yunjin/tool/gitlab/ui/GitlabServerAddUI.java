/*
 * Created by JFormDesigner on Mon Apr 29 15:20:38 CST 2024
 */

package com.yunjin.tool.gitlab.ui;

import cn.hutool.core.util.StrUtil;
import com.yunjin.tool.gitlab.config.GitLabServerInfo;
import com.yunjin.tool.gitlab.service.GitlabService;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author carlos
 */
@Slf4j
public class GitlabServerAddUI extends JFrame {
    public GitlabServerAddUI() {
        initComponents();
    }

    private void commitButtonActionPerformed(ActionEvent e) {
        String serverNameText = serverName.getText();
        if (StrUtil.isBlank(serverNameText)) {
            JOptionPane.showMessageDialog(null, "服务名称不能为空！", "请完善信息", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String serverHostText = serverHost.getText();
        if (StrUtil.isBlank(serverHostText)) {
            JOptionPane.showMessageDialog(null, "服务地址不能为空！", "请完善信息", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String serverKeyText = serverKey.getText();
        if (StrUtil.isBlank(serverKeyText)) {
            JOptionPane.showMessageDialog(null, "服务密钥不能为空！", "请完善信息", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        GitLabServerInfo serverInfo = new GitLabServerInfo();
        serverInfo.setServerName(serverNameText);
        serverInfo.setServerHost(serverHostText);
        serverInfo.setServerKey(serverKeyText);
        GitlabService gitlabService = new GitlabService();
        try {
            gitlabService.saveServerInfo(serverInfo);
            JOptionPane.showConfirmDialog(null, "保存成功？", "提示", JOptionPane.OK_CANCEL_OPTION);
        } catch (Exception exception) {
            log.error("服务信息保存失败！", exception);
            JOptionPane.showMessageDialog(null, "服务信息保存失败！:" + exception.getMessage(), "保存失败", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void commit(ActionEvent e) {
        // TODO add your code here
    }

    private void closeButtonActionPerformed(ActionEvent e) {
        // TODO add your code here
        this.setVisible(false);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        label1 = new JLabel();
        serverName = new JTextField();
        label2 = new JLabel();
        serverHost = new JTextField();
        label3 = new JLabel();
        serverKey = new JTextField();
        commitButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        setTitle("Gitlab\u670d\u52a1\u6dfb\u52a0");
        Container contentPane = getContentPane();

        //---- label1 ----
        label1.setText("\u540d      \u79f0:");
        label1.setHorizontalAlignment(SwingConstants.RIGHT);

        //---- label2 ----
        label2.setText("gitlab\u5730\u5740:");
        label2.setHorizontalAlignment(SwingConstants.RIGHT);

        //---- label3 ----
        label3.setText("\u4ee4      \u724c:");
        label3.setHorizontalAlignment(SwingConstants.RIGHT);

        //---- commitButton ----
        commitButton.setText("\u786e\u8ba4");
        commitButton.addActionListener(e -> {
            commitButtonActionPerformed(e);
            commit(e);
        });

        //---- cancelButton ----
        cancelButton.setText("\u5173   \u95ed");
        cancelButton.addActionListener(e -> closeButtonActionPerformed(e));

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addGap(62, 62, 62)
                                                .addGroup(contentPaneLayout.createParallelGroup()
                                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                                .addComponent(label1, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
                                                                .addGap(12, 12, 12)
                                                                .addComponent(serverName, GroupLayout.PREFERRED_SIZE, 246, GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                                .addComponent(label2, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
                                                                .addGap(12, 12, 12)
                                                                .addComponent(serverHost, GroupLayout.PREFERRED_SIZE, 246, GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                                .addGap(33, 33, 33)
                                                                .addComponent(label3)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(serverKey, GroupLayout.PREFERRED_SIZE, 246, GroupLayout.PREFERRED_SIZE))))
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addGap(124, 124, 124)
                                                .addComponent(commitButton, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
                                                .addGap(76, 76, 76)
                                                .addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(94, Short.MAX_VALUE))
        );
        contentPaneLayout.setVerticalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addComponent(label1, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(serverName, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12)
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addComponent(label2, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(serverHost, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(serverKey, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(label3, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
                                .addGap(31, 31, 31)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(commitButton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(43, Short.MAX_VALUE))
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JLabel label1;
    private JTextField serverName;
    private JLabel label2;
    private JTextField serverHost;
    private JLabel label3;
    private JTextField serverKey;
    private JButton commitButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
