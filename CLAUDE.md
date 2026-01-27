# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Yunjin-frame3 (renamed to carlos-spring-boot) is a Java-based microservices framework built on Spring Boot 3.5.9 (JDK 17) and Spring Cloud Alibaba. The project is organized as a multi-module Maven build with two main directories:

- **carlos-spring-boot**: The actual framework implementation - a collection of 30 reusable component modules
- **carlos-spring-boot-dependencies**: Centralized dependency management with version definitions

This is an internal framework/scaffold project designed to accelerate application development by providing pre-built integrations and utilities.

## Maven Commands

### Building the Project

```bash
# Build all modules (tests are skipped by default in pom.xml)
mvn clean install

# Build with specific profile (internal Nexus repository)
mvn clean install -P carlos-public    # Public server: 100.127.2.47:10004
mvn clean install -P carlos-private   # Private server: 192.168.1.50:10004
mvn clean install -P carlos-yanfa     # Dev server: 192.168.20.145:8081

# Build a specific module
cd carlos-spring-boot/carlos-core
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

The framework follows a parent-child Maven structure with version inheritance:

```
carlos-spring-boot/
├── pom.xml                    # Root aggregator (v2.0.0)
├── carlos-spring-boot/        # Framework implementation (30 modules)
│   ├── pom.xml               # Parent POM with dependency management
│   └── [30 component modules]
└── carlos-spring-boot-dependencies/  # Centralized dependency versions
    └── pom.xml
```

### Key Component Modules

**Core Infrastructure:**
- `yunjin-core`: Base abstractions, annotations, AOP, exceptions, pagination, response wrappers
- `yunjin-utils`: Common utilities and helper functions
- `yunjin-springboot`: Spring Boot autoconfiguration and starter support
- `yunjin-springcloud`: Spring Cloud Alibaba integrations (Nacos, Sentinel, Gateway)

**Data Access:**
- `yunjin-mybatis`: MyBatis-Plus integration with multi-datasource support (Dynamic Datasource 3.6.0)
- `yunjin-mongodb`: MongoDB integration
- `yunjin-redis`: Redis caching with Lettuce
- `yunjin-redisson`: Redisson distributed lock and cache support
- `yunjin-datascope`: Data permission/scope control

**Integration & Communication:**
- `yunjin-gateway`: API Gateway utilities for Spring Cloud Gateway
- `yunjin-openapi`: OpenAPI/Swagger documentation support (Knife4j)
- `yunjin-docking`: Third-party integration framework (DingTalk, RongZhengTong, etc.)
- `yunjin-mq`: Message queue abstractions
- `yunjin-datacenter`: Data center integration/synchronization

**Security & Authentication:**
- `yunjin-encrypt`: Encryption utilities (SM2, SM4 national cryptography algorithms)
- `yunjin-license`: TrueLicense-based software licensing system (3 sub-modules: core, generate, verify)
- `yunjin-oauth2`: OAuth2 authentication and authorization (Spring Security OAuth2 Authorization Server)

**Observability:**
- `yunjin-log`: Logging enhancements
- `yunjin-apm`: APM integration (SkyWalking 9.7.0, Sleuth)

**Utilities:**
- `yunjin-excel`: Excel import/export using Apache POI 5.2.5
- `yunjin-json`: JSON serialization (Fastjson 2.0.60)
- `yunjin-minio`: MinIO object storage integration
- `yunjin-sms`: SMS sending abstraction with multi-provider support
- `yunjin-snowflake`: Distributed ID generation
- `yunjin-tools`: GUI desktop tools (code generator, project scaffolding, Swing-based)
- `yunjin-flowable`: Flowable workflow engine integration
- `yunjin-magicapi`: Magic API integration
- `yunjin-test`: Test utilities and examples
- `yunjin-disruptor`: Disruptor high-performance queue component

### Technology Stack (from carlos-spring-boot-dependencies/pom.xml)

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

The parent POM (`carlos-spring-boot/pom.xml`) uses the `${revision}` placeholder pattern (currently `3.0.0-SNAPSHOT`) with the `flatten-maven-plugin` to manage versions centrally. All child modules inherit this version.

### Configuration Patterns

The framework uses Spring Boot's autoconfiguration mechanism. Modules typically provide:
- AutoConfiguration classes with `@Configuration` and `@ConditionalOnProperty`
- `spring.factories` or `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` files
- `@ConfigurationProperties` classes with `yunjin.*` prefix namespace

Example configuration structure (from yunjin-test):
```yaml
yunjin:
  boot:
    cors:
      allowed-origins: [...]
    enums:
      scan-package: com.yunjin
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

