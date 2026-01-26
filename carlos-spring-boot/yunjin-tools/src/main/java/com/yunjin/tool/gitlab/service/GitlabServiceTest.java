package com.yunjin.tool.gitlab.service;

import com.google.common.collect.Sets;
import com.yunjin.tool.gitlab.config.GitLabServerInfo;
import org.junit.jupiter.api.Test;

public class GitlabServiceTest {


    @Test
    public void loadProject() {
        GitlabService gitlabService = new GitlabService();
        GitLabServerInfo server = new GitLabServerInfo();
        server.setServerName("");
        server.setServerHost("http://100.127.2.47:10001/");
        server.setServerKey("hoxtM8FpYx96chQk4LhV");
        gitlabService.loadProject(server);
    }

    @Test
    public void exportCommit() {
        GitlabService gitlabService = new GitlabService();
        try {
            gitlabService.exportCommit(Sets.newHashSet("248", "201", "199", "246", "254", "366"));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void exportBranch() {
        GitlabService gitlabService = new GitlabService();
        try {
            gitlabService.exportBranchs("154");
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}