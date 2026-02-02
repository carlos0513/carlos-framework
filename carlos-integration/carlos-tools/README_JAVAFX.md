# Carlos Tools - JavaFX Migration

## Overview

This document describes the Carlos Tools JavaFX migration implementation.

## Build and Run

### Prerequisites

- JDK 17 or higher
- Maven 3.8+

### Build

```bash
cd carlos-integration/carlos-tools
mvn clean install
```

### Run

```bash
mvn javafx:run
```

### Package Native Installer

```bash
mvn jpackage:jpackage
```

The installer will be created in `target/dist/`.

## Architecture

### Module Structure

```
carlos-tools/
├── src/main/java/com/carlos/tool/
│   ├── fx/                           # JavaFX UI Layer
│   │   ├── app/                      # Application entry point
│   │   ├── common/                   # Shared components
│   │   │   ├── component/            # Reusable UI components
│   │   │   ├── controller/           # Base controllers
│   │   │   └── util/                 # JavaFX utilities
│   │   ├── main/                     # Main window
│   │   ├── codege/                   # Code Generator UI
│   │   ├── projectge/                # Project Scaffolding UI
│   │   ├── encrypt/                  # Encryption Tool UI
│   │   └── gitlab/                   # GitLab Tool UI
│   ├── codege/                       # Code Generator business logic
│   ├── projectge/                    # Project Scaffolding business logic
│   ├── encrypt/                      # Encryption Tool business logic
│   └── gitlab/                       # GitLab business logic
│       ├── entity/                   # GitLab entities
│       └── service/                  # GitLab services
└── src/main/resources/
    ├── fxml/                         # FXML layouts
    ├── css/                          # Stylesheets
    └── images/                       # Icons
```

## Features

### 1. Code Generator

- Database connection (MySQL, PostgreSQL, etc.)
- Table selection with tree view
- Code generation for Entity, Mapper, Service, Controller
- Progress tracking
- Batch generation

### 2. Project Scaffolding

- Project configuration (name, groupId, artifactId, package)
- Multiple project templates
- Template description preview
- Output directory selection
- Progress tracking

### 3. Encryption Tool

- Database connection
- Table and field selection
- SM4 encryption/decryption
- Batch processing (5000 records per batch)
- Operation log

### 4. GitLab Tool

#### Merge Request Management

- List merge requests by state
- Create, view, approve, merge, close merge requests
- View MR details (description, commits, changes, comments)
- Add comments to merge requests
- Search and filter

#### Branch Management

- List all branches
- Create, delete, protect, unprotect branches
- Compare branches
- Delete merged branches
- Batch operations with checkbox selection

#### Issue Management

- List issues by state
- Create, close, reopen issues
- Assign issues to users
- Add labels
- Batch close issues
- Search and filter

#### User Management

- List all users
- Create, edit, delete users
- Block/unblock users
- Import users from Excel
- Export users to Excel
- Batch delete users
- Search users

## UI Components

### Shared Components

1. **DatabaseConnectionPanel**: Reusable database connection form
2. **ProgressDialog**: Progress tracking dialog for long-running tasks
3. **TableSelectionView**: Tree view with checkboxes for table/field selection
4. **ServerConfigDialog**: GitLab server configuration dialog
5. **DiffViewer**: Code diff viewer with syntax highlighting

### Utilities

1. **FxUtil**: JavaFX utility methods (load FXML, open modals, etc.)
2. **DialogUtil**: Dialog helpers (info, warning, error, confirm, notifications)
3. **AsyncTaskUtil**: Async task execution with JavaFX Task API

## Styling

### Themes

- **Light Theme**: Default theme with light colors
- **Dark Theme**: Dark theme for low-light environments
- **Main CSS**: Base styles for all components

### Custom Styles

- Navigation drawer
- Form layouts
- Buttons (primary, secondary, success, danger)
- Tables
- Tabs
- Cards

## Dependencies

### JavaFX Core

- javafx-controls 21.0.1
- javafx-fxml 21.0.1
- javafx-web 21.0.1

### UI Libraries

- ControlsFX 11.2.1 (enhanced controls)
- Ikonli 12.3.1 (icon library)
- AtlantaFX 2.0.1 (modern theme)
- RichTextFX 0.11.2 (code editor)

### Business Logic

- carlos-spring-boot-starter-encrypt (SM4 encryption)
- carlos-excel (Excel import/export)
- gitlab4j-api 5.4.0 (GitLab API)
- freemarker 2.3.31 (template engine)

