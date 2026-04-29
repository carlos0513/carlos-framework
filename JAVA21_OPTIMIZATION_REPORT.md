# Carlos Framework Java 21 新特性优化报告

> **扫描时间**: 2026-04-29  
> **扫描范围**: `carlos-spring-boot`, `carlos-integration`, `carlos-commons`, `carlos-samples`  
> **扫描文件数**: 2,329 个 Java 文件  
> **JDK 目标版本**: Java 21 (LTS)

---

## 目录

- [优化概览](#优化概览)
- [一、Pattern Matching for instanceof / switch](#一pattern-matching-for-instanceof--switch)
- [二、Virtual Threads 虚拟线程](#二virtual-threads-虚拟线程)
- [三、Text Blocks / String.formatted / 日志占位符](#三text-blocks--stringformatted--日志占位符)
- [四、Sequenced Collections / 不可变集合](#四sequenced-collections--不可变集合)
- [五、Record 类重构评估](#五record-类重构评估)
- [六、推荐实施计划](#六推荐实施计划)

---

## 优化概览

| 优化类别                                       | 发现数量      | 风险等级  | 整体优先级      |
|--------------------------------------------|-----------|-------|------------|
| Pattern Matching (`instanceof` / `switch`) | **~50 处** | ⭐ 极低  | 🔴 **高**   |
| Virtual Threads（虚拟线程）                      | **~19 处** | ⭐⭐ 中  | 🔴 **高**   |
| Text Blocks / `String.formatted`           | **~40 处** | ⭐ 极低  | 🟡 **中**   |
| Sequenced Collections / 不可变集合              | **~30 处** | ⭐ 极低  | 🟢 **低**   |
| Record 类重构                                 | **极少数**   | ⭐⭐⭐ 高 | ⚪ **暂不推荐** |

> **说明**: 本项目当前已使用 Java 15+ 的部分特性（如 `instanceof Type var` 在 `EmptyStringPropertyFilter.java`、
`ApplicationConverterConfig.java` 中已有良好实践），值得继续保持并推广。

---

## 一、Pattern Matching for instanceof / switch

**优先级**: 🔴 **高** | **风险**: ⭐ 极低

此类优化属于语法糖升级，**编译器层面保证类型安全**，零运行时风险，且能显著减少代码冗余、提升可读性。建议优先批量处理。

Java 21 已正式标准化：

- **JEP 394**: Pattern Matching for `instanceof` (since Java 16，最终版)
- **JEP 441**: Pattern Matching for `switch` (since Java 21，正式版)

---

### 1.1 `instanceof` + 强制转换 → 模式匹配

**发现数量**: ~20 处

**优化原理**: 将 `if (obj instanceof Type) { Type var = (Type) obj; ... }` 合并为 `if (obj instanceof Type var) { ... }`
，消除强制转换，减少重复代码，避免 `ClassCastException` 风险。

#### 详细优化点

##### 1.1.1 `PageInfo.java`

- **文件**:
  `carlos-spring-boot/carlos-spring-boot-starter-mybatis/src/main/java/com/carlos/datasource/pagination/PageInfo.java`
- **行号**: 142-143
- **当前代码**:

```java
if (paramPage instanceof ParamPageOrder) {
    ParamPageOrder pageOrder = (ParamPageOrder) paramPage;
```

- **优化后**:

```java
if (paramPage instanceof ParamPageOrder pageOrder) {
```

##### 1.1.2 `FeignClientErrorDecoder.java`

- **文件**:
  `carlos-spring-boot/carlos-spring-cloud-starter/src/main/java/com/carlos/cloud/feign/FeignClientErrorDecoder.java`
- **行号**: 37-42
- **当前代码**:

```java
if (!(exception instanceof FeignException)) {
    return null;
}
final ByteBuffer responseBody = ((FeignException) exception).responseBody().get();
```

- **优化后**:

```java
if (!(exception instanceof FeignException feignException)) {
    return null;
}
final ByteBuffer responseBody = feignException.responseBody().get();
```

##### 1.1.3 `MyBatisDataScopeInterceptor.java`（3 处）

- **文件**:
  `carlos-spring-boot/carlos-spring-boot-starter-datascope/src/main/java/com/carlos/datascope/interceptor/MyBatisDataScopeInterceptor.java`

**第 1 处 - 行号 98-99**:

```java
// 当前
if (statement instanceof Select) {
    Select select = (Select) statement;
// 优化后
if (statement instanceof Select select) {
```

**第 2 处 - 行号 112-113**:

```java
// 当前
if (select instanceof PlainSelect) {
    PlainSelect plainSelect = (PlainSelect) select;
// 优化后
if (select instanceof PlainSelect plainSelect) {
```

**第 3 处 - 行号 124-125**:

```java
// 当前
} else if (select instanceof SetOperationList) {
    SetOperationList setOpList = (SetOperationList) select;
// 优化后
} else if (select instanceof SetOperationList setOpList) {
```

##### 1.1.4 `RequestBuilder.java`（3 处重复模式）

- **文件**:
  `carlos-spring-boot/carlos-spring-boot-starter-integration/src/main/java/com/carlos/integration/core/client/RequestBuilder.java`
- **行号**: 137-139, 159-161, 181-183
- **当前代码**:

```java
if (body != null && spec instanceof RestClient.RequestBodySpec) {
    ((RestClient.RequestBodySpec) spec).body(body);
}
```

- **优化后**:

```java
if (body != null && spec instanceof RestClient.RequestBodySpec bodySpec) {
    bodySpec.body(body);
}
```

##### 1.1.5 `DockingClientRegistry.java`（2 处重复）

- **文件**:
  `carlos-spring-boot/carlos-spring-boot-starter-integration/src/main/java/com/carlos/integration/core/client/DockingClientRegistry.java`
- **行号**: 59-61, 66-68
- **当前代码**:

```java
if (beanFactory instanceof BeanDefinitionRegistry) {
    this.beanRegistry = (BeanDefinitionRegistry) beanFactory;
}
```

- **优化后**:

```java
if (beanFactory instanceof BeanDefinitionRegistry registry) {
    this.beanRegistry = registry;
}
```

##### 1.1.6 `CustomizeErrorAttributes.java`

- **文件**:
  `carlos-spring-boot/carlos-spring-boot-starter-web/src/main/java/com/carlos/boot/error/CustomizeErrorAttributes.java`
- **行号**: 24-25
- **当前代码**:

```java
if (error instanceof GlobalException) {
    errorAttributes.put("code", ((GlobalException) error).getErrorCode());
```

- **优化后**:

```java
if (error instanceof GlobalException globalEx) {
    errorAttributes.put("code", globalEx.getErrorCode());
```

##### 1.1.7 `ResourceServerAutoConfiguration.java`

- **文件**:
  `carlos-spring-boot/carlos-spring-boot-starter-security/src/main/java/com/carlos/security/config/ResourceServerAutoConfiguration.java`
- **行号**: 161-164
- **当前代码**:

```java
if (permissionProvider instanceof CachedPermissionProvider) {
    syncManager.setCachedProviders(
        Collections.singletonList((CachedPermissionProvider) permissionProvider)
    );
```

- **优化后**:

```java
if (permissionProvider instanceof CachedPermissionProvider cached) {
    syncManager.setCachedProviders(Collections.singletonList(cached));
```

##### 1.1.8 `CarlosPermissionEvaluator.java`

- **文件**:
  `carlos-spring-boot/carlos-spring-boot-starter-security/src/main/java/com/carlos/security/permission/evaluator/CarlosPermissionEvaluator.java`
- **行号**: 68-73
- **当前代码**:

```java
if (!(principal instanceof UserContext)) {
    log.debug("Permission denied: principal is not UserContext");
    return false;
}
UserContext userContext = (UserContext) principal;
```

- **优化后**:

```java
if (!(principal instanceof UserContext userContext)) {
    log.debug("Permission denied: principal is not UserContext");
    return false;
}
// 注意：Java 21 中，模式匹配变量在逻辑非 guard 后的作用域需注意
// 若后续仍需 userContext，需将逻辑调整为正向判断
```

> ⚠️ **注意**: `if (!(obj instanceof Type var))` 中，`var` 的作用域在 `if` 块内部不可用。若原逻辑后续依赖 `userContext`
> ，建议改为正向判断或调整代码结构。

##### 1.1.9 `PermissionService.java`（2 处）

- **文件**:
  `carlos-spring-boot/carlos-spring-boot-starter-security/src/main/java/com/carlos/security/permission/PermissionService.java`

**第 1 处 - 行号 227-230**:

```java
// 当前
if (permissionProvider instanceof CachedPermissionProvider) {
    ((CachedPermissionProvider) permissionProvider).clearAllLocalCache();
// 优化后
if (permissionProvider instanceof CachedPermissionProvider cached) {
    cached.clearAllLocalCache();
```

**第 2 处 - 行号 239-241**:

```java
// 当前
if (permissionProvider instanceof CachedPermissionProvider) {
    return ((CachedPermissionProvider) permissionProvider).getStatsMap();
// 优化后
if (permissionProvider instanceof CachedPermissionProvider cached) {
    return cached.getStatsMap();
```

##### 1.1.10 `ExtendAuthenticationConverter.java`

- **文件**:
  `carlos-integration/carlos-auth/carlos-auth-service/src/main/java/com/carlos/auth/security/ext/ExtendAuthenticationConverter.java`
- **行号**: 105-110
- **当前代码**:

```java
if (clientPrincipal == null || !(clientPrincipal instanceof OAuth2ClientAuthenticationToken)) {
    ...
}
OAuth2ClientAuthenticationToken clientAuth = (OAuth2ClientAuthenticationToken) clientPrincipal;
```

- **优化后**:

```java
if (clientPrincipal == null || !(clientPrincipal instanceof OAuth2ClientAuthenticationToken clientAuth)) {
    ...
}
```

##### 1.1.11 `BaseAuthenticationProvider.java`（3 处）

- **文件**:
  `carlos-integration/carlos-auth/carlos-auth-service/src/main/java/com/carlos/auth/oauth2/grant/BaseAuthenticationProvider.java`

**第 1 处 - 行号 164-168**:

```java
// 当前
if (generatedAccessToken instanceof ClaimAccessor) {
    authorizationBuilder.id(accessToken.getTokenValue())
            .token(accessToken,
                    (metadata) -> metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME,
                            ((ClaimAccessor) generatedAccessToken).getClaims()))
// 优化后
if (generatedAccessToken instanceof ClaimAccessor claimAccessor) {
    authorizationBuilder.id(accessToken.getTokenValue())
            .token(accessToken,
                    (metadata) -> metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME,
                            claimAccessor.getClaims()))
```

**第 2 处 - 行号 188-193**:

```java
// 当前
if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
    ...
}
refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
// 优化后
if (!(generatedRefreshToken instanceof OAuth2RefreshToken refreshToken)) {
    ...
}
```

**第 3 处 - 行号 256-257**:

```java
// 当前
if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
    clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
}
// 优化后
if (authentication.getPrincipal() instanceof OAuth2ClientAuthenticationToken token) {
    clientPrincipal = token;
}
```

##### 1.1.12 `UserAuthController.java`

- **文件**:
  `carlos-integration/carlos-auth/carlos-auth-service/src/main/java/com/carlos/auth/login/UserAuthController.java`
- **行号**: 214-215
- **当前代码**:

```java
if (principal instanceof UserDetails) {
    return Result.success((UserDetails) principal);
}
```

- **优化后**:

```java
if (principal instanceof UserDetails userDetails) {
    return Result.success(userDetails);
}
```

##### 1.1.13 `WhitelistContext.java`

- **文件**: `carlos-integration/carlos-gateway/src/main/java/com/carlos/gateway/whitelist/WhitelistContext.java`
- **行号**: 38-42
- **当前代码**:

```java
if (result instanceof WhitelistCheckResult) {
    return Optional.of((WhitelistCheckResult) result);
}
```

- **优化后**:

```java
if (result instanceof WhitelistCheckResult wcr) {
    return Optional.of(wcr);
}
```

##### 1.1.14 `DisruptorTemplate.java`

- **文件**:
  `carlos-spring-boot/carlos-spring-boot-starter-disruptor/src/main/java/com/carlos/disruptor/core/DisruptorTemplate.java`
- **行号**: 55-57
- **当前代码**:

```java
if (data instanceof DisruptorEvent) {
    @SuppressWarnings("unchecked")
    DisruptorEvent<T> srcEvent = (DisruptorEvent<T>) data;
```

- **优化后**:

```java
if (data instanceof DisruptorEvent<?> srcEvent) {
    // 如需要保留泛型 T，可能需要额外处理
```

##### 1.1.15 `BatchServiceImpl.java`

- **文件**:
  `carlos-spring-boot/carlos-spring-boot-starter-mybatis/src/main/java/com/carlos/datasource/base/BatchServiceImpl.java`
- **行号**: 57-63
- **当前代码**:

```java
if (id instanceof Number) {
    return ((Number) id).longValue() == 0;
}
if (id instanceof String) {
    return ((String) id).isEmpty();
}
```

- **优化后**:

```java
if (id instanceof Number num) {
    return num.longValue() == 0;
}
if (id instanceof String str) {
    return str.isEmpty();
}
```

##### 1.1.16 `KeyPairManager.java`

- **文件**:
  `carlos-integration/carlos-auth/carlos-auth-service/src/main/java/com/carlos/auth/security/manager/KeyPairManager.java`
- **行号**: 141-145
- **当前代码**:

```java
if (key instanceof PrivateKey) {
    Certificate cert = keyStore.getCertificate(keyAlias);
    PublicKey publicKey = cert.getPublicKey();
    return new KeyPair(publicKey, (PrivateKey) key);
```

- **优化后**:

```java
if (key instanceof PrivateKey privateKey) {
    Certificate cert = keyStore.getCertificate(keyAlias);
    PublicKey publicKey = cert.getPublicKey();
    return new KeyPair(publicKey, privateKey);
```

##### 1.1.17 `ResponseWrapperAdvice.java`

- **文件**:
  `carlos-spring-boot/carlos-spring-boot-starter-web/src/main/java/com/carlos/boot/response/ResponseWrapperAdvice.java`
- **行号**: 111-112
- **当前代码**:

```java
assert body instanceof Result<?>;
RequestUtil.printResponseInfo((Result<?>) body);
```

- **优化后**:

```java
if (body instanceof Result<?> result) {
    RequestUtil.printResponseInfo(result);
}
```

##### 1.1.18 `GlobalParamHttpServletRequestWrapper.java`

- **文件**:
  `carlos-spring-boot/carlos-spring-boot-starter-web/src/main/java/com/carlos/boot/filters/GlobalParamHttpServletRequestWrapper.java`
- **行号**: 66-69
- **当前代码**:

```java
if (value instanceof String[]) {
    params.put(name, (String[]) value);
} else if (value instanceof String) {
    params.put(name, new String[]{(String) value});
```

- **优化后**:

```java
if (value instanceof String[] arr) {
    params.put(name, arr);
} else if (value instanceof String str) {
    params.put(name, new String[]{str});
```

##### 1.1.19 `XssHttpServletRequestWrapper.java`

- **文件**:
  `carlos-spring-boot/carlos-spring-boot-starter-web/src/main/java/com/carlos/boot/filters/xss/XssHttpServletRequestWrapper.java`
- **行号**: 65-66
- **当前代码**:

```java
if (value instanceof String && StrUtil.isNotBlank((String) value)) {
    return cleanXSS((String) value);
```

- **优化后**:

```java
if (value instanceof String str && StrUtil.isNotBlank(str)) {
    return cleanXSS(str);
```

---

### 1.2 `if-else if` 类型判断链 → Pattern Matching for switch

**发现数量**: ~8 处

**优化原理**: 将多个 `if (obj instanceof TypeA) { ... } else if (obj instanceof TypeB) { ... }` 重构为
`switch (obj) { case TypeA a -> ...; case TypeB b -> ...; }`，代码更紧凑、分支更清晰，且 `switch` 表达式可直接返回值。

#### 详细优化点

##### 1.2.1 `GlobalExceptionHandler.java`

- **文件**:
  `carlos-spring-boot/carlos-spring-boot-starter-web/src/main/java/com/carlos/boot/GlobalExceptionHandler.java`
- **行号**: 194-203
- **当前代码**:

```java
ErrorCode errorCode;
if (exception instanceof BusinessException) {
    errorCode = CommonErrorCode.BUSINESS_ERROR;
} else if (exception instanceof DaoException) {
    errorCode = CommonErrorCode.DATABASE_ERROR;
} else if (exception instanceof RestException) {
    errorCode = CommonErrorCode.BAD_REQUEST;
} else {
    errorCode = CommonErrorCode.INTERNAL_ERROR;
}
```

- **优化后**:

```java
ErrorCode errorCode = switch (exception) {
    case BusinessException b  -> CommonErrorCode.BUSINESS_ERROR;
    case DaoException d       -> CommonErrorCode.DATABASE_ERROR;
    case RestException r      -> CommonErrorCode.BAD_REQUEST;
    default                   -> CommonErrorCode.INTERNAL_ERROR;
};
```

##### 1.2.2 `JacksonUtil.java`（2 处）

- **文件**: `carlos-spring-boot/carlos-spring-boot-starter-json/src/main/java/com/carlos/json/jackson/JacksonUtil.java`

**第 1 处 - 行号 66-72**:

```java
// 当前
if (object instanceof String) {
    array.set(i, ((String) object).trim());
} else if (object instanceof JSONObject) {
    jsonTrim((JSONObject) object);
} else if (object instanceof JSONArray) {
    jsonTrimArray((JSONArray) object);
}
// 优化后
switch (object) {
    case String s       -> array.set(i, s.trim());
    case JSONObject j   -> jsonTrim(j);
    case JSONArray a    -> jsonTrimArray(a);
    default             -> {}
}
```

**第 2 处 - 行号 91-98**（结构同上，针对 `value` 变量）

##### 1.2.3 `GatewayExceptionHandler.java`（2 处）

- **文件**: `carlos-integration/carlos-gateway/src/main/java/com/carlos/gateway/config/GatewayExceptionHandler.java`

**第 1 处 - 行号 101-117**:

```java
// 当前：7 个异常类型判断
if (ex instanceof GatewayException) {
    handleGatewayException((GatewayException) ex, builder);
} else if (ex instanceof GlobalException) {
    handleGlobalException((GlobalException) ex, builder);
} else if (ex instanceof NotFoundException) {
    ...
} else if (ex instanceof ResponseStatusException) {
    ...
} else if (ex instanceof WebClientResponseException) {
    ...
} else if (ex instanceof TimeoutException) {
    ...
} else if (ex instanceof ConnectException) {
    ...
} else {
    handleGenericException(ex, builder);
}
// 优化后
switch (ex) {
    case GatewayException g           -> handleGatewayException(g, builder);
    case GlobalException ge           -> handleGlobalException(ge, builder);
    case NotFoundException n          -> handleNotFoundException(n, builder);
    case ResponseStatusException r    -> handleResponseStatusException(r, builder);
    case WebClientResponseException w -> handleWebClientResponseException(w, builder);
    case TimeoutException t           -> handleTimeoutException(t, builder);
    case ConnectException c           -> handleConnectException(c, builder);
    default                           -> handleGenericException(ex, builder);
}
```

**第 2 处 - 行号 140-171**（`GatewayException` 子类分发，共 8 个子类判断）：
同样建议改为 `switch (ex) { case RateLimitException r -> ...; case CircuitBreakerException c -> ...; }`

##### 1.2.4 `GatewayErrorAttributes.java`（2 处）

- **文件**: `carlos-integration/carlos-gateway/src/main/java/com/carlos/gateway/config/GatewayErrorAttributes.java`

**第 1 处 - 行号 50-58**:

```java
// 当前
if (error instanceof GatewayException) {
    populateGatewayExceptionAttributes(errorAttributes, (GatewayException) error);
} else if (error instanceof GlobalException) {
    populateGlobalExceptionAttributes(errorAttributes, (GlobalException) error);
} else if (error instanceof ResponseStatusException) {
    populateResponseStatusExceptionAttributes(errorAttributes, (ResponseStatusException) error);
} else {
    populateDefaultAttributes(errorAttributes, error, options);
}
// 优化后
switch (error) {
    case GatewayException g        -> populateGatewayExceptionAttributes(errorAttributes, g);
    case GlobalException ge        -> populateGlobalExceptionAttributes(errorAttributes, ge);
    case ResponseStatusException r -> populateResponseStatusExceptionAttributes(errorAttributes, r);
    default                        -> populateDefaultAttributes(errorAttributes, error, options);
}
```

**第 2 处 - 行号 84-113**：`GatewayException` 子类的 `if-else if` 链，与 `GatewayExceptionHandler` 类似。

##### 1.2.5 `BaseAuthenticationProvider.java`

- **文件**:
  `carlos-integration/carlos-auth/carlos-auth-service/src/main/java/com/carlos/auth/oauth2/grant/BaseAuthenticationProvider.java`
- **行号**: 223-248
- **当前代码**: 8 个连续的
  `if (authenticationException instanceof Xxx) { return new OAuth2AuthenticationException(...); }`
- **优化后**:

```java
return switch (authenticationException) {
    case UsernameNotFoundException e   -> new OAuth2AuthenticationException(OAuth2ErrorCodesExpand.USERNAME_NOT_FOUND.toOAuth2Error());
    case BadCredentialsException e     -> new OAuth2AuthenticationException(OAuth2ErrorCodesExpand.BAD_CREDENTIALS.toOAuth2Error());
    case LockedException e             -> new OAuth2AuthenticationException(OAuth2ErrorCodesExpand.USER_LOCKED.toOAuth2Error());
    case DisabledException e           -> new OAuth2AuthenticationException(OAuth2ErrorCodesExpand.USER_DISABLE.toOAuth2Error());
    case AccountExpiredException e     -> new OAuth2AuthenticationException(OAuth2ErrorCodesExpand.USER_EXPIRED.toOAuth2Error());
    case CredentialsExpiredException e -> new OAuth2AuthenticationException(OAuth2ErrorCodesExpand.CREDENTIALS_EXPIRED.toOAuth2Error());
    case VerificationCodeException e   -> new OAuth2AuthenticationException(OAuth2ErrorCodesExpand.VERIFICATION_CODE_ERROR.toOAuth2Error());
    case UserNotFoundException e       -> new OAuth2AuthenticationException(OAuth2ErrorCodesExpand.USER_NOT_FOUND.toOAuth2Error());
    default                            -> new OAuth2AuthenticationException(OAuth2ErrorCodesExpand.UN_KNOW_LOGIN_ERROR.toOAuth2Error());
};
```

##### 1.2.6 `DingtalkService.java`

- **文件**:
  `carlos-spring-boot/carlos-spring-boot-starter-integration/src/main/java/com/carlos/integration/module/dingtalk/service/DingtalkService.java`
- **行号**: 390-421
- **当前代码**: 9 个消息类型判断

```java
if (msg instanceof TextMsg) {
    reqMsg.setMsgtype("text");
    reqMsg.setText(BeanUtil.copyProperties(msg, OapiMessageCorpconversationAsyncsendV2Request.Text.class));
}
if (msg instanceof FileMsg) { ... }
// ... 共 9 个
```

- **优化后**:

```java
switch (msg) {
    case TextMsg t      -> { reqMsg.setMsgtype("text"); reqMsg.setText(BeanUtil.copyProperties(t, ...)); }
    case FileMsg f      -> { reqMsg.setMsgtype("file"); reqMsg.setFile(BeanUtil.copyProperties(f, ...)); }
    case LinkMsg l      -> { reqMsg.setMsgtype("link"); reqMsg.setLink(BeanUtil.copyProperties(l, ...)); }
    case ImageMsg i     -> { reqMsg.setMsgtype("image"); reqMsg.setImage(BeanUtil.copyProperties(i, ...)); }
    case VoiceMsg v     -> { reqMsg.setMsgtype("voice"); reqMsg.setVoice(BeanUtil.copyProperties(v, ...)); }
    case MarkdownMsg m  -> { reqMsg.setMsgtype("markdown"); reqMsg.setMarkdown(BeanUtil.copyProperties(m, ...)); }
    case ActionCardMsg a-> { reqMsg.setMsgtype("action_card"); reqMsg.setActionCard(BeanUtil.copyProperties(a, ...)); }
    case OAMsg o        -> { reqMsg.setMsgtype("oa"); reqMsg.setOa(BeanUtil.copyProperties(o, ...)); }
    default             -> {}
}
```

---

### 1.3 传统 `switch` → Arrow Syntax Switch 表达式

**发现数量**: ~12 处

**优化原理**: 将带 `break` 的传统 `switch` 改为 arrow syntax（`->`）的 switch 表达式，可消除 `break` 穿透风险，支持多 case
合并，且可直接作为表达式返回值。

#### 详细优化点

##### 1.3.1 `GlobalExceptionHandler.java`

- **文件**:
  `carlos-spring-boot/carlos-spring-boot-starter-web/src/main/java/com/carlos/boot/GlobalExceptionHandler.java`
- **行号**: 349-370
- **当前代码**:

```java
switch (level) {
    case CLIENT_ERROR:
        log.warn(message);
        break;
    case BUSINESS_ERROR:
        log.warn(message);
        break;
    case THIRD_PARTY_ERROR:
        log.error(message);
        break;
    case SYSTEM_ERROR:
        log.error(message);
        break;
    default:
        log.info(message);
}
```

- **优化后**:

```java
switch (level) {
    case CLIENT_ERROR, BUSINESS_ERROR     -> log.warn(message);
    case THIRD_PARTY_ERROR, SYSTEM_ERROR  -> log.error(message);
    default                               -> log.info(message);
}
```

##### 1.3.2 `GlobalErrorController.java`

- **文件**:
  `carlos-spring-boot/carlos-spring-boot-starter-web/src/main/java/com/carlos/boot/error/GlobalErrorController.java`
- **行号**: 37-46
- **当前代码**:

```java
switch (status) {
    case HttpServletResponse.SC_UNAUTHORIZED:
        return Result.error(CommonErrorCode.UNAUTHORIZED);
    case HttpServletResponse.SC_FORBIDDEN:
        return Result.error(CommonErrorCode.UNAUTHORIZED);
    case HttpServletResponse.SC_NOT_FOUND:
        return Result.error(CommonErrorCode.NOT_FOUND);
    default:
        break;
}
```

- **优化后**:

```java
return switch (status) {
    case HttpServletResponse.SC_UNAUTHORIZED -> Result.error(CommonErrorCode.UNAUTHORIZED);
    case HttpServletResponse.SC_FORBIDDEN    -> Result.error(CommonErrorCode.UNAUTHORIZED);
    case HttpServletResponse.SC_NOT_FOUND    -> Result.error(CommonErrorCode.NOT_FOUND);
    default                                  -> Result.error(CommonErrorCode.INTERNAL_ERROR);
};
```

##### 1.3.3 `JsonFactory.java`

- **文件**: `carlos-spring-boot/carlos-spring-boot-starter-json/src/main/java/com/carlos/json/JsonFactory.java`
- **行号**: 82-93
- **当前代码**:

```java
switch (engineType) {
    case FASTJSON2:
        return new FastjsonJsonService(properties);
    case GSON:
        return new GsonJsonService(properties);
    case JACKSON:
    default:
        if (globalObjectMapper != null) {
            return new JacksonJsonService(globalObjectMapper, properties);
        }
        return new JacksonJsonService(new ObjectMapper(), properties);
}
```

- **优化后**:

```java
return switch (engineType) {
    case FASTJSON2 -> new FastjsonJsonService(properties);
    case GSON      -> new GsonJsonService(properties);
    case JACKSON, default -> globalObjectMapper != null
            ? new JacksonJsonService(globalObjectMapper, properties)
            : new JacksonJsonService(new ObjectMapper(), properties);
};
```

##### 1.3.4 `SerializerFactory.java`

- **文件**:
  `carlos-spring-boot/carlos-spring-boot-redis/carlos-spring-boot-starter-redis-core/src/main/java/com/carlos/redis/serialize/SerializerFactory.java`
- **行号**: 53-65
- **当前代码**:

```java
switch (type) {
    case JACKSON:
        return new JacksonSerializer();
    case FASTJSON:
        return new FastjsonSerializer();
    case KRYO:
        return new KryoSerializer();
    case JDK:
        return new JdkSerializer();
    default:
        log.warn("Unknown serializer type...");
        return new JacksonSerializer();
}
```

- **优化后**:

```java
return switch (type) {
    case JACKSON  -> new JacksonSerializer();
    case FASTJSON -> new FastjsonSerializer();
    case KRYO     -> new KryoSerializer();
    case JDK      -> new JdkSerializer();
    default -> {
        log.warn("Unknown serializer type...");
        yield new JacksonSerializer();
    }
};
```

##### 1.3.5 `DefaultDataScopeHandler.java`

- **文件**:
  `carlos-spring-boot/carlos-spring-boot-starter-datascope/src/main/java/com/carlos/datascope/conf/DefaultDataScopeHandler.java`
- **行号**: 114-151
- **当前代码**: 传统 `switch` 含 `break`，多个分支赋值
- **优化后**: 改为 `case NONE -> { info.setColumn("0"); sets = Sets.newHashSet("1"); }` 等 arrow syntax。

##### 1.3.6 `GatewayErrorAttributes.java`（2 处）

- **文件**: `carlos-integration/carlos-gateway/src/main/java/com/carlos/gateway/config/GatewayErrorAttributes.java`

**第 1 处 - 行号 158-178**:

```java
// 当前
switch (errorCode) {
    case "4001": return 401;
    case "4003": return 403;
    // ...
    default: return 500;
}
// 优化后
return switch (errorCode) {
    case "4001"       -> 401;
    case "4003"       -> 403;
    case "4004"       -> 404;
    case "5001"       -> 400;
    case "5104"       -> 401;
    case "5105", "5106" -> 403;
    case "5107"       -> 401;
    case "5108"       -> 405;
    default           -> 500;
};
```

**第 2 处 - 行号 185-207**: 同上结构，将 `switch (status)` 改为 arrow syntax。

##### 1.3.7 `EncryptUtil.java`（3 处）

- **文件**: `carlos-spring-boot/carlos-spring-boot-starter-encrypt/src/main/java/com/carlos/encrypt/EncryptUtil.java`

**第 1 处 - 行号 103-112**（SM4 加密）:

```java
// 当前
switch (storeType) {
    case HEX: encrypt = sm4.encryptHex(str); break;
    case BASE64: encrypt = sm4.encryptBase64(str); break;
    default: encrypt = sm4.encryptHex(str);
}
// 优化后
encrypt = switch (storeType) {
    case BASE64 -> sm4.encryptBase64(str);
    case HEX, default -> sm4.encryptHex(str);
};
```

**第 2 处 - 行号 191-200**（SM2 加密）: 结构同上。

**第 3 处 - 行号 817-828**（密钥生成）:

```java
// 当前
switch (algorithmType) {
    case AES: return generateAesKey();
    case DES3: return java.util.Base64.getEncoder().encodeToString(SecureUtil.generateKey("DESede").getEncoded());
    case RSA: return "请使用 generateRsaKeyPair() 方法生成 RSA 密钥对";
    case SM2: return "请使用 generateSm2KeyPair() 方法生成 SM2 密钥对";
    default: throw new EncryptException("该算法不支持密钥生成: " + algorithmType.getCode());
}
// 优化后
return switch (algorithmType) {
    case AES  -> generateAesKey();
    case DES3 -> java.util.Base64.getEncoder().encodeToString(SecureUtil.generateKey("DESede").getEncoded());
    case RSA  -> "请使用 generateRsaKeyPair() 方法生成 RSA 密钥对";
    case SM2  -> "请使用 generateSm2KeyPair() 方法生成 SM2 密钥对";
    default   -> throw new EncryptException("该算法不支持密钥生成: " + algorithmType.getCode());
};
```

##### 1.3.8 `CacheKeyGenerator.java`

- **文件**:
  `carlos-spring-boot/carlos-spring-boot-redis/carlos-spring-boot-starter-redis-core/src/main/java/com/carlos/redis/util/CacheKeyGenerator.java`
- **行号**: 125-136
- **当前代码**:

```java
switch (overflowStrategy.toLowerCase()) {
    case "md5":
        return "MD5:" + DigestUtil.md5Hex(key);
    case "sha1":
        return "SHA1:" + DigestUtil.sha1Hex(key);
    case "truncate":
    default:
        return key.substring(0, maxLength - 3) + "...";
}
```

- **优化后**:

```java
return switch (overflowStrategy.toLowerCase()) {
    case "md5"      -> "MD5:" + DigestUtil.md5Hex(key);
    case "sha1"     -> "SHA1:" + DigestUtil.sha1Hex(key);
    case "truncate", default -> key.substring(0, maxLength - 3) + "...";
};
```

---

## 二、Virtual Threads 虚拟线程

**优先级**: 🔴 **高** | **风险**: ⭐⭐ 中

Java 21 正式发布的 **JEP 444: Virtual Threads** 能以轻量级方式支持海量并发，特别适合 I/O
密集型场景（网络请求、数据库操作、消息队列）。本项目存在大量此类场景，但升级前需验证 JDBC 驱动和第三方框架兼容性。

> **关键兼容性提示**:
> - MySQL Connector/J **8.0.33+** 已支持虚拟线程
> - PostgreSQL JDBC **42.7.0+** 已支持虚拟线程
> - Spring Boot **3.2+** 原生支持 `server.tomcat.threads.max=virtual`
> - 当前项目使用 Spring Boot **3.5.9**，已具备虚拟线程支持能力

---

### 2.1 显式线程池创建（I/O 密集型）

#### 2.1.1 `RedisUtil.java` — Redis 并行操作线程池

- **文件**:
  `carlos-spring-boot/carlos-spring-boot-redis/carlos-spring-boot-starter-redis/src/main/java/com/carlos/redis/util/RedisUtil.java`
- **行号**: 61-75
- **当前代码**:

```java
private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(
    4, 16, 60L, TimeUnit.SECONDS,
    new LinkedBlockingQueue<>(1000),
    new ThreadFactory() {
        private final AtomicLong counter = new AtomicLong(0);
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "redis-util-" + counter.incrementAndGet());
            t.setDaemon(true);
            return t;
        }
    },
    new ThreadPoolExecutor.CallerRunsPolicy()
);
```

- **优化建议**: ✅ **高度适合虚拟线程**。该线程池用于 Redis 批量 `mget` / `hashMultiGet` 的并行 I/O 操作（第 769、1012
  行）。Redis 网络 I/O 属于典型 I/O 密集型场景，固定 16 个平台线程会成为瓶颈。
- **优化后**:

```java
private static final ExecutorService EXECUTOR = Executors.newVirtualThreadPerTaskExecutor();
```

#### 2.1.2 `ClickHouseBatchWriter.java` — ClickHouse 批量刷新线程池

- **文件**:
  `carlos-integration/carlos-audit/carlos-audit-bus/src/main/java/com/carlos/audit/clickhouse/ClickHouseBatchWriter.java`
- **行号**: 66-70
- **当前代码**:

```java
flushExecutor = Executors.newFixedThreadPool(2, r -> {
    Thread t = new Thread(r, "clickhouse-flush");
    t.setDaemon(true);
    return t;
});
```

- **优化建议**: ✅ **适合虚拟线程**。ClickHouse 写入属于网络 I/O 操作，当前仅 2 条线程可能成为吞吐量瓶颈。若审计日志量大，建议改用虚拟线程执行器。但需注意
  ClickHouse 客户端本身的连接池限制。

#### 2.1.3 `MultiDataSourceLiquibase.java` — 数据库迁移异步线程池

- **文件**:
  `carlos-spring-boot/carlos-spring-boot-starter-migration/src/main/java/com/carlos/migration/core/MultiDataSourceLiquibase.java`
- **行号**: 60-70
- **当前代码**:

```java
executorService = Executors.newFixedThreadPool(
    migrationProperties.getAsyncThreadPoolSize(),
    new ThreadFactory() {
        private int count = 0;
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "liquibase-migration-" + (++count));
        }
    }
);
```

- **优化建议**: ⚠️ **需谨慎评估**。Liquibase 数据库迁移属于阻塞 I/O（DDL/DML），但通常是**启动期一次性任务**
  。虚拟线程可提升并行迁移多个数据源的效率，但需确认底层 JDBC 驱动是否支持虚拟线程（MySQL Connector/J 8.0.33+ 已支持）。

#### 2.1.4 `AsyncTaskUtil.java` — GUI 工具异步任务线程池

- **文件**: `carlos-integration/carlos-tools/src/main/java/com/carlos/fx/utils/AsyncTaskUtil.java`
- **行号**: 65-70
- **当前代码**:

```java
private static final ExecutorService executor = Executors.newCachedThreadPool(r -> {
    Thread thread = new Thread(r);
    thread.setDaemon(true);
    return thread;
});
```

- **优化建议**: ✅ **适合虚拟线程**。`CachedThreadPool` 在高并发下会创建大量平台线程，改用虚拟线程执行器可避免线程爆炸，同时降低内存占用。

#### 2.1.5 `ExecutorUtil.java` — 框架默认线程池工具类

- **文件**: `carlos-spring-boot/carlos-spring-boot-core/src/main/java/com/carlos/core/util/ExecutorUtil.java`
- **行号**: 23, 37-52
- **当前代码**:

```java
public static final ThreadPoolExecutor POOL = ExecutorUtil.get(8, 15, "default-", 20, null);

public static ThreadPoolExecutor get(int coreNum, int maxNum, String namePrefix, int queueSize, @Nullable RejectedExecutionHandler handler) {
    handler = ObjUtil.defaultIfNull(handler, new ThreadPoolExecutor.CallerRunsPolicy());
    return ExecutorBuilder.create()
        .setCorePoolSize(coreNum)
        .setMaxPoolSize(maxNum)
        .setKeepAliveTime(10, TimeUnit.SECONDS)
        .setThreadFactory(ThreadUtil.createThreadFactoryBuilder().setNamePrefix(namePrefix).build())
        .setWorkQueue(new LinkedBlockingQueue<>(queueSize))
        .setHandler(handler)
        .build();
}
```

- **优化建议**: ✅ **核心改进点**。这是整个框架的默认线程池工具，提供固定大小（8-15）的线程池。**建议新增虚拟线程工厂方法**，让各
  Starter 模块在 I/O 密集型场景下可选择使用虚拟线程。
- **优化后（新增方法）**:

```java
public static ExecutorService newVirtualThreadPerTaskExecutor() {
    return Executors.newVirtualThreadPerTaskExecutor();
}
```

#### 2.1.6 `SnowflakeRedisCacheManager.java` — 定时调度线程池

- **文件**:
  `carlos-spring-boot/carlos-spring-boot-starter-snowflake/src/main/java/com/carlos/snowflake/cache/SnowflakeRedisCacheManager.java`
- **行号**: 48-50
- **当前代码**:

```java
ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
    new BasicThreadFactory.Builder().namingPattern("snowflake-redis-pool-%d").daemon(true).build());
```

- **优化建议**: ❌ **不适合替换为虚拟线程执行器**。`ScheduledExecutorService` 不支持 `newVirtualThreadPerTaskExecutor()`
  。但可将线程工厂改为虚拟线程工厂：

```java
ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
    Thread.ofVirtual().factory());
```

#### 2.1.7 `PropertyColumnUtil.java` / `DisruptorMetrics.java` — 定时调度线程池

- **文件**:
    - `carlos-spring-boot/carlos-spring-boot-starter-mybatis/.../PropertyColumnUtil.java`
    - `carlos-spring-boot/carlos-spring-boot-starter-disruptor/.../DisruptorMetrics.java`
- **优化建议**: ❌ 同为 `ScheduledExecutorService` 定时调度。可将线程工厂改为 `Thread.ofVirtual().factory()`，但保持
  `ScheduledExecutorService` 结构。

---

### 2.2 `@Async` 注解优化

#### 2.2.1 `MessageAsyncConfig.java` — 消息异步线程池

- **文件**:
  `carlos-integration/carlos-message/carlos-message-bus/src/main/java/com/carlos/message/config/MessageAsyncConfig.java`
- **行号**: 22-33
- **当前代码**:

```java
@Bean("messageTaskExecutor")
public Executor messageTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(5);
    executor.setMaxPoolSize(20);
    executor.setQueueCapacity(200);
    executor.setThreadNamePrefix("msg-async-");
    executor.setWaitForTasksToCompleteOnShutdown(true);
    executor.setAwaitTerminationSeconds(30);
    executor.initialize();
    return executor;
}
```

- **优化建议**: ✅ **高度适合虚拟线程**。消息发送是典型 I/O 密集型操作（HTTP/RPC 调用各消息渠道）。`ThreadPoolTaskExecutor` 的
  5-20 线程限制会制约消息吞吐量。
- **优化后**:

```java
@Bean("messageTaskExecutor")
public Executor messageTaskExecutor() {
    return Executors.newVirtualThreadPerTaskExecutor();
}
```

> 若需支持 `setWaitForTasksToCompleteOnShutdown`，可自定义 `VirtualThreadTaskExecutor` 包装类。

#### 2.2.2 `BatchServiceImpl.java` — 批量数据异步插入

- **文件**:
  `carlos-spring-boot/carlos-spring-boot-starter-mybatis/src/main/java/com/carlos/datasource/base/BatchServiceImpl.java`
- **行号**: 226-233
- **当前代码**:

```java
@Async("taskExecutor")
public CompletableFuture<Boolean> asyncSaveBatch(List<T> entityList) { ... }

@Async("taskExecutor")
public CompletableFuture<Boolean> asyncSaveBatch(List<T> entityList, int partitionSize) { ... }
```

- **优化建议**: ✅ **适合虚拟线程**。`taskExecutor` 在当前项目中**未找到显式 Bean 定义**，Spring Boot 会回退到
  `SimpleAsyncTaskExecutor`（每任务创建新平台线程），性能极差。
- **优化后**: 显式定义 `taskExecutor` Bean 为虚拟线程执行器：

```java
@Bean("taskExecutor")
public Executor taskExecutor() {
    return Executors.newVirtualThreadPerTaskExecutor();
}
```

#### 2.2.3 `UserLoginService.java` — 登录审计日志记录

- **文件**:
  `carlos-integration/carlos-auth/carlos-auth-service/src/main/java/com/carlos/auth/login/UserLoginService.java`
- **行号**: 441-450
- **当前代码**:

```java
@Async
public void recordLoginAudit(UserInfo user, String eventType, String status, String errorMessage) {
    ApiAuditLogMainParam param = buildAuditLogParam(...);
    apiAuditLogMain.saveAuditLog(param);
}
```

- **优化建议**: ✅ **适合虚拟线程**。未指定线程池名称，使用 Spring 默认 `SimpleAsyncTaskExecutor`，每调用创建一个新平台线程。登录是高频操作，建议：

1. 显式指定虚拟线程执行器：`@Async("virtualTaskExecutor")`
2. 或配置全局 `AsyncConfigurer` 使用虚拟线程作为默认执行器。

#### 2.2.4 `SecurityAlertService.java` — 安全告警审计日志

- **文件**:
  `carlos-integration/carlos-auth/carlos-auth-service/src/main/java/com/carlos/auth/audit/service/SecurityAlertService.java`
- **行号**: 365-374
- **当前代码**:

```java
@Async
public void recordSecurityAlertAudit(Long userId, String username, ...) {
    ApiAuditLogMainParam param = buildAuditLogParam(...);
    apiAuditLogMain.saveAuditLog(param);
}
```

- **优化建议**: ✅ **适合虚拟线程**。与 `UserLoginService.java` 同理，使用默认 `SimpleAsyncTaskExecutor`
  。安全告警可能突发高频（如遭受攻击时），虚拟线程可优雅应对突发流量而不会导致平台线程耗尽。

#### 2.2.5 `RateLimitMetricsListener.java` / `RateLimitAlertListener.java` — 网关限流事件监听

- **文件**:
    - `carlos-integration/carlos-gateway/.../RateLimitMetricsListener.java`
    - `carlos-integration/carlos-gateway/.../RateLimitAlertListener.java`
- **当前代码**: `@Async` 未指定线程池
- **优化建议**: ✅ **适合虚拟线程**。事件监听是高频轻量级 I/O（Micrometer 指标上报）。使用默认 `SimpleAsyncTaskExecutor`
  在高并发限流场景下会大量创建平台线程。

---

### 2.3 `CompletableFuture` 异步调用优化

#### 2.3.1 `RedisUtil.java` — Redis 批量获取

- **文件**:
  `carlos-spring-boot/carlos-spring-boot-redis/carlos-spring-boot-starter-redis/src/main/java/com/carlos/redis/util/RedisUtil.java`
- **行号**: 768-782, 1011-1014
- **当前代码**:

```java
List<CompletableFuture<Void>> futures = batches.stream()
    .map(batch -> CompletableFuture.runAsync(() -> {
        try {
            List<T> vs = (List<T>) valueOperations.multiGet(batch);
            ...
        } catch (Exception e) {
            log.error("Redis mget batch error, batchSize={}", batch.size(), e);
        }
    }, EXECUTOR))
    .toList();
```

- **优化建议**: ✅ **高度适合虚拟线程**。Redis `multiGet` 是网络 I/O，当前受 `EXECUTOR` 的 16 线程上限限制。建议将
  `EXECUTOR` 替换为 `Executors.newVirtualThreadPerTaskExecutor()`。

#### 2.3.2 `RabbitMqClient.java` — MQ 异步发送

- **文件**:
  `carlos-spring-boot/carlos-spring-boot-starter-mq/src/main/java/com/carlos/mq/client/rabbitmq/RabbitMqClient.java`
- **行号**: 154-163
- **当前代码**:

```java
@Override
public void sendAsync(MqMessage<?> message, SendCallback callback) {
    CompletableFuture.runAsync(() -> {
        SendResult result = send(message);
        if (result.isSuccess()) {
            callback.onSuccess(result);
        } else {
            callback.onException(result.getException());
        }
    });
}
```

- **优化建议**: ✅ **适合虚拟线程**。`runAsync` 无指定线程池时使用 `ForkJoinPool.commonPool()`（线程数 = CPU 核心数）。MQ
  发送是网络 I/O，建议显式传入虚拟线程执行器：

```java
CompletableFuture.runAsync(() -> { ... }, virtualThreadExecutor);
```

#### 2.3.3 `KafkaMqClient.java` — Kafka 延迟发送

- **文件**:
  `carlos-spring-boot/carlos-spring-boot-starter-mq/src/main/java/com/carlos/mq/client/kafka/KafkaMqClient.java`
- **行号**: 149
- **当前代码**:

```java
CompletableFuture.delayedExecutor(delayTime, timeUnit).execute(() -> send(message));
```

- **优化建议**: ⚠️ **无需优先优化**。`delayedExecutor` 内部使用 `ScheduledThreadPoolExecutor`，仅用于延迟调度，不承载大量并发任务。

---

### 2.4 手动 `new Thread(...)` 优化

#### 2.4.1 `LicenseCreatorService.java` — 子进程输出读取

- **文件**:
  `carlos-spring-boot/carlos-spring-boot-starter-license-generate/src/main/java/com/carlos/license/generate/LicenseCreatorService.java`
- **行号**: 296-300
- **当前代码**:

```java
Thread outputThread = new Thread(() -> {
    String result = RuntimeUtil.getResult(process);
    System.out.println("Output:  " + result);
});
outputThread.start();
```

- **优化建议**: ✅ **适合虚拟线程**。读取子进程输出流是一次性短时 I/O 任务，使用虚拟线程更轻量：

```java
Thread.startVirtualThread(() -> { ... });
```

#### 2.4.2 `PropertyColumnUtil.java` — Shutdown Hook

- **文件**: `carlos-spring-boot/carlos-spring-boot-starter-mybatis/.../PropertyColumnUtil.java`
- **当前代码**:

```java
Runtime.getRuntime().addShutdownHook(new Thread(() -> { ... }));
```

- **优化建议**: ❌ **shutdown hook 不建议用虚拟线程**。JVM 关闭钩子（Shutdown Hook）不建议使用虚拟线程，因为虚拟线程在 JVM
  关闭时可能无法保证执行完成。保持平台线程。

---

### 2.5 WebClient / Feign 客户端评估

#### 2.5.1 `InfrastructureAutoConfiguration.java` — Gateway WebClient

- **文件**:
  `carlos-integration/carlos-gateway/src/main/java/com/carlos/gateway/config/InfrastructureAutoConfiguration.java`
- **优化建议**: ✅ **无需改为虚拟线程**。`WebClient` 基于 Project Reactor，已经是**非阻塞 I/O**。虚拟线程主要针对阻塞 I/O
  的线程池优化，此处不适用。

#### 2.5.2 `FeignConfig.java` — Feign + OkHttp

- **文件**: `carlos-spring-boot/carlos-spring-cloud-starter/src/main/java/com/carlos/cloud/feign/FeignConfig.java`
- **优化建议**: ⚠️ **间接优化点**。Feign + OkHttp 是**阻塞式 HTTP 客户端**，但 Feign 调用本身通常发生在 Tomcat/Jetty
  的处理线程中。若希望从虚拟线程受益，可配置 **Spring Boot 3.2+ 的 Tomcat 虚拟线程支持**（
  `server.tomcat.threads.max = virtual` 或配置 `VirtualThreadExecutor`），让 Feign 调用在虚拟线程上执行，而非单独改造
  OkHttp。

---

## 三、Text Blocks / String.formatted / 日志占位符

**优先级**: 🟡 **中** | **风险**: ⭐ 极低

---

### 3.1 Text Blocks 多行字符串（~14 处）

**优化原理**: Java 15 正式引入的 Text Blocks（`"""..."""`）可消除多行字符串中的 `\n` 和 `"` 转义，提升
JSON/YAML/日志/对话框消息的可读性。

#### 3.1.1 `ApplicationRunnerWorker.java` — 启动 Logo

- **文件**:
  `carlos-spring-boot/carlos-spring-boot-starter-web/src/main/java/com/carlos/boot/runner/ApplicationRunnerWorker.java`
- **行号**: 45-52
- **当前代码**:

```java
String startSuccess = "\n" +
    "\n" +
    "    _____ __             __     _____\n" +
    "   / ___// /_____ ______/ /_   / ___/__  _______________  __________\n" +
    "   \\__ \\/ __/ __ `/ ___/ __/   \\__ \\/ / / / ___/ ___/ _ \\/ ___/ ___/\n" +
    "  _____/ / /_/ /_/ /  / /     _____/ / /_/ /__/ /__/  __(__  |__  )      \n" +
    " /____/\\__/\\__,_/_/   \\__/   /____/\\__,_/\\___/\\___/\\___/____/____/\n" +
    "";
```

- **优化后**:

```java
String startSuccess = """


        _____ __             __     _____
       / ___// /_____ ______/ /_   / ___/__  _______________  __________
       \\__ \\/ __/ __ `/ ___/ __/   \\__ \\/ / / / ___/ ___/ _ \\/ ___/ ___/
      _____/ / /_/ /_/ /  / /     _____/ / /_/ /__/ /__/  __(__  |__  )
     /____/\\__/\\__,_/_/   \\__/   /____/\\__,_/\\___/\\___/\\___/____/____/
    """;
```

#### 3.1.2 `BucketOptUtil.java` — MinIO 桶策略 JSON

- **文件**:
  `carlos-spring-boot/carlos-spring-boot-starter-minio/src/main/java/com/carlos/minio/utils/BucketOptUtil.java`
- **行号**: 113-131
- **当前代码**: 大量 `"{\n" + "\t\"Version\": ...` 拼接
- **优化后**:

```java
String config = """
    {
    	"Version": "2012-10-17",
    	"Statement": [{
    		"Effect": "Allow",
    		"Principal": {
    			"AWS": ["*"]
    		},
    		"Action": ["s3:GetBucketLocation", "s3:ListBucket", "s3:ListBucketMultipartUploads"],
    		"Resource": ["arn:aws:s3:::%s"]
    	}, {
    		"Effect": "Allow",
    		"Principal": {
    			"AWS": ["*"]
    		},
    		"Action": ["s3:AbortMultipartUpload", "s3:DeleteObject", "s3:GetObject", "s3:ListMultipartUploadParts", "s3:PutObject"],
    		"Resource": ["arn:aws:s3:::%s/*"]
    	}]
    }
    """.formatted(bucket, bucket);
```

#### 3.1.3 `ChangelogGenerator.java` — Liquibase YAML/SQL 模板

- **文件**:
  `carlos-spring-boot/carlos-spring-boot-starter-migration/src/main/java/com/carlos/migration/util/ChangelogGenerator.java`
- **行号**: 48-71, 97-103
- **当前代码**: `String.format("databaseChangeLog:\n  - changeSet:\n      id: %s\n" + ...)`
- **优化后（YAML）**:

```java
String content = """
    databaseChangeLog:
      - changeSet:
          id: %s
          author: %s
          created: "%s"
          comment: "%s"
          changes:
            # 在此添加变更操作
          rollback:
            # 在此添加回滚操作
    """.formatted(version, author, LocalDateTime.now().format(FORMATTER), description);
```

#### 3.1.4 `MergeRequestController.java` — JavaFX 对话框消息

- **文件**: `carlos-integration/carlos-tools/src/main/java/com/carlos/fx/gitlab/controller/MergeRequestController.java`
- **行号**: 534-538
- **当前代码**:

```java
boolean confirmed = DialogUtil.showConfirm(
        "确认合并",
        "确定要合并请求 #" + selected.getIid() + " 吗？\n\n" +
                "源分支: " + selected.getSourceBranch() + "\n" +
                "目标分支: " + selected.getTargetBranch()
);
```

- **优化后**:

```java
boolean confirmed = DialogUtil.showConfirm(
        "确认合并",
        """
        确定要合并请求 #%d 吗？

        源分支: %s
        目标分支: %s
        """.formatted(selected.getIid(), selected.getSourceBranch(), selected.getTargetBranch())
);
```

#### 3.1.5 `SecurityAlertService.java` — 钉钉通知消息

- **文件**:
  `carlos-integration/carlos-auth/carlos-auth-service/src/main/java/com/carlos/auth/audit/service/SecurityAlertService.java`
- **行号**: 354-357
- **当前代码**:

```java
message.put("text", String.format("用户: %s\nIP: %s\n位置: %s\n时间: %s",
    alert.getUsername(), alert.getIpAddress(), alert.getLocation(),
    alert.getCreateTime()));
```

- **优化后**:

```java
message.put("text", """
    用户: %s
    IP: %s
    位置: %s
    时间: %s
    """.formatted(alert.getUsername(), alert.getIpAddress(), alert.getLocation(), alert.getCreateTime()));
```

#### 其他 Text Blocks 候选文件

- `carlos-integration/carlos-tools/.../UserManagementController.java`
- `carlos-integration/carlos-tools/.../BranchManagementController.java`
- `carlos-integration/carlos-tools/.../DialogUtil.java`
- `carlos-integration/carlos-tools/.../IssueManagementController.java`

---

### 3.2 `String.format(...)` → `"...".formatted(...)`（~25 处）

**优化原理**: Java 15 引入的实例方法 `String.formatted(Object... args)` 比静态方法
`String.format(String format, Object... args)` 更简洁，减少括号嵌套。

#### 3.2.1 `QrCodeGenerator.java`

- **文件**: `carlos-integration/carlos-auth/carlos-auth-service/src/main/java/com/carlos/auth/util/QrCodeGenerator.java`
- **行号**: 48-54, 84-87
- **当前代码**:

```java
String uri = String.format(
    "otpauth://totp/%s:%s?secret=%s&issuer=%s&algorithm=SHA1&digits=6&period=30",
    APP_NAME, encodedUsername, encodedSecret, encodedIssuer
);

return String.format(
    "https://chart.googleapis.com/chart?chs=200x200&cht=qr&chl=%s",
    encodedUri
);
```

- **优化后**:

```java
String uri = "otpauth://totp/%s:%s?secret=%s&issuer=%s&algorithm=SHA1&digits=6&period=30"
    .formatted(APP_NAME, encodedUsername, encodedSecret, encodedIssuer);

return "https://chart.googleapis.com/chart?chs=200x200&cht=qr&chl=%s".formatted(encodedUri);
```

#### 3.2.2 `DeviceFingerprint.java`

- **文件**:
  `carlos-integration/carlos-auth/carlos-auth-service/src/main/java/com/carlos/auth/util/DeviceFingerprint.java`
- **行号**: 38-43, 65-71, 144
- **当前代码**:

```java
String fingerprintData = String.format("%s|%s|%s|%s", ...);
String enhancedData = String.format("%s|%s|%s|%s|%s", ...);
return String.format("%s on %s", browser, os);
```

- **优化后**:

```java
String fingerprintData = "%s|%s|%s|%s".formatted(...);
String enhancedData = "%s|%s|%s|%s|%s".formatted(...);
return "%s on %s".formatted(browser, os);
```

#### 3.2.3 `GatewayExceptionHandler.java`

- **文件**: `carlos-integration/carlos-gateway/src/main/java/com/carlos/gateway/config/GatewayExceptionHandler.java`
- **行号**: 200, 233, 312
- **当前代码**:

```java
message = String.format("服务 [%s] 暂不可用，请稍后重试", serviceName);
String message = String.format("下游服务返回错误 [%d]: %s", status.value(), status.getReasonPhrase());
String logMessage = String.format("[%s] %s %s - %s: %s", requestId, method, path, ...);
```

- **优化后**:

```java
message = "服务 [%s] 暂不可用，请稍后重试".formatted(serviceName);
String message = "下游服务返回错误 [%d]: %s".formatted(status.value(), status.getReasonPhrase());
String logMessage = "[%s] %s %s - %s: %s".formatted(requestId, method, path, ...);
```

#### 3.2.4 其他涉及文件

- `carlos-integration/carlos-gateway/.../AccessLogFilter.java`
- `carlos-integration/carlos-gateway/.../RequestTracingFilter.java`
- `carlos-spring-boot/carlos-spring-boot-starter-web/.../GlobalExceptionHandler.java`（3 处）
- `carlos-integration/carlos-auth/.../TotpGenerator.java`
- `carlos-spring-boot/carlos-spring-boot-starter-redis/.../CacheMetrics.java`
- `carlos-spring-boot/carlos-spring-boot-starter-mq/.../Mq*Exception.java`（多处）
- `carlos-integration/carlos-license/.../LicenseVerify.java`（`MessageFormat.format` → `formatted`）

---

### 3.3 日志字符串拼接 → 占位符（~7 处）

**优化原理**: SLF4J/Logback 的 `{}` 占位符可避免字符串拼接开销（尤其在日志级别被过滤时），同时提升代码可读性。

#### 3.3.1 `BaseAuthenticationProvider.java`

- **文件**:
  `carlos-integration/carlos-auth/carlos-auth-service/src/main/java/com/carlos/auth/oauth2/grant/BaseAuthenticationProvider.java`
- **行号**: 128
- **当前代码**:

```java
log.debug("got usernamePasswordAuthenticationToken=" + usernamePasswordAuthenticationToken);
```

- **优化后**:

```java
log.debug("got usernamePasswordAuthenticationToken={}", usernamePasswordAuthenticationToken);
```

#### 3.3.2 `WoCloudClient.java` / `PostalClient.java`

- **文件**:
    - `carlos-spring-boot/carlos-spring-boot-starter-sms/.../WoCloudClient.java`
    - `carlos-spring-boot/carlos-spring-boot-starter-sms/.../PostalClient.java`
- **当前代码**:

```java
log.warn("短信第 {" + retry + "} 次重新发送");
```

- **优化后**:

```java
log.warn("短信第 {} 次重新发送", retry);
```

#### 3.3.3 其他涉及文件

- `carlos-integration/carlos-tools/.../TemplateUtil.java`
- `carlos-integration/carlos-tools/.../XmlUtils.java`
- `carlos-integration/carlos-tools/.../CodeGeneratorService.java`

---

## 四、Sequenced Collections / 不可变集合

**优先级**: 🟢 **低** | **风险**: ⭐ 极低

---

### 4.1 `list.get(0)` → `list.getFirst()`（~14 处）

**优化原理**: Java 21 的 **JEP 431: Sequenced Collections** 为 `List` 新增了 `getFirst()` / `getLast()` 等方法，语义更清晰。

#### 涉及文件清单

| #  | 文件                            | 行号      | 当前代码                          | 优化后                               |
|----|-------------------------------|---------|-------------------------------|-----------------------------------|
| 1  | `GlobalExceptionHandler.java` | 91      | `details.get(0).getMessage()` | `details.getFirst().getMessage()` |
| 2  | `GlobalExceptionHandler.java` | 114     | `details.get(0).getMessage()` | `details.getFirst().getMessage()` |
| 3  | `IpUtil.java`                 | 143     | `return ipv6Result.get(0);`   | `return ipv6Result.getFirst();`   |
| 4  | `LogConfig.java`              | 72-73   | `storages.get(0)`             | `storages.getFirst()`             |
| 5  | `AppClientManagerImpl.java`   | 179     | `entity.get(0)`               | `entity.getFirst()`               |
| 6  | `AppClientManagerImpl.java`   | 204     | `clients.get(0)`              | `clients.getFirst()`              |
| 7  | `SysDictService.java`         | 191-192 | `words.get(0)`                | `words.getFirst()`                |
| 8  | `SysDictService.java`         | 199     | `words.get(0)`                | `words.getFirst()`                |
| 9  | `SysConfigServiceImpl.java`   | 87      | `dto.get(0)`                  | `dto.getFirst()`                  |
| 10 | `SysRegionService.java`       | 634     | `sysRegionDTOS.get(0)`        | `sysRegionDTOS.getFirst()`        |
| 11 | `SmsUtil.java`                | 128     | `phones.get(0)`               | `phones.getFirst()`               |
| 12 | `SmsUtil.java`                | 134     | `phones.get(0)`               | `phones.getFirst()`               |
| 13 | `CodeGeneratorService.java`   | 183     | `items.get(0)`                | `items.getFirst()`                |

### 4.2 `list.get(list.size() - 1)` → `list.getLast()`（1 处）

| # | 文件            | 行号  | 当前代码                                    | 优化后                    | 优先级 |
|---|---------------|-----|-----------------------------------------|------------------------|-----|
| 1 | `IpUtil.java` | 141 | `ipv4Result.get(ipv4Result.size() - 1)` | `ipv4Result.getLast()` | 中   |

> 该行注释明确说明"存在多个 ip 的时候，获取最后一个"，使用 `getLast()` 语义最匹配，可读性提升明显。

### 4.3 `Collections.unmodifiableXxx(new Xxx<>(...))` → `Xxx.copyOf(...)`（~15 处）

**优化原理**: Java 10 引入的 `List.copyOf` / `Set.copyOf` / `Map.copyOf` 比
`Collections.unmodifiableXxx(new Xxx<>(...))` 更简洁、语义更清晰，且本身返回不可变集合。

#### 涉及文件清单

| # | 文件                                    | 当前代码                                                 | 优化后                                          |
|---|---------------------------------------|------------------------------------------------------|----------------------------------------------|
| 1 | `ApplicationCorsProperties.java`      | `Collections.unmodifiableList(Arrays.asList(...))`   | `List.of(...)`                               |
| 2 | `BaseAuthenticationToken.java` (x2)   | `Collections.unmodifiableSet(new HashSet<>(scopes))` | `Set.copyOf(scopes)`                         |
| 3 | `ExtendAuthenticationToken.java` (x2) | `Collections.unmodifiableMap(new HashMap<>(...))`    | `Map.copyOf(additionalParameters)`           |
| 4 | `TranslationMetadata.java`            | `Collections.unmodifiableList(list)`                 | `List.copyOf(list)`                          |
| 5 | `TranslationData.java` (x4)           | `Collections.unmodifiableMap(users)` 等               | `Map.copyOf(users)` 等                        |
| 6 | `TranslationBatch.java` (x5)          | `Collections.unmodifiableSet(userIds)` 等             | `Set.copyOf(userIds)` / `Map.copyOf(result)` |

---

## 五、Record 类重构评估

**优先级**: ⚪ **暂不推荐** | **风险**: ⭐⭐⭐ 高

---

### 5.1 结论：本项目 Record 适用性非常有限

经对 **2,329** 个 Java 文件扫描分析，本项目**绝大多数 DTO/VO/AO/Param 类不适合改为 Record**，核心原因如下：

| 阻碍因素                       | 影响范围              | 说明                                                            |
|----------------------------|-------------------|---------------------------------------------------------------|
| `@Accessors(chain = true)` | 几乎所有 DTO/VO       | Record 无 setter，无法支持 `new XxxDTO().setA().setB()` 链式调用        |
| `@NoArgsConstructor`       | Spring/MyBatis 实体 | `@RequestBody` / `@ModelAttribute` / MyBatis-Plus 映射需要无参构造    |
| `@Builder`                 | 部分配置/事件类          | Record 原生不支持 Lombok `@Builder`                                |
| 继承框架基类                     | Entity 类          | 如 `AuditLogConfig extends Model<AuditLogConfig>`，Record 不能继承类 |
| `@ConfigurationProperties` | 配置类               | 配置类需要可变字段接收配置注入                                               |

### 5.2 少数可考虑改为 Record 的类（低收益）

以下类是纯数据载体、无继承、无 `@Accessors(chain = true)`，但**改为 Record 的收益有限**，且会破坏现有的反射/序列化习惯：

| # | 文件                                                  | 类名                         | 说明                                                    |
|---|-----------------------------------------------------|----------------------------|-------------------------------------------------------|
| 1 | `carlos-spring-boot-core/.../FieldErrorDetail.java` | `FieldErrorDetail`         | 仅 3 个字段，但用了 `@Builder` + `@NoArgsConstructor`，需调整构建方式 |
| 2 | `carlos-auth/.../SecurityAlertHandleParam.java`     | `SecurityAlertHandleParam` | 纯数据，但 `implements Serializable` + `serialVersionUID`  |
| 3 | `carlos-org/.../OrgDepartmentMoveParam.java`        | `OrgDepartmentMoveParam`   | 纯数据，但 Param 类通常需要无参构造供框架绑定                            |
| 4 | `carlos-spring-boot-core/.../ParamId.java`          | `ParamId<T>`               | 泛型类，实现了 `Param` 接口                                    |

### 5.3 真正适合 Record 的场景

如果**未来新增**以下类型的类，建议直接使用 `record`：

- 不可变的事件对象（如 Disruptor 事件，若不需要继承和可变字段）
- 内部只读数据载体（如翻译模块中 `TranslationData` 内部的小结构）
- 配置键值对/元数据对象（无 Spring 绑定需求）

---

## 六、推荐实施计划

### 阶段一：低风险、高可读性（立即执行）

预计耗时：1-2 天

| 序号 | 优化项                                           | 数量    | 收益    | 风险   |
|----|-----------------------------------------------|-------|-------|------|
| 1  | `instanceof` + 强制转换 → 模式匹配                    | ~20 处 | ⭐⭐⭐ 高 | ⭐ 极低 |
| 2  | 传统 `switch` → Arrow Syntax                    | ~12 处 | ⭐⭐⭐ 高 | ⭐ 极低 |
| 3  | `if-else if` 类型链 → Pattern Matching switch    | ~8 处  | ⭐⭐⭐ 高 | ⭐ 极低 |
| 4  | `String.format(...)` → `"...".formatted(...)` | ~25 处 | ⭐⭐ 中  | ⭐ 极低 |

### 阶段二：字符串与集合优化（本周内）

预计耗时：2-3 天

| 序号 | 优化项                                                            | 数量    | 收益    | 风险   |
|----|----------------------------------------------------------------|-------|-------|------|
| 1  | 多行字符串拼接 → Text Blocks                                          | ~14 处 | ⭐⭐⭐ 高 | ⭐ 极低 |
| 2  | 日志 `+` 拼接 → `{}` 占位符                                           | ~7 处  | ⭐⭐ 中  | ⭐ 极低 |
| 3  | `Collections.unmodifiableXxx(new Xxx<>())` → `Xxx.copyOf(...)` | ~15 处 | ⭐⭐ 中  | ⭐ 极低 |
| 4  | `get(0)` / `get(size()-1)` → `getFirst()` / `getLast()`        | ~15 处 | ⭐⭐ 中  | ⭐ 极低 |

### 阶段三：虚拟线程升级（需测试验证）

预计耗时：1 周（含测试）

| 优先级  | 模块                                            | 优化动作                                                                           | 验证点                |
|------|-----------------------------------------------|--------------------------------------------------------------------------------|--------------------|
| 🔴 高 | `RedisUtil.EXECUTOR`                          | 将 `ThreadPoolExecutor(4,16)` 替换为 `Executors.newVirtualThreadPerTaskExecutor()` | Redis 批量操作性能、连接池行为 |
| 🔴 高 | `MessageAsyncConfig.messageTaskExecutor`      | `ThreadPoolTaskExecutor` → 虚拟线程执行器                                             | 消息发送吞吐量、并发稳定性      |
| 🔴 高 | `BatchServiceImpl` 的 `@Async("taskExecutor")` | 显式定义 `taskExecutor` Bean 为虚拟线程执行器                                              | 数据库批量插入性能          |
| 🔴 高 | 所有默认 `@Async`（未指定线程池）                         | 配置全局 `AsyncConfigurer` 使用虚拟线程执行器                                               | 登录/告警/限流场景并发稳定性    |
| 🔴 高 | `RabbitMqClient.sendAsync`                    | `runAsync` 显式传入虚拟线程执行器                                                         | MQ 发送吞吐量           |
| 🟡 中 | `ClickHouseBatchWriter.flushExecutor`         | `newFixedThreadPool(2)` → 虚拟线程执行器                                              | ClickHouse 写入性能    |
| 🟡 中 | `AsyncTaskUtil.executor`                      | `newCachedThreadPool` → 虚拟线程执行器                                                | GUI 工具后台任务响应       |
| 🟡 中 | `ExecutorUtil` 工具类                            | 新增虚拟线程工厂方法供各模块使用                                                               | 兼容性                |
| 🟢 低 | `LicenseCreatorService` 子进程读取                 | `new Thread` → `Thread.startVirtualThread`                                     | 功能正确性              |

### 全局配置建议

建议新增一个全局配置类，统一提供虚拟线程执行器：

```java
@Configuration
public class VirtualThreadConfig {
    
    @Bean(name = {"virtualTaskExecutor", "taskExecutor"})
    public Executor virtualTaskExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }
    
    @Bean
    public AsyncConfigurer asyncConfigurer() {
        return new AsyncConfigurer() {
            @Override
            public Executor getAsyncExecutor() {
                return virtualTaskExecutor();
            }
        };
    }
}
```

---

## 附录：已使用 Java 21 新特性的良好实践

以下代码已正确使用 Java 16+ 特性，值得继续保持：

1. **`EmptyStringPropertyFilter.java`**（行 26）：

```java
if (value instanceof String str && str.isEmpty()) { ... }
```

2. **`ApplicationConverterConfig.java`**（行 40）：

```java
if (converter instanceof MappingJackson2HttpMessageConverter jacksonConverter) { ... }
```

3. **`ExtendAuthenticationToken.java`**（已使用 `Set.copyOf` / `Map.copyOf`）

---

> **报告生成完毕**。如需进一步执行具体的代码重构，请告知优先处理的模块。
