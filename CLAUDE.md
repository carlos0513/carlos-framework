# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**Carlos Framework** is a Java-based microservices framework built on Spring Boot 3.5.9 (JDK 17) and Spring Cloud Alibaba. The project is organized as a multi-module Maven build with a clear layered architecture:

- **carlos-dependencies**: Centralized dependency version management (BOM)
- **carlos-parent**: Unified parent POM with build configuration and plugin management
- **carlos-commons**: Framework-agnostic common utilities (core, utils, excel)
- **carlos-spring-boot**: Spring Boot integration layer with 22 starter modules
- **carlos-integration**: Third-party integrations (license, tools)
- **carlos-samples**: Example applications and test modules

This is an internal framework/scaffold project designed to accelerate application development by providing pre-built integrations and utilities.

## Maven Commands

### Building the Project

```bash
# Build all modules (tests are skipped by default in pom.xml)
mvn clean install

# Build with specific profile (internal Nexus repository)
mvn clean install -P carlos-public    # Public server: zcarlos.com:8081
mvn clean install -P carlos-private   # Private server: 192.168.3.30:8081

# Build a specific module
cd carlos-commons/carlos-core
mvn clean install

# Build Spring Boot starters
cd carlos-spring-boot
mvn clean install

# Deploy to Nexus
mvn clean deploy -P carlos-public
```

### Running Tests

```bash
# Run unit tests (currently skipped in pom.xml configuration)
mvn test

# Run specific test
mvn test -Dtest=ClassName#methodName

# Run integration tests
mvn verify
```

### Dependency Management

```bash
# Check for dependency updates
mvn versions:display-dependency-updates

# Check for plugin updates
mvn versions:display-plugin-updates

# Upgrade to latest versions
mvn versions:use-latest-versions

# View effective POM
mvn help:effective-pom
```

## Architecture

### Module Structure

The framework follows a layered architecture with clear separation of concerns:

```
carlos-framework/                          # Root aggregator POM
├── carlos-dependencies/                   # Dependency version management (BOM)
├── carlos-parent/                         # Parent POM (build config, plugins)
├── carlos-commons/                        # Framework-agnostic utilities
│   ├── carlos-core/                      # Core abstractions, exceptions, pagination
│   ├── carlos-utils/                     # Common utility functions
│   └── carlos-excel/                     # Excel processing utilities
├── carlos-spring-boot/                    # Spring Boot integration layer
│   ├── carlos-spring-boot-starter-apm/
│   ├── carlos-spring-boot-starter-redis/
│   ├── carlos-spring-boot-starter-mybatis/
│   └── ... (22 starters total)
├── carlos-integration/                    # Third-party integrations
│   ├── carlos-license/                   # Software licensing (TrueLicense)
│   │   ├── carlos-license-core/
│   │   ├── carlos-spring-boot-starter-license-generate/
│   │   └── carlos-spring-boot-starter-license-verify/
│   └── carlos-tools/                     # GUI tools (code projectGeneratorService)
└── carlos-samples/                        # Examples and tests
    └── carlos-test/                      # Test application
```

**Build Order:**

1. carlos-dependencies (BOM)
2. carlos-parent (parent POM)
3. carlos-commons (3 modules)
4. carlos-spring-boot (22 modules)
5. carlos-integration (5 modules)
6. carlos-samples (1 module)

### Key Component Modules

**Commons Layer (Framework-agnostic):**

- `carlos-core`: Base abstractions, annotations, AOP, exceptions, pagination, response wrappers
- `carlos-utils`: Common utilities and helper functions (tree utils, HTTP client)
- `carlos-excel`: Excel import/export using Apache POI 5.2.5 and EasyExcel

**Spring Boot Integration Layer:**

*Core Infrastructure:*

- `carlos-spring-boot-starter-springboot`: Spring Boot autoconfiguration and starter support
- `carlos-spring-boot-starter-springcloud`: Spring Cloud Alibaba integrations (Nacos, Sentinel)
- `carlos-spring-boot-starter-gateway`: API Gateway utilities for Spring Cloud Gateway
- `carlos-spring-boot-starter-json`: JSON serialization (Fastjson 2.0.60)

*Data Access:*

- `carlos-spring-boot-starter-mybatis`: MyBatis-Plus integration with multi-datasource support
- `carlos-spring-boot-starter-mongodb`: MongoDB integration
- `carlos-spring-boot-starter-redis`: Redis + Redisson + Caffeine (unified caching solution)
- `carlos-spring-boot-starter-datascope`: Data permission/scope control