## Testing

### Manual Testing Checklist

#### Code Generator

- [ ] Connect to MySQL database
- [ ] Load tables
- [ ] Select multiple tables
- [ ] Configure generation options
- [ ] Generate code
- [ ] Verify output files

#### Project Scaffolding

- [ ] Configure project information
- [ ] Select template
- [ ] Generate project
- [ ] Verify Maven project structure

#### Encryption Tool

- [ ] Connect to database
- [ ] Load tables and fields
- [ ] Encrypt data
- [ ] Verify encrypted data in database
- [ ] Decrypt data
- [ ] Verify original data

#### GitLab Tool

- [ ] Configure GitLab server
- [ ] List merge requests
- [ ] Create merge request
- [ ] Approve and merge MR
- [ ] List branches
- [ ] Create and delete branch
- [ ] Protect/unprotect branch
- [ ] List issues
- [ ] Create and close issue
- [ ] List users
- [ ] Create and delete user
- [ ] Import users from Excel
- [ ] Export users to Excel

## Known Issues

1. **GitLab API Token**: Currently hardcoded in GitlabMainController. Need to implement secure token storage.
2. **Project Selection**: Project combo boxes in GitLab tabs need to be populated from actual GitLab projects.
3. **Excel Import/Export**: User import/export functionality needs implementation.
4. **MR Details Dialog**: Merge request details dialog with tabs needs implementation.
5. **Compare Branches Dialog**: Branch comparison dialog needs implementation.

## Future Enhancements

1. **Settings Page**: Add settings page for theme selection, language, etc.
2. **Recent Connections**: Save and load recent database connections
3. **GitLab Server Management**: Save multiple GitLab server configurations
4. **Code Templates**: Allow users to customize code generation templates
5. **Syntax Highlighting**: Add syntax highlighting to diff viewer using RichTextFX
6. **Keyboard Shortcuts**: Add keyboard shortcuts for common operations
7. **Dark Mode Toggle**: Add theme switcher in main window
8. **Internationalization**: Add support for multiple languages

## Migration Notes

### From Swing to JavaFX

1. **Layout Management**: Swing's GroupLayout → JavaFX's GridPane, VBox, HBox
2. **Event Handling**: Swing's ActionListener → JavaFX's EventHandler
3. **Data Binding**: Manual updates → JavaFX Properties and ObservableList
4. **Threading**: SwingWorker → JavaFX Task API
5. **Dialogs**: JOptionPane → JavaFX Alert and custom dialogs

### Business Logic Preservation

All existing business logic has been preserved:

- Database services (DatabaseService)
- Code generators (Generator)
- Encryption handlers (EncryptHandler, DecryptHandler)
- GitLab services (GitlabService, MergeRequestService, etc.)

Only the UI layer has been migrated to JavaFX.

## Performance Considerations

1. **Virtual Scrolling**: TableView uses virtual scrolling for large datasets
2. **Lazy Loading**: Data is loaded on demand
3. **Async Operations**: All long-running operations use JavaFX Task API
4. **Progress Tracking**: Progress bars and indicators for user feedback
5. **Batch Processing**: Encryption tool processes 5000 records per batch

## Security Considerations

1. **Password Fields**: Use PasswordField for sensitive input
2. **Token Storage**: GitLab tokens should be stored securely (not implemented yet)
3. **SQL Injection**: Use prepared statements in database services
4. **Input Validation**: Validate all user input before processing

## Troubleshooting

### JavaFX Runtime Not Found

If you get "JavaFX runtime components are missing" error:

```bash
# Make sure JavaFX is in your classpath
mvn javafx:run
```

### FXML Load Error

If FXML files fail to load:

1. Check that FXML files are in `src/main/resources/fxml/`
2. Verify controller class names in FXML files
3. Check that all @FXML fields match FXML fx:id attributes

### CSS Not Applied

If styles are not applied:

1. Check that CSS files are in `src/main/resources/css/`
2. Verify CSS file paths in code
3. Check for CSS syntax errors

## Contributing

When adding new features:

1. Follow the existing package structure
2. Use BaseController for all controllers
3. Use AsyncTaskUtil for long-running operations
4. Use DialogUtil for user notifications
5. Add FXML files to `src/main/resources/fxml/`
6. Add styles to `src/main/resources/css/main.css`
7. Update this README with new features

## License

Internal use only - Carlos Framework
