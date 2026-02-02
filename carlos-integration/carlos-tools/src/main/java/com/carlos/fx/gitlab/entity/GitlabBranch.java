package com.carlos.fx.gitlab.entity;

import java.util.Date;

/**
 * GitLab 分支实体类
 * 封装 GitLab 分支的详细信息
 *
 * <p>该类用于存储从 GitLab API 获取的分支信息，包括：</p>
 * <ul>
 *   <li>分支基本信息：名称、是否合并、是否受保护等</li>
 *   <li>权限信息：开发者是否可以推送、合并等</li>
 *   <li>最后提交信息：提交 ID、标题、作者、时间等</li>
 * </ul>
 *
 * <p>使用场景：</p>
 * <ul>
 *   <li>在 TableView 中显示分支列表</li>
 *   <li>在对话框中显示分支详情</li>
 *   <li>作为分支操作的参数传递</li>
 * </ul>
 *
 * @author Carlos
 * @since 3.0.0
 */
public class GitlabBranch {

    /**
     * 分支名称
     * 例如：main, develop, feature/user-auth
     */
    private String name;

    /**
     * 是否已合并
     * true 表示该分支已经合并到默认分支
     */
    private Boolean merged;

    /**
     * 是否受保护
     * 受保护的分支不能被强制推送或删除，通常用于主分支
     */
    private Boolean protectedBranch;

    /**
     * 是否为默认分支
     * 默认分支通常是 main 或 master
     */
    private Boolean defaultBranch;

    /**
     * 开发者是否可以推送
     * 控制普通开发者是否有权限推送到该分支
     */
    private Boolean developersCanPush;

    /**
     * 开发者是否可以合并
     * 控制普通开发者是否有权限合并到该分支
     */
    private Boolean developersCanMerge;

    /**
     * 当前用户是否可以推送
     * 根据当前用户的权限判断是否可以推送到该分支
     */
    private Boolean canPush;

    /**
     * 最后提交的完整 ID
     * 40 位的 SHA-1 哈希值，例如：a1b2c3d4e5f6...
     */
    private String commitId;

    /**
     * 最后提交的短 ID
     * 通常是前 8 位，例如：a1b2c3d4
     */
    private String commitShortId;

    /**
     * 最后提交的标题
     * 提交消息的第一行
     */
    private String commitTitle;

    /**
     * 最后提交的完整消息
     * 包含提交的详细描述
     */
    private String commitMessage;

    /**
     * 最后提交的作者名称
     */
    private String commitAuthorName;

    /**
     * 最后提交的作者邮箱
     */
    private String commitAuthorEmail;

    /**
     * 最后提交的创建时间
     */
    private Date commitCreatedAt;

    // ==================== Getter 和 Setter 方法 ====================

    /**
     * 获取分支名称
     * @return 分支名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置分支名称
     * @param name 分支名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取是否已合并
     * @return true 表示已合并，false 表示未合并
     */
    public Boolean getMerged() {
        return merged;
    }

    /**
     * 设置是否已合并
     * @param merged 是否已合并
     */
    public void setMerged(Boolean merged) {
        this.merged = merged;
    }

    /**
     * 获取是否受保护
     * @return true 表示受保护，false 表示不受保护
     */
    public Boolean getProtectedBranch() {
        return protectedBranch;
    }

    /**
     * 设置是否受保护
     * @param protectedBranch 是否受保护
     */
    public void setProtectedBranch(Boolean protectedBranch) {
        this.protectedBranch = protectedBranch;
    }

    /**
     * 获取是否为默认分支
     * @return true 表示是默认分支，false 表示不是
     */
    public Boolean getDefaultBranch() {
        return defaultBranch;
    }

    /**
     * 设置是否为默认分支
     * @param defaultBranch 是否为默认分支
     */
    public void setDefaultBranch(Boolean defaultBranch) {
        this.defaultBranch = defaultBranch;
    }

    /**
     * 获取开发者是否可以推送
     * @return true 表示开发者可以推送，false 表示不可以
     */
    public Boolean getDevelopersCanPush() {
        return developersCanPush;
    }

    /**
     * 设置开发者是否可以推送
     * @param developersCanPush 开发者是否可以推送
     */
    public void setDevelopersCanPush(Boolean developersCanPush) {
        this.developersCanPush = developersCanPush;
    }

    /**
     * 获取开发者是否可以合并
     * @return true 表示开发者可以合并，false 表示不可以
     */
    public Boolean getDevelopersCanMerge() {
        return developersCanMerge;
    }

    /**
     * 设置开发者是否可以合并
     * @param developersCanMerge 开发者是否可以合并
     */
    public void setDevelopersCanMerge(Boolean developersCanMerge) {
        this.developersCanMerge = developersCanMerge;
    }

    /**
     * 获取当前用户是否可以推送
     * @return true 表示当前用户可以推送，false 表示不可以
     */
    public Boolean getCanPush() {
        return canPush;
    }

    /**
     * 设置当前用户是否可以推送
     * @param canPush 当前用户是否可以推送
     */
    public void setCanPush(Boolean canPush) {
        this.canPush = canPush;
    }

    /**
     * 获取最后提交的完整 ID
     * @return 提交的完整 SHA-1 哈希值
     */
    public String getCommitId() {
        return commitId;
    }

    /**
     * 设置最后提交的完整 ID
     * @param commitId 提交的完整 ID
     */
    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    /**
     * 获取最后提交的短 ID
     * @return 提交的短 ID（通常是前 8 位）
     */
    public String getCommitShortId() {
        return commitShortId;
    }

    /**
     * 设置最后提交的短 ID
     * @param commitShortId 提交的短 ID
     */
    public void setCommitShortId(String commitShortId) {
        this.commitShortId = commitShortId;
    }

    /**
     * 获取最后提交的标题
     * @return 提交消息的第一行
     */
    public String getCommitTitle() {
        return commitTitle;
    }

    /**
     * 设置最后提交的标题
     * @param commitTitle 提交标题
     */
    public void setCommitTitle(String commitTitle) {
        this.commitTitle = commitTitle;
    }

    /**
     * 获取最后提交的完整消息
     * @return 提交的完整消息
     */
    public String getCommitMessage() {
        return commitMessage;
    }

    /**
     * 设置最后提交的完整消息
     * @param commitMessage 提交消息
     */
    public void setCommitMessage(String commitMessage) {
        this.commitMessage = commitMessage;
    }

    /**
     * 获取最后提交的作者名称
     * @return 作者名称
     */
    public String getCommitAuthorName() {
        return commitAuthorName;
    }

    /**
     * 设置最后提交的作者名称
     * @param commitAuthorName 作者名称
     */
    public void setCommitAuthorName(String commitAuthorName) {
        this.commitAuthorName = commitAuthorName;
    }

    /**
     * 获取最后提交的作者邮箱
     * @return 作者邮箱
     */
    public String getCommitAuthorEmail() {
        return commitAuthorEmail;
    }

    /**
     * 设置最后提交的作者邮箱
     * @param commitAuthorEmail 作者邮箱
     */
    public void setCommitAuthorEmail(String commitAuthorEmail) {
        this.commitAuthorEmail = commitAuthorEmail;
    }

    /**
     * 获取最后提交的创建时间
     * @return 提交创建时间
     */
    public Date getCommitCreatedAt() {
        return commitCreatedAt;
    }

    /**
     * 设置最后提交的创建时间
     * @param commitCreatedAt 提交创建时间
     */
    public void setCommitCreatedAt(Date commitCreatedAt) {
        this.commitCreatedAt = commitCreatedAt;
    }
}
