package com.carlos.oauth.support.thirdparty;

import com.carlos.core.auth.LoginUserInfo;

/**
 * 第三方登录服务接口
 *
 * <p>定义第三方平台（微信、钉钉、短信等）登录的标准流程。</p>
 *
 * <h3>实现类：</h3>
 * <ul>
 *   <li>{@link WechatLoginService} - 微信登录</li>
 *   <li>{@link DingtalkLoginService} - 钉钉登录</li>
 *   <li>{@link SmsLoginService} - 短信验证码登录</li>
 * </ul>
 *
 * <h3>使用方式：</h3>
 * <pre>{@code
 * @Service
 * public class WechatLoginServiceImpl implements WechatLoginService {
 *
 *     @Autowired
 *     private WxMpService wxMpService;
 *
 *     @Override
 *     public LoginUserInfo login(String code) {
 *         // 1. 用 code 换取 access_token 和 openid
 *         WxMpOAuth2AccessToken token = wxMpService.oauth2getAccessToken(code);
 *
 *         // 2. 获取用户信息
 *         WxMpUser wxUser = wxMpService.oauth2getUserInfo(token, null);
 *
 *         // 3. 查找或创建本地用户
 *         User user = userMapper.findByWechatOpenId(wxUser.getOpenId());
 *         if (user == null) {
 *             user = createUserFromWechat(wxUser);
 *         }
 *
 *         // 4. 返回登录信息
 *         return convertToLoginUserInfo(user);
 *     }
 *
 *     @Override
 *     public ThirdPartyType getType() {
 *         return ThirdPartyType.WECHAT;
 *     }
 * }
 * }</pre>
 *
 * @author carlos
 * @version 3.0.0
 * @since 2026-02-22
 */
public interface ThirdPartyLoginService {

    /**
     * 执行第三方登录
     *
     * <p>根据第三方平台返回的授权码或凭证，获取用户信息并完成登录。</p>
     *
     * <h3>登录流程：</h3>
     * <ol>
     *   <li>使用授权码换取访问令牌</li>
     *   <li>使用访问令牌获取用户信息</li>
     *   <li>查找或创建本地用户绑定</li>
     *   <li>返回统一的用户信息</li>
     * </ol>
     *
     * @param authCode 授权码或凭证
     *                 <ul>
     *                   <li>微信：code（需先通过前端获取）</li>
     *                   <li>钉钉：code 或 access_token</li>
     *                   <li>短信：手机号:验证码 格式</li>
     *                 </ul>
     * @return LoginUserInfo 登录用户信息，登录失败返回 null
     * @throws ThirdPartyLoginException 第三方登录异常
     */
    LoginUserInfo login(String authCode);

    /**
     * 验证授权码是否有效
     *
     * <p>在实际登录前验证授权码格式或向第三方平台验证有效性。</p>
     *
     * @param authCode 授权码
     * @return boolean 是否有效
     */
    default boolean validateAuthCode(String authCode) {
        return authCode != null && !authCode.isEmpty();
    }

    /**
     * 获取第三方平台类型
     *
     * @return ThirdPartyType 平台类型
     */
    ThirdPartyType getType();

    /**
     * 解绑用户
     *
     * <p>解除用户与第三方平台的绑定关系。</p>
     *
     * @param userId 本地用户ID
     * @return boolean 是否解绑成功
     */
    default boolean unbind(Long userId) {
        return false;
    }

    /**
     * 检查用户是否已绑定
     *
     * @param userId 本地用户ID
     * @return boolean 是否已绑定
     */
    default boolean isBound(Long userId) {
        return false;
    }
}