The `yunjin-license` module is structured for security:
- `yunjin-license-core`: Shared models and abstractions
- `yunjin-license-generate`: Certificate generation (should NOT be included in production deployments)
- `yunjin-license-verify`: Certificate verification (include this in applications)

Uses TrueLicense to validate hardware fingerprints (IP, MAC, CPU serial, mainboard serial) and time constraints.

## Development Guidelines

### Adding New Modules

1. Create module under `carlos-spring-boot/`
2. Add `<module>` entry to `carlos-spring-boot/pom.xml`
3. Set parent to `carlos-spring-boot` with version `${revision}`
4. Add dependency management entry in parent POM if the module will be consumed by others
5. Follow naming convention: `yunjin-{function}`

### Module Dependencies

- Core modules (`yunjin-core`, `yunjin-utils`) are foundational and can be used everywhere
- Spring Boot modules should depend on `yunjin-springboot`
- Spring Cloud modules should depend on `yunjin-springcloud`
- Avoid circular dependencies between modules

### Code Generation Tools

The `yunjin-tools` module provides a Swing-based GUI for:
- Database code generation (MyBatis/MongoDB/Elasticsearch from MySQL schema)
- Project scaffolding
- Encryption utilities
- GitLab integration

Run with: `ToolsApplication.start()` method

### Encryption Standards

This framework uses Chinese national cryptography standards (SM2/SM4) rather than RSA/AES in some modules. Be aware when reviewing encryption code.

### Configuration Files

Resource filtering is enabled for `application*.yml/yaml/properties` and `bootstrap*.yml` files using `@...@` delimiters for Maven property substitution.

### Testing

- Unit tests: `*Test.java` in `src/test/java` (currently skipped in build)
- Integration tests: `*IT.java` (use maven-failsafe-plugin)
- Test configuration in `yunjin-test` module demonstrates framework usage

## Important Notes

- **Not a Git Repository**: The root directory is not a git repository (only subdirectories have `.git`)
- **Multi-Profile Support**: Three Nexus repository profiles exist for different deployment environments
- **JDK 17 Required**: This is a Spring Boot 3 project requiring JDK 17+
- **Internal Use Only**: README states "严肃声明:当前脚手架代码仅限内部使用!" (Internal use only)
- **Parent Version**: Use `${revision}` in child POMs, not hardcoded versions
- **Security Note**: Never include `yunjin-license-generate` module in production artifacts
- **Directory Renaming**: The framework was renamed from `yunjin-parent` to `carlos-spring-boot`

## Recent Updates

### yunjin-oauth2 Module (Added 2026-01-25)

A comprehensive OAuth2 authentication and authorization module based on Spring Security OAuth2 Authorization Server.

**Key Features:**
- OAuth2 Authorization Server with multiple grant types (authorization_code, password, client_credentials, refresh_token)
- OAuth2 Resource Server with JWT validation
- Custom JWT token enhancement with user context (user_id, tenant_id, dept_id, role_ids, authorities)
- Integration with yunjin-core authentication system (LoginUserInfo, UserContext)
- Method-level security with @PreAuthorize annotations
- Utility class OAuth2Util for easy access to current user information

**Module Structure:**
```
yunjin-oauth2/
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
yunjin:
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
- Full documentation: `yunjin-parent/yunjin-oauth2/README.md`
- Integration summary: `yunjin-parent/yunjin-oauth2/INTEGRATION_SUMMARY.md`
- Example code: `com.yunjin.oauth2.example.*`

**Dependencies:**
- yunjin-core (user info, exceptions)
- yunjin-redis (optional, for token storage)
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

### Repository Renaming (2026-01-27)
The framework has been renamed from `yunjin-frame3` to `carlos-spring-boot`:
- `yunjin-parent/` → `carlos-spring-boot/`
- `carlos-boot-dependencies/` → `carlos-spring-boot-dependencies/`

### Version Updates
The dependency versions have been updated from those documented in README-REF.md:
- Spring Boot: 3.5.8 → **3.5.9**
- Spring Cloud: 2023.0.6 → **2025.0.1**
- Spring Cloud Alibaba: 2023.0.3.3 → **2025.0.0.0**
- MyBatis-Plus: 3.5.12 → **3.5.15**
- SkyWalking: 9.7.0 → **9.5.0**

### Module Count
The framework now contains **30 modules** (previously 29), with the addition of `yunjin-disruptor` for high-performance queue operations.

### Git Status
- Root directory: Not a git repository (subdirectories have `.git`)
- Main branch: `main` (4 commits ahead of origin)
- Recent changes: Directory renaming and POM updates
