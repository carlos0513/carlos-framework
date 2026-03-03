# Open Source Setup Guide

This guide will help you set up Carlos Framework as an open source project on GitHub and Gitee.

## 📋 Prerequisites

Before you begin, ensure you have:

1. **GitHub Account**: [github.com](https://github.com)
2. **Gitee Account**: [gitee.com](https://gitee.com) (for Chinese mirror)
3. **Git configured locally** with your credentials
4. **Admin access to the repository**

## 🚀 Step 1: Create GitHub Repository

### Option A: Convert Existing Repository

If this codebase is already in a GitHub repository:

1. Go to your GitHub repository
2. Navigate to Settings > General > Danger Zone
3. Click "Change visibility" to make it Public
4. Confirm the change

### Option B: Create New Repository

1. Go to [github.com/new](https://github.com/new)
2. Fill in repository details:
    - **Repository name**: `carlos-framework`
    - **Description**: "A comprehensive Java microservice framework based on Spring Boot 3.5.9"
    - **Visibility**: Public
    - **Initialize with**: Keep unchecked (we'll push existing code)
3. Click "Create repository"
4. In the repository directory, run:

```bash
# If this is a new local repository
git init
git add .
git commit -m "initial: open source release with complete documentation"
git branch -M main
git remote add origin https://github.com/yourusername/carlos-framework.git
git push -u origin main
```

## 🇨🇳 Step 2: Create Gitee Mirror (Optional but Recommended)

1. Go to [gitee.com](https://gitee.com) and log in
2. Click "+" (New) > "Create Repository"
3. Fill in:
    - **Repository name**: `carlos-framework`
    - **Description**: Use Chinese description from README-CN.md
    - **Visibility**: Public
4. Add Gitee as a second remote:

```bash
# Add Gitee remote
git remote add gitee https://gitee.com/yourusername/carlos-framework.git

# Push to both remotes in the future
git push origin main
git push gitee main
```

5. Configure automatic sync (optional):
    - Use GitHub Actions to sync to Gitee
    - Or use Gitee's built-in sync feature (premium feature)

## ⚙️ Step 3: Configure Repository Settings

### GitHub Settings

#### 1. Branch Protection Rules

Navigate to Settings > Branches and add protection rules:

**For `main` branch:**

- ✅ Require a pull request before merging
- ✅ Require approvals (1-2 minimum)
- ✅ Dismiss stale pull request approvals when new commits are pushed
- ✅ Require status checks to pass before merging
    - Add: `build`, `static-analysis`, `code-coverage`
- ✅ Require conversation resolution before merging
- ✅ Require signed commits (optional but recommended)
- ✅ Do not allow bypassing the above settings

**For `develop` branch:**

- Same rules as main with slightly relaxed requirements

#### 2. Issue Configuration

1. Enable discussions: Settings > General
2. Configure issue templates (already in `.github/ISSUE_TEMPLATE/`)
3. Enable issues: Settings > General > Features > Issues

#### 3. Security Settings

Navigate to Settings > Security:

- ✅ Enable vulnerability alerts
- ✅ Enable secret scanning
- ✅ Enable code scanning
- Set up Dependabot: Security > Dependabot alerts
- Review security policy (already in `SECURITY.md`)

#### 4. Webhooks & Notifications

If using external CI/CD:

1. Settings > Webhooks > Add webhook
2. Configure for your build system
3. Set content type and trigger events

#### 5. Repository Metadata

In repository root, update:

- Edit repository description
- Add topics/keywords:
    - `java`
    - `spring-boot`
    - `microservices`
    - `spring-cloud`
    - `mybatis-plus`
    - `redis`
    - `oauth2`
    - `framework`
    - `starter`
    - `multi-module`

### Gitee Settings (Similar Configuration)

1. Go to repository Settings
2. Configure branch protection (similar to GitHub)
3. Enable code quality scanning
4. Set up webhook for sync if needed

## 📚 Step 4: Review Open Source Files

Ensure all open source files are complete:

### ✅ Required Files

- [x] `README.md` - Project overview (in English)
- [x] `README-CN.md` - Project overview (in Chinese)
- [x] `LICENSE` - MIT License
- [x] `CONTRIBUTING.md` - Contribution guidelines
- [x] `CODE_OF_CONDUCT.md` - Community standards
- [x] `SECURITY.md` - Security policy and vulnerability reporting
- [x] `AUTHORS.md` - Project contributors
- [x] `CHANGELOG.md` - Version history
- [x] `.gitignore` - Git ignore patterns
- [x] `.gitattributes` - Git attributes for line endings
- [x] `.editorconfig` - Editor configuration

### ✅ GitHub-Specific Files (in `.github/`)

- [x] `ISSUE_TEMPLATE/bug_report.yml` - Bug report template
- [x] `ISSUE_TEMPLATE/feature_request.yml` - Feature request template
- [x] `ISSUE_TEMPLATE/config.yml` - Issue template configuration
- [x] `PULL_REQUEST_TEMPLATE.md` - Pull request template
- [x] `workflows/maven-build.yml` - Maven build CI
- [x] `workflows/code-quality.yml` - Code quality checks
- [x] `workflows/release.yml` - Release automation
- [x] `workflows/stale.yml` - Stale issue management
- [x] `dependabot.yml` - Automated dependency updates
- [x] `funding.yml` - Sponsorship information

## 🔄 Step 5: Verify CI/CD Workflows

GitHub Actions workflows are configured in `.github/workflows/`.

### Check Workflow Status

1. Navigate to Actions tab
2. Verify workflows are triggering correctly
3. Check workflow logs for errors

### Required Workflow Secrets

If using external services, configure these secrets:

- Settings > Security > Secrets and variables > Actions

Common secrets needed:

- `CODECOV_TOKEN` - For code coverage reporting
- `GITHUB_TOKEN` - Auto-provided by GitHub
- `NEXUS_USERNAME` - For deployments (if applicable)
- `NEXUS_PASSWORD` - For deployments (if applicable)
- `DOCKER_USERNAME` - For Docker builds
- `DOCKER_PASSWORD` - For Docker builds

## 📊 Step 6: Enable Analytics and Insights

### GitHub Insights

Navigate to Insights tab to view:

- Traffic (views, clones)
- Commit frequency
- Code frequency
- Contributors
- Community health

### Enable GitHub Discussions

Settings > General > Features > Discussions

Benefits:

- Community Q&A
- Announcements
- Feature discussions
- Reduce issue noise

## 🏷️ Step 7: Create Initial Release

Create your first release to mark the open source launch:

1. Go to Releases > Create a new release
2. Tag version: `v3.0.0`
3. Release title: "Open Source Launch"
4. Description: Use content from CHANGELOG.md
5. Upload release artifacts (optional):
    - JAR files
    - Documentation
    - Sample configs

## 📢 Step 8: Promote Your Project

### Announce the Release

Share in appropriate channels:

1. **Twitter/LinkedIn** - Professional networks
2. **Reddit** - r/java, r/springboot
3. **Hacker News** - Show HN
4. **Gitee** - Chinese developer community
5. **V2EX** - Chinese tech community
6. **技术博客** - Write technical blog posts

### Communication Templates

#### English Announcement

```markdown
🚀 Excited to announce that Carlos Framework is now open source!

A comprehensive Java microservice framework built on Spring Boot 3.5.9:

✅ 38+ starter modules
✅ OAuth2 authorization server
✅ Multi-level caching
✅ Built-in code generation
✅ Production-ready

Check it out: https://github.com/yourusername/carlos-framework

#java #springboot #microservices #opensource
```

#### Chinese Announcement

```markdown
🚀 Carlos Framework 现已开源！

基于 Spring Boot 3.5.9 的综合性 Java 微服务框架：

✅ 38+ starter 模块
✅ OAuth2 认证服务器
✅ 多级缓存
✅ 内置代码生成器
✅ 生产就绪

GitHub: https://github.com/yourusername/carlos-framework
Gitee: https://gitee.com/yourusername/carlos-framework

#Java #SpringBoot #微服务 #开源
```

## 🔧 Step 9: Ongoing Maintenance

### Daily/Weekly Tasks

- [ ] Review and merge PRs
- [ ] Respond to issues
- [ ] Monitor Discussions
- [ ] Review Dependabot alerts
- [ ] Check security advisories

### Monthly Tasks

- [ ] Review community health
- [ ] Update dependencies
- [ ] Review and close stale issues
- [ ] Analyze traffic/usage patterns
- [ ] Plan roadmap

### Release Process

When releasing new versions:

1. Update version in POM files
2. Update CHANGELOG.md
3. Create release notes
4. Tag the release
5. Push to both GitHub and Gitee
6. Announce the release

## 🌐 Bilingual Support Strategy

### Primary: GitHub (English)

- Main README: README.md (English)
- Issue templates: English
- PR templates: English
- Documentation: English
- Discussions: Primarily English

### Secondary: Gitee (Chinese)

- Chinese README: README-CN.md
- Chinese announcements
- Chinese community support
- Consider creating Chinese issue labels

### Code Comments

- Use English for all code comments
- Use English for Javadoc
- Consistent terminology across both platforms

## 🛡️ Security Considerations

### For Maintainers

1. **Never merge directly to main**: Always use PRs
2. **Review all dependencies**: Check for vulnerabilities
3. **Monitor security advisories**: Act quickly on CVEs
4. **Control access**: Limit maintainer permissions
5. **Audit commit access**: Regularly review contributors

### For Contributors

1. **Never commit secrets**: Use environment variables
2. **Validate all inputs**: Prevent injections
3. **Follow security guidelines**: See SECURITY.md
4. **Report vulnerabilities privately**: Email security team

## 📚 Useful Links

- [GitHub Open Source Guides](https://opensource.guide/)
- [Best Practices for Maintainers](https://opensource.guide/best-practices/)
- [Building Welcoming Communities](https://opensource.guide/building-community/)
- [Open Source Security Best Practices](https://github.blog/security/)
- [Semantic Versioning](https://semver.org/)
- [Keep a Changelog](https://keepachangelog.com/)

## ❓ Troubleshooting

### Common Issues

**Workflow Fails**: Check secrets are configured
**Build Fails**: Verify JDK and Maven versions
**Permission Issues**: Check repository settings
**Sync Problems**: Verify remotes are configured correctly

### Getting Help

- GitHub Community Forum
- GitHub Support
- Open source community resources

## 🎉 Success Metrics

Track these metrics to measure project success:

- ⭐ Star count (GitHub)
- 🍴 Fork count
- 👀 Watchers
- 📊 Traffic analytics
- 🐛 Issues submitted/resolved
- 🔀 PRs merged
- 📝 Contributors
- 💬 Discussion activity
- 📥 Downloads (if applicable)

---

Congratulations on open sourcing Carlos Framework! With proper maintenance and community engagement, this can become a
valuable resource for Java developers worldwide.

**Next Steps**: Verify all configurations and make your first announcement! 🚀
