/*
 * Created by JFormDesigner on Sat Dec 28 00:49:26 CST 2019
 */

package com.carlos.tool.gitlab.ui;

import com.carlos.tool.gitlab.config.GitLabServerInfo;
import com.carlos.tool.gitlab.service.GitlabService;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Map;


/**
 * <p>
 *
 * </p>
 *
 * @author Carlos
 * @date 2019/12/28 1:36
 */
@Slf4j
public class GitlabTool extends JFrame {

    public GitlabTool() {
        initComponents();
        gitlabServiceMap = new java.util.HashMap<>();
    }


    private Map<String, GitLabServerInfo> gitlabServiceMap;
    private GitlabService gitlabService = new GitlabService();
    private GitLabServerInfo gitlabSeverInfo;

    private void thisWindowClosed(WindowEvent e) {
        dispose();
        System.exit(0);
    }

    private void thisWindowClosing(WindowEvent e) {
        thisWindowClosed(e);
    }

    private void thisWindowOpened(WindowEvent e) {
        // GitlabToolType[] values = GitlabToolType.values();
        // for (GitlabToolType value : values) {
        //     this.comboBox2.addItem(value.getDesc());
        // }

    }

    /**
     * 切换工具类型
     *
     * @param e 参数0
     * @throws
     * @author Carlos
     * @date 2024/4/29 14:34
     */
    // private void toolTypeItemStateChanged(ItemEvent e) {
    //     int selectedIndex = comboBox2.getSelectedIndex();
    //     if (selectedIndex == 0) {
    //         return;
    //     }
    //     // 当指定的数据库选中时，获取对应的表信息
    //     if (e.getStateChange() != ItemEvent.SELECTED) {
    //         return;
    //     }
    //
    //     GitlabToolType[] values = GitlabToolType.values();
    //     GitlabToolType toolType = values[selectedIndex - 1];
    //     JFrame frame = toolType.getFrame();
    //     switch (toolType) {
    //         case CREATE_MERGE_REQUEST:
    //             CreateMergeRequest createMergeRequest = (CreateMergeRequest) frame;
    //             createMergeRequest.setServerInfo(gitlabSeverInfo);
    //             break;
    //
    //         default:
    //             throw new IllegalStateException("Unexpected value: " + toolType);
    //     }
    //     frame.setVisible(true);
    // }
    private void newServerButtonActionPerformed(ActionEvent e) {
        new GitlabServerAddUI().setVisible(true);
    }


    /**
     * 切换服务选项
     *
     * @param e 参数0
     * @throws
     * @author Carlos
     * @date 2024/4/29 14:32
     */
    private void serverItemStateChanged(ItemEvent e) {
        int selectedIndex = serverSelect.getSelectedIndex();
        if (selectedIndex == 0) {
            return;
        }

        // 获取选中的服务信息
        if (e.getStateChange() != ItemEvent.SELECTED) {
            return;
        }
        Object selectedItem = serverSelect.getSelectedItem();
        GitLabServerInfo selectServer = gitlabServiceMap.get((String) selectedItem);
        serverHost.setText(selectServer.getServerHost());
        serverKey.setText(selectServer.getServerKey());
        this.gitlabSeverInfo = selectServer;
    }

    private void closeButtonActionPerformed(ActionEvent e) {
        System.exit(0);
    }

    private void initSelectServer(ActionEvent e) {

    }

    private void serverSelectMouseClicked(MouseEvent e) {
        // 初始化可选服务
        List<GitLabServerInfo> locationServers = gitlabService.getLocationServers();
        serverSelect.removeAllItems();
        serverSelect.addItem("--请选择Gitlab服务--");
        for (GitLabServerInfo locationServer : locationServers) {
            serverSelect.addItem(locationServer.getServerName());
            gitlabServiceMap.put(locationServer.getServerName(), locationServer);
        }
    }

    private void commitBotton(ActionEvent e) {
        GitlabToolBar gitlabToolBar = new GitlabToolBar();
        gitlabToolBar.setServerInfo(gitlabSeverInfo);
        gitlabToolBar.setVisible(true);

    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        serverKey = new JTextField();
        label4 = new JLabel();
        cancelBotton = new JButton();
        label10 = new JLabel();
        label11 = new JLabel();
        serverHost = new JTextField();
        serverSelect = new JComboBox<>();
        serverBotton = new JButton();
        commitBotton = new JButton();

        //======== this ========
        setTitle("Gitlab\u5feb\u6377\u5904\u7406\u5de5\u5177");
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

        //---- serverKey ----
        serverKey.setEditable(false);

        //---- label4 ----
        label4.setText("\u4ee4  \u724c:");
        label4.setHorizontalAlignment(SwingConstants.RIGHT);

        //---- cancelBotton ----
        cancelBotton.setText("\u5173   \u95ed");
        cancelBotton.addActionListener(e -> closeButtonActionPerformed(e));

        //---- label10 ----
        label10.setText("\u8bf7\u9009\u62e9\u670d\u52a1:");
        label10.setHorizontalAlignment(SwingConstants.RIGHT);

        //---- label11 ----
        label11.setText("gitlab\u5730\u5740:");
        label11.setHorizontalAlignment(SwingConstants.RIGHT);

        //---- serverHost ----
        serverHost.setEditable(false);

        //---- serverSelect ----
        serverSelect.setModel(new DefaultComboBoxModel<>(new String[]{
                "--\u8bf7\u9009\u62e9Gitlab\u670d\u52a1--"
        }));
        serverSelect.setSelectedIndex(0);
        serverSelect.addItemListener(e -> serverItemStateChanged(e));
        serverSelect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                serverSelectMouseClicked(e);
            }
        });

        //---- serverBotton ----
        serverBotton.setText("\u65b0\u589e\u670d\u52a1");
        serverBotton.addActionListener(e -> newServerButtonActionPerformed(e));

        //---- commitBotton ----
        commitBotton.setText("\u786e   \u8ba4");
        commitBotton.addActionListener(e -> commitBotton(e));

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addGap(82, 82, 82)
                                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                                        .addComponent(label11, GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                                                        .addComponent(label4)
                                                        .addComponent(label10, GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(serverKey, GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                                                        .addComponent(serverHost, GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                                                        .addComponent(serverSelect, GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(serverBotton))
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addGap(182, 182, 182)
                                                .addComponent(commitBotton, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                                                .addGap(57, 57, 57)
                                                .addComponent(cancelBotton, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(70, Short.MAX_VALUE))
        );
        contentPaneLayout.setVerticalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(serverBotton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(label10, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(serverSelect))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(label11, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(serverHost, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(label4, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(serverKey, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(commitBotton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cancelBotton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
                                .addGap(24, 24, 24))
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JTextField serverKey;
    private JLabel label4;
    private JButton cancelBotton;
    private JLabel label10;
    private JLabel label11;
    private JTextField serverHost;
    private JComboBox<String> serverSelect;
    private JButton serverBotton;
    private JButton commitBotton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