*Storage & Messaging:*

- `carlos-spring-boot-starter-minio`: MinIO object storage integration
- `carlos-spring-boot-starter-oss`: OSS (Object Storage Service) abstraction
- `carlos-spring-boot-starter-mq`: Message queue abstractions

*Security & Authentication:*

- `carlos-spring-boot-starter-encrypt`: Encryption utilities (SM2, SM4 national cryptography)
- `carlos-spring-boot-starter-oauth2`: OAuth2 authentication and authorization

*Integration:*

- `carlos-spring-boot-starter-docking`: Third-party integration framework (DingTalk, RongZhengTong)
- `carlos-spring-boot-starter-datacenter`: Data center integration/synchronization
- `carlos-spring-boot-starter-sms`: SMS sending abstraction with multi-provider support

*Observability:*

- `carlos-spring-boot-starter-log`: Logging enhancements
- `carlos-spring-boot-starter-apm`: APM integration (SkyWalking 9.5.0)

*Utilities:*

- `carlos-spring-boot-starter-openapi`: OpenAPI/Swagger documentation support (Knife4j)
- `carlos-spring-boot-starter-snowflake`: Distributed ID generation
- `carlos-spring-boot-starter-flowable`: Flowable workflow engine integration
- `carlos-spring-boot-starter-disruptor`: Disruptor high-performance queue component

**Integration Layer:**

- `carlos-license`: TrueLicense-based software licensing system (3 sub-modules)
    - `carlos-license-core`: Core licensing functionality
    - `carlos-spring-boot-starter-license-generate`: License generation (dev only)
    - `carlos-spring-boot-starter-license-verify`: License verification (production)
- `carlos-tools`: GUI desktop tools (code projectGeneratorService, project scaffolding, Swing-based)

**Samples:**

- `carlos-test`: Test application demonstrating framework usage

### Technology Stack (from carlos-dependencies/pom.xml)

| Component | Version | Notes |
|-----------|---------|-------|
| Spring Boot | 3.5.9 | |
| Spring Cloud | 2025.0.1 | |
| Spring Cloud Alibaba | 2025.0.0.0 | Nacos, Sentinel, Seata |
| JDK | 17 | Minimum required version |
| Maven | 3.8+ | Minimum required version |
| MyBatis-Plus | 3.5.15 | With Join extension 1.5.4 |
| Seata | 2.0.0 | Distributed transactions |
| Hutool | 5.8.40 | Utility library |
| MapStruct | 1.6.3 | Bean mapping |
| Guava | 33.4.8-jre | |
| Druid | 1.2.27 | Database connection pool |
| SkyWalking | 9.5.0 | APM tracing |

### Dependency Version Management

The framework uses a centralized dependency management approach:

1. **carlos-dependencies**: Defines all dependency versions using `<dependencyManagement>`
2. **carlos-parent**: Imports carlos-dependencies BOM and provides build configuration
3. **All modules**: Inherit from carlos-parent and reference dependencies without versions

The `${revision}` placeholder pattern (currently `3.0.0-SNAPSHOT`) with the `flatten-maven-plugin` manages versions centrally. All child modules inherit this version.

### Configuration Patterns

The framework uses Spring Boot's autoconfiguration mechanism. Modules typically provide:
- AutoConfiguration classes with `@Configuration` and `@ConditionalOnProperty`
- `spring.factories` or `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` files
- `@ConfigurationProperties` classes with `carlos.*` prefix namespace

Example configuration structure (from carlos-test):
```yaml
carlos:
  boot:
    cors:
      allowed-origins: [...]
    enums:
      scan-package: com.carlos
      enabled: true
  encrypt:
    sm4:
      enabled: true
      encrypt-mode: cbc
  docking:
    dingtalk:
      enabled: ${DINGTALK_ENABLE:false}
    rzt:
      enabled: ${RZT_ENABLE:true}
```

### Licensing Module Architecture

The `carlos-license` module (located in `carlos-integration/`) is structured for security:

- `carlos-license-core`: Shared models and abstractions
- `carlos-spring-boot-starter-license-generate`: Certificate generation (should NOT be included in production deployments)
- `carlos-spring-boot-starter-license-verify`: Certificate verification (include this in applications)

Uses TrueLicense to validate hardware fingerprints (IP, MAC, CPU serial, mainboard serial) and time constraints.

## Development Guidelines

### Adding New Modules

