# Carlos Tools - Quick Start Guide

## 🚀 Getting Started

### Prerequisites

- **JDK 17 or higher** (required for Spring Boot 3 and JavaFX 21)
- **Maven 3.8+**
- **Git** (for version control)

### Installation

1. **Clone the repository** (if not already done):

```bash
git clone <repository-url>
cd carlos-framework
```

2. **Navigate to carlos-tools**:

```bash
cd carlos-integration/carlos-tools
```

3. **Build the project**:

```bash
mvn clean install
```

4. **Run the application**:

```bash
mvn javafx:run
```

## 📋 First Time Setup

### 1. Code Generator

**Step 1: Configure Database Connection**

- Database Type: Select MySQL (or your database)
- Host: `localhost`
- Port: `3306`
- Database Name: Your database name
- Username: Your database username
- Password: Your database password

**Step 2: Test Connection**

- Click "测试连接" (Test Connection)
- Wait for success notification

**Step 3: Load Tables**

- Click "加载表信息" (Load Tables)
- Wait for tables to load

**Step 4: Select Tables**

- Expand the tree view
- Check the tables you want to generate code for

**Step 5: Configure Generation**

- Package: `com.carlos.demo` (or your package)
- Author: Your name
- Output Path: Click "浏览..." to select output directory
- Generation Options: Check Entity, Mapper, Service, Controller

**Step 6: Generate Code**

- Click "生成代码" (Generate Code)
- Wait for completion
- Check output directory for generated files

### 2. Project Scaffolding

**Step 1: Configure Project**

- Project Name: `my-project`
- Group ID: `com.carlos`
- Artifact ID: `my-project` (auto-filled)
- Version: `1.0.0-SNAPSHOT`
- Package: `com.carlos.myproject` (auto-filled)
- Author: Your name

**Step 2: Select Template**

- Choose from 5 templates:
    - Spring Boot 基础项目
    - Spring Boot + MyBatis-Plus
    - Spring Boot + Redis
    - Spring Boot 微服务
    - Spring Boot 完整项目

**Step 3: Select Output Directory**

- Click "浏览..." to select output directory

**Step 4: Generate Project**

- Click "生成项目" (Generate Project)
- Wait for completion
- Optionally open project directory

### 3. Encryption Tool

**Step 1: Configure Database Connection**

- Same as Code Generator

**Step 2: Load Tables**

- Click "加载表信息" (Load Tables)

**Step 3: Select Fields**

- Expand table nodes
- Check the fields you want to encrypt/decrypt

**Step 4: Configure Encryption**

- SM4 Key: 32-character hex string (e.g., `0123456789abcdef0123456789abcdef`)
- SM4 IV: 32-character hex string (e.g., `0123456789abcdef0123456789abcdef`)
- Operation Type: Select "加密" (Encrypt) or "解密" (Decrypt)
- Batch Size: `5000` (default)

**Step 5: Execute Operation**

- Click "执行操作" (Execute Operation)
- Monitor progress in log area
- Wait for completion

### 4. GitLab Tool

**Step 1: Configure GitLab Server**

- Click "添加服务器" (Add Server)
- Server URL: `http://gitlab.example.com`
- Access Token: Your GitLab personal access token
- Name: Server name
- Description: Optional description

**Step 2: Select Server**

- Choose server from dropdown

**Step 3: Use Features**

**Merge Requests:**

- Select project
- Filter by state (opened, closed, merged)
- Search merge requests
- Actions: Create, View, Approve, Merge, Close

**Branches:**

- Select project
- Search branches
- Select branches with checkboxes
- Actions: Create, Delete, Protect, Unprotect, Compare, Delete Merged

**Issues:**

- Select project
- Filter by state (opened, closed)
- Search issues
- Select issues with checkboxes
- Actions: Create, View, Close, Reopen, Assign, Add Label, Batch Close

**Users:**

- Search users
- Select users with checkboxes
- Actions: Add, Edit, Delete, Block, Unblock, Import from Excel, Export to Excel, Batch Delete

## 🎨 UI Features

### Navigation

- Click on tool names in the left sidebar to switch between tools
- Icons indicate each tool type

### Search and Filter

- Use search boxes to filter results in real-time
- Use combo boxes to filter by state, project, etc.

### Batch Operations

- Use checkboxes to select multiple items
- Click batch operation buttons (e.g., "批量删除", "批量关闭")

### Progress Tracking

- Progress bars show operation progress
- Status labels show current operation
- Notifications appear on success/error

