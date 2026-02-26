# 第三方OAuth集成与扫码登录实现指南

## 一、第三方OAuth登录集成（Phase 6.1）

### 1.1 架构设计

提供可扩展的第三方OAuth提供者框架，便于新增OAuth服务：

**接口定义：**

```java
public interface ThirdPartyOAuthProvider {
    // 支持多种OAuth类型
    OAuthProviderType getProviderType();
    // 生成授权URL
    String generateAuthorizationUrl(String state);
    // 使用授权码获取用户信息
    OAuthUserInfo getUserInfo(String authorizationCode);
}
```

**OAuth用户基本信息实体：**

```java
@Getter
@Builder
@AllArgsConstructor
public class OAuthUserInfo {
    // 唯一标识
    private String openId;
    // 全局唯一标识
    private String unionId;
    // 用户昵称
    private String nickname;
    // 应用接入的用户唯一标识
    private String appUserId;
}
```

**用户绑定数据表结构：**

```sql
CREATE TABLE user_third_party_bind (
    id BIGINT PRIMARY KEY COMMENT '绑定记录唯一标识',
    user_id BIGINT NOT NULL COMMENT '平台内部用户标识',
    provider VARCHAR(20) NOT NULL COMMENT '第三方平台类型',
    provider_user_id VARCHAR(100) NOT NULL COMMENT '第三方平台用户标识',
    access_token TEXT COMMENT '访问令牌',
    refresh_token TEXT COMMENT '刷新令牌',
    expires_in INT COMMENT '令牌有效期',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_provider_user (provider, provider_user_id)
);
```

### 1.2 企业应用示例

以企业微信为例：

```java
public class WeChatWorkOAuthProvider implements ThirdPartyOAuthProvider {
    // 微信 OAuth 集成细节
    @Override
    public String generateAuthorizationUrl(String state) {
        return "https://open.work.weixin.qq.com/wwopen/sso/3rd_qrConnect?appid=" + corpId + "&redirect_uri=" + redirectUri + "&state=" + state;
    }

    @Override
    public OAuthUserInfo getUserInfo(String authorizationCode) {
        // 使用授权码获取访问令牌
        String accessTokenUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token={" + accessToken + "}&code={" + authorizationCode + "}";
        // 实现详情处理企业内部授权逻辑
        return new OAuthUserInfo(openid, userid, name, appUserId);
    }
}
```

## 二、扫码登录实现指南（Phase 6.2）

### 2.1 二维码状态转换机制

**二维码状态设计：**

```java
public enum QRCodeStatus {
    PENDING(0, "待扫描"),
    SCANNED(1, "已扫描"),
    CONFIRMED(2, "已确认"),
    EXPIRED(3, "已过期"),
    CANCELLED(4, "已取消");

    private final Integer code;
    private final String description;
}
```

**二维码数据存储策略：**

```java
public class QRLoginService {
    // Redis 中存储二维码状态
    private static final String QR_CODE_PREFIX = "auth:qr:";

    // WebSocket 实现实时状态更新
    public void handleQRCodeScan(String sessionId, String userToken) {
        QRCodeStatus status = redis.get(QR_CODE_PREFIX + sessionId);
        if (!QRCodeStatus.PENDING.equals(status)) {
            throw new QRCodeException("二维码状态异常");
        }

        // 更新二维码状态
        redis.set(QR_CODE_PREFIX + sessionId, QRCodeStatus.SCANNED);

        // WebSocket 推送扫描通知
        webSocketHandler.sendNotification(sessionId, "二维码已扫描");
    }
}
```

### 2.2 二维码登录流程时序图

```
PC 端                      服务端                      App 端
  |                           |                           |
  |-----> /qr/generate        |                           |
  |<- 返回 QR Code  + Token   |                           |
  |                           |                           |
  |        轮询 =---------->  |                           |
  |                           |                           |
  |                           |<- App 扫描二维码          |
  |                           |                           |
  |                           |-- 确认登录请求            |
  |        状态变更通知       |                           |
  |<-------------------------|                           |
  |         登录成功          |                           |
```

## 三、安全合规建议

### 3.1 数据保护措施

- 实施严格的数据解码校验机制
- 使用OAuth 2.0时的state参数防止跨站请求伪造
- 敏感操作实行二次身份验证
- 对用户隐私信息采用字段级加密

### 3.2 监控与审计

- 配置异常登录风险识别模型
- 按地区、时间、设备类型等多维度风险评估
- 建立安全事件快速响应机制
- 持续更新安全威胁情报库

## 四、配置模板

### application-oauth.yml

```yaml
third-party:
  oauth:
    wechat:
      corp-id: ${WECHAT_CORP_ID}
      agent-id: ${WECHAT_AGENT_ID}
      secret: ${WECHAT_SECRET}
      redirect-uri: ${WECHAT_REDIRECT_URI}

    dingtalk:
      client-id: ${DINGTALK_CLIENT_ID}
      client-secret: ${DINGTALK_CLIENT_SECRET}
      redirect-uri: ${DINGTALK_REDIRECT_URI}

    github:
      client-id: ${GITHUB_CLIENT_ID}
      client-secret: ${GITHUB_CLIENT_SECRET}
      redirect-uri: ${GITHUB_REDIRECT_URI}
```

### application-security.yml

```yaml
security:
  qr-login:
    timeout: 300
    max-attempts: 3
    enable-websocket: true
    verification-mode: device-fingerprint

  third-party-oauth:
    enable-multifactor-protection: true
    require-verified-email: true
    auto-register: false
```

## 五、性能优化建议

1. **OAuth服务调用**
    - 实现OAuth访问令牌本地缓存策略
    - 建立OAuth服务降级机制
    - 使用连接池优化HTTP请求

2. **二维码生成与验证**
    - Redis集群部署支持高并发
    - 实现二维码生成速率限制
    - 支持二维码状态批量查询

3. **负载均衡**
    - OAuth回调服务支持多实例部署
    - WebSocket服务实现多节点同步
    - 配置健康检查机制

## 六、测试策略

### 6.1 OAuth集成测试

- 模拟不同OAuth1.0/2.0协议的第三方验证流程
- 异常场景处理（如网络中断、令牌过期）
- 并发授权请求的性能基准测试

### 6.2 二维码登录测试

- 跨设备扫描功能验证
- 二维码状态变更的实时性测试
- 高并发二维码生成与验证压力测试

### 6.3 安全测试

- 授权重放攻击防护测试
- CSRF攻击防护
- 短信验证码发送频率控制验证

## 七、维护与扩展

### 7.1 增加新的OAuth支持

1. 创建对应Provider实现类
2. 在OAuthProviderFactory中注册新Provider
3. 在application.yml中新增配置项
4. 编写单元测试和集成测试

### 7.2 二维码功能扩展

- 支持动态有效期配置
- 实现设备指纹增强验证
- 支持第三方设备扫描（如小程序）
- 提供二维码扫描分析统计功能

# 下一阶段集成重点

1. 完善OAuth集成测试用例
2. 实现企业微信OAuth和扫码登录完整Demo
3. 对接公司内部的用户中心
4. 实现扫码登录的设备指纹保护
5. 加入安全告警模块的通知渠道集成