**For Spring Boot Starters:**
1. Create module under `carlos-spring-boot/`
2. Add `<module>` entry to `carlos-spring-boot/pom.xml`
3. Set parent to `carlos-spring-boot` with version `${revision}`
4. Follow naming convention: `carlos-spring-boot-starter-{function}`
5. Add dependency management entry in `carlos-dependencies/pom.xml`

**For Common Utilities:**

1. Create module under `carlos-commons/`
2. Add `<module>` entry to `carlos-commons/pom.xml`
3. Set parent to `carlos-commons` with version `${revision}`
4. Follow naming convention: `carlos-{function}`
5. Ensure no Spring Boot dependencies (keep it framework-agnostic)

**For Third-party Integrations:**

1. Create module under `carlos-integration/`
2. Add `<module>` entry to `carlos-integration/pom.xml`
3. Set parent to `carlos-parent` with version `${revision}`

### Module Dependencies

**Dependency Rules:**

- Commons modules (`carlos-core`, `carlos-utils`, `carlos-excel`) are foundational and framework-agnostic
- Spring Boot starters can depend on commons modules
- Spring Boot starters should depend on `carlos-spring-boot-starter-springboot` for base configuration
- Spring Cloud starters should depend on `carlos-spring-boot-starter-springcloud`
- Integration modules can depend on both commons and Spring Boot starters
- Avoid circular dependencies between modules

**Layering:**

```
carlos-samples (test apps)
    ↓
carlos-integration (license, tools)
    ↓
carlos-spring-boot (22 starters)
    ↓
carlos-commons (core, utils, excel)
    ↓
carlos-parent (build config)
    ↓
carlos-dependencies (BOM)
```

### Code Generation Tools

The `carlos-tools` module (located in `carlos-integration/`) provides a Swing-based GUI for:
- Database code generation (MyBatis/MongoDB/Elasticsearch from MySQL schema)
- Project scaffolding
- Encryption utilities
- GitLab integration

Run with: `com.carlos.fx.ToolsApplication.main()` method

### Encryption Standards

This framework uses Chinese national cryptography standards (SM2/SM4) rather than RSA/AES in some modules. Be aware when reviewing encryption code.

### Configuration Files

Resource filtering is enabled for `application*.yml/yaml/properties` and `bootstrap*.yml` files using `@...@` delimiters for Maven property substitution.

### Testing

- Unit tests: `*Test.java` in `src/test/java` (currently skipped in build)
- Integration tests: `*IT.java` (use maven-failsafe-plugin)
- Test configuration in `carlos-samples/carlos-test` module demonstrates framework usage

## Important Notes

- **Git Repository**: The root directory is a git repository
- **Multi-Profile Support**: Two Nexus repository profiles exist for different deployment environments
    - `carlos-public`: Public server at zcarlos.com:8081
    - `carlos-private`: Private server at 192.168.3.30:8081
- **JDK 17 Required**: This is a Spring Boot 3 project requiring JDK 17+
- **Maven 3.8+ Required**: Minimum Maven version is 3.8
- **Internal Use Only**: This framework is for internal use only
- **Parent Version**: Use `${revision}` in child POMs, not hardcoded versions
- **Security Note**: Never include `carlos-spring-boot-starter-license-generate` module in production artifacts
- **Layered Architecture**: The framework follows a clear layered architecture (commons → spring-boot → integration → samples)

## Recent Updates

### carlos-oauth2 Module (Added 2026-01-25)

A comprehensive OAuth2 authentication and authorization module based on Spring Security OAuth2 Authorization Server.

**Key Features:**
- OAuth2 Authorization Server with multiple grant types (authorization_code, password, client_credentials, refresh_token)
- OAuth2 Resource Server with JWT validation
- Custom JWT token enhancement with user context (user_id, tenant_id, dept_id, role_ids, authorities)
- Integration with carlos-core authentication system (LoginUserInfo, UserContext)
- Method-level security with @PreAuthorize annotations
- Utility class OAuth2Util for easy access to current user information

**Module Structure:**
```
carlos-oauth2/
├── config/                    # OAuth2 configuration classes
│   ├── OAuth2Properties.java
│   ├── OAuth2AuthorizationServerConfig.java
│   └── OAuth2ResourceServerConfig.java
├── constant/                  # OAuth2 constants
├── enhancer/                  # JWT token enhancer
├── exception/                 # OAuth2 exceptions
├── service/                   # User details service
├── util/                      # OAuth2 utilities
└── example/                   # Usage examples
```