### Notifications

- Success: Green notification in bottom-right
- Error: Red notification in bottom-right
- Warning: Yellow notification in bottom-right
- Info: Blue notification in bottom-right

## 🔧 Configuration

### Database Connection

- Connections are not saved by default
- You need to enter connection details each time
- Future enhancement: Save recent connections

### GitLab Server

- Server configurations are not saved by default
- You need to configure server each time
- Future enhancement: Save server configurations

### SM4 Encryption Keys

- Keys are not saved by default
- You need to enter keys each time
- Keep your keys secure and do not share them

## 📦 Building Native Installer

### Windows Installer

1. **Build the application**:

```bash
mvn clean install
```

2. **Create runtime image**:

```bash
mvn javafx:jlink
```

3. **Create installer**:

```bash
mvn jpackage:jpackage
```

4. **Find installer**:

- Location: `target/dist/`
- File: `CarlosTools-3.0.0.exe` (or similar)

### Installer Options

- Install directory chooser: Yes
- Desktop shortcut: Yes
- Start menu entry: Yes

## 🐛 Troubleshooting

### Application Won't Start

**Problem**: "JavaFX runtime components are missing"

**Solution**:

```bash
# Use Maven to run
mvn javafx:run

# Or ensure JavaFX is in classpath
java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -jar carlos-tools.jar
```

### Database Connection Failed

**Problem**: "Connection refused" or "Access denied"

**Solutions**:

1. Check database is running
2. Verify host and port are correct
3. Check username and password
4. Ensure database exists
5. Check firewall settings

### GitLab API Errors

**Problem**: "401 Unauthorized" or "403 Forbidden"

**Solutions**:

1. Verify GitLab server URL is correct
2. Check access token is valid
3. Ensure token has required permissions (api, read_user, write_repository)
4. Check GitLab server is accessible

### Code Generation Failed

**Problem**: "Template not found" or "Output directory not writable"

**Solutions**:

1. Ensure output directory exists and is writable
2. Check template files are in resources
3. Verify package name is valid
4. Check disk space

### UI Not Responding

**Problem**: UI freezes during operations

**Solutions**:

1. Wait for operation to complete (check progress bar)
2. If truly frozen, restart application
3. Check console for error messages
4. Reduce batch size for large operations

## 💡 Tips and Best Practices

### Code Generator

- Test database connection before loading tables
- Select only tables you need to reduce generation time
- Use meaningful package names
- Review generated code before using in production

### Project Scaffolding

- Choose appropriate template for your needs
- Use consistent naming conventions
- Review generated project structure
- Customize generated code as needed

### Encryption Tool

- **IMPORTANT**: Backup your database before encryption
- Test encryption on a small dataset first
- Keep encryption keys secure
- Use same keys for encryption and decryption
- Monitor operation log for errors

### GitLab Tool

- Use search and filter to find items quickly
- Use batch operations for multiple items
- Review changes before merging
- Add meaningful comments to merge requests
- Assign issues to appropriate users

## 🔐 Security Notes

### Database Credentials

- Never commit database credentials to version control
- Use environment variables for sensitive data
- Rotate passwords regularly

### GitLab Tokens

- Keep access tokens secure
- Use tokens with minimum required permissions
- Rotate tokens regularly
- Never share tokens

### Encryption Keys

- Generate strong random keys
- Store keys securely (not in code)
- Use different keys for different environments
- Never commit keys to version control

## 📚 Additional Resources

### Documentation

- `README_JAVAFX.md` - Comprehensive documentation
- `MIGRATION_SUMMARY.md` - Implementation summary
- JavaFX Documentation: https://openjfx.io/
- GitLab API Documentation: https://docs.gitlab.com/ee/api/

### Support

- Report issues on GitHub
- Check console output for error details
- Review log files in application directory

## 🎯 Next Steps

After getting familiar with the application:

1. **Customize Code Templates**
    - Modify templates in `src/main/resources/templates/`
    - Adjust to match your coding standards

2. **Configure GitLab Integration**
    - Set up GitLab server configurations
    - Create access tokens with appropriate permissions
    - Test all GitLab features

3. **Explore Advanced Features**
    - Batch operations
    - Excel import/export
    - Branch comparison
    - Merge request workflows

4. **Provide Feedback**
    - Report bugs
    - Suggest improvements
    - Share use cases

---

**Happy Coding! 🚀**

For questions or issues, please contact the development team or create an issue on GitHub.
