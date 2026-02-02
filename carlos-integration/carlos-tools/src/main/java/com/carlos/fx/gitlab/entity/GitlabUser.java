package com.carlos.fx.gitlab.entity;

import java.util.Date;

/**
 * GitLab User Entity
 *
 * @author Carlos
 * @since 3.0.0
 */
public class GitlabUser {

    private Long id;
    private String username;
    private String name;
    private String email;
    private String state;
    private String avatarUrl;
    private String webUrl;
    private Date createdAt;
    private String bio;
    private String location;
    private String publicEmail;
    private String skype;
    private String linkedin;
    private String twitter;
    private String websiteUrl;
    private String organization;
    private Boolean isAdmin;
    private Boolean canCreateGroup;
    private Boolean canCreateProject;
    private Integer projectsLimit;
    private Date currentSignInAt;
    private Date lastSignInAt;
    private Boolean confirmed;
    private String externalUid;
    private String provider;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPublicEmail() {
        return publicEmail;
    }

    public void setPublicEmail(String publicEmail) {
        this.publicEmail = publicEmail;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Boolean getCanCreateGroup() {
        return canCreateGroup;
    }

    public void setCanCreateGroup(Boolean canCreateGroup) {
        this.canCreateGroup = canCreateGroup;
    }

    public Boolean getCanCreateProject() {
        return canCreateProject;
    }

    public void setCanCreateProject(Boolean canCreateProject) {
        this.canCreateProject = canCreateProject;
    }

    public Integer getProjectsLimit() {
        return projectsLimit;
    }

    public void setProjectsLimit(Integer projectsLimit) {
        this.projectsLimit = projectsLimit;
    }

    public Date getCurrentSignInAt() {
        return currentSignInAt;
    }

    public void setCurrentSignInAt(Date currentSignInAt) {
        this.currentSignInAt = currentSignInAt;
    }

    public Date getLastSignInAt() {
        return lastSignInAt;
    }

    public void setLastSignInAt(Date lastSignInAt) {
        this.lastSignInAt = lastSignInAt;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getExternalUid() {
        return externalUid;
    }

    public void setExternalUid(String externalUid) {
        this.externalUid = externalUid;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