**Configuration Example:**
```yaml
carlos:
  oauth2:
    enabled: true
    authorization-server:
      enabled: true
      access-token-time-to-live: 2h
      refresh-token-time-to-live: 7d
    jwt:
      issuer: http://localhost:8080
      include-user-info: true
    clients:
      - client-id: my-client
        client-secret: my-secret
        authorization-grant-types: [authorization_code, refresh_token, password]
        redirect-uris: [http://localhost:8080/authorized]
        scopes: [read, write]
```

**Usage:**
```java
// Get current user information
UserContext userContext = OAuth2Util.extractUserContext();
Long userId = OAuth2Util.getCurrentUserId();
String userName = OAuth2Util.getCurrentUserName();

// Method-level security
@PreAuthorize("hasRole('ADMIN')")
public void adminOnlyMethod() { }
```

**OAuth2 Endpoints:**
- `POST /oauth2/token` - Get access token
- `POST /oauth2/revoke` - Revoke token
- `POST /oauth2/introspect` - Token introspection
- `GET /oauth2/jwks` - JWK Set
- `GET /oauth2/authorize` - Authorization endpoint

**Documentation:**

- Full documentation: `carlos-spring-boot/carlos-spring-boot-starter-oauth2/README.md`
- Integration summary: `carlos-spring-boot/carlos-spring-boot-starter-oauth2/INTEGRATION_SUMMARY.md`
- Example code: `com.carlos.oauth2.example.*`

**Dependencies:**

- carlos-core (user info, exceptions)
- carlos-redis (optional, for token storage)
- spring-boot-starter-security
- spring-security-oauth2-authorization-server
- spring-boot-starter-oauth2-resource-server
- spring-security-oauth2-jose

**Important Notes:**
- Default `DefaultOAuth2UserDetailsService` is for testing only
- Production environments must implement custom `OAuth2UserDetailsService`
- Password grant type is deprecated in Spring Security OAuth2
- Current implementation uses in-memory token storage (consider Redis for production)
- JWT tokens include custom claims: user_id, user_name, tenant_id, dept_id, role_ids, authorities

## Current State and Recent Changes

### Architecture Refactoring (2026-02-01)

The framework underwent a major architecture refactoring to improve clarity and maintainability:

**Directory Structure Changes:**

- `carlos-spring-boot-framework` → `carlos-framework` (root aggregator)
- `carlos-spring-boot-dependencies` → `carlos-dependencies` (BOM)
- `carlos-spring-boot-parent` → `carlos-parent` (parent POM)
- `carlos-spring-boot-commons` → `carlos-commons` (framework-agnostic utilities)
- `carlos-spring-boot-starters` → `carlos-spring-boot` (Spring Boot integration)
- Created `carlos-integration/` for third-party integrations (license, tools)
- Created `carlos-samples/` for examples and tests

**Benefits:**

- Clear separation between framework-agnostic utilities and Spring Boot integrations
- Better naming that doesn't imply everything is Spring Boot-specific
- Improved extensibility for future framework integrations (e.g., Spring Cloud, Quarkus)
- Follows industry best practices (similar to Spring Framework, Apache Commons)

### Module Consolidation (2026-02-01)

**Redis Module Enhancement:**

- Merged `carlos-spring-boot-starter-redisson` into `carlos-spring-boot-starter-redis`
- Added Caffeine local cache integration
- Added multi-level cache support (Caffeine L1 + Redis L2)
- Unified caching solution: Redis + Redisson + Caffeine in one module

**Removed Modules:**

- `carlos-magicapi`: Removed (no longer needed)

### Version Updates

Current versions:

- Spring Boot: **3.5.9**
- Spring Cloud: **2025.0.1**
- Spring Cloud Alibaba: **2025.0.0.0**
- MyBatis-Plus: **3.5.15**
- SkyWalking: **9.5.0**
- Hutool: **5.8.40**
- MapStruct: **1.6.3**
- Guava: **33.4.8-jre**
- Redisson: **3.51.0**

### Module Count

The framework now contains **38 modules** organized in a layered architecture:

- 1 root aggregator (carlos-framework)
- 1 BOM (carlos-dependencies)
- 1 parent POM (carlos-parent)
- 4 commons modules (carlos-commons + 3 sub-modules)
- 23 Spring Boot modules (carlos-spring-boot + 22 starters)
- 6 integration modules (carlos-integration + 5 sub-modules)
- 2 sample modules (carlos-samples + 1 test app)

### Git Status

- Root directory: Git repository
- Current branch: `refactor-yunjin-to-carlos`
- Recent changes: Architecture refactoring, module consolidation, directory reorganization
