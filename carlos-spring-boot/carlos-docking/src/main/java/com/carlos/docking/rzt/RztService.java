package com.carlos.docking.rzt;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.carlos.docking.exception.DockingException;
import com.carlos.docking.rzt.config.RztAccessTokenManager;
import com.carlos.docking.rzt.config.RztProperties;
import com.carlos.docking.rzt.exception.DockingRztException;
import com.carlos.docking.rzt.organization.FeignRztOrganization;
import com.carlos.docking.rzt.organization.RztOrganizationManager;
import com.carlos.docking.rzt.organization.result.RztPageResult;
import com.carlos.docking.rzt.param.RztRevokeMessageParam;
import com.carlos.docking.rzt.param.RztSendMessageParam;
import com.carlos.docking.rzt.param.RztSendMessageParam.TextMessage;
import com.carlos.docking.rzt.result.MessageSendResult;
import com.carlos.docking.rzt.result.RztResult;
import com.carlos.docking.rzt.result.UserIdResult;
import com.carlos.docking.rzt.result.UserInfoResult;
import com.carlos.docking.rzt.user.RztUserCache;
import com.carlos.docking.rzt.user.RztUserManager;
import com.carlos.docking.rzt.user.SysUserInfo;
import com.carlos.json.jackson.JacksonUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 引擎相关服务
 * </p>
 *
 * @author Carlos
 * @date 2022/2/23 18:05
 */
@Slf4j
@RequiredArgsConstructor
public class RztService {

    private final FeignRzt feignRzt;
    private final FeignRztOrganization feignRztOrganization;

    private final RztAccessTokenManager tokenManager;
    private final RztOrganizationManager organizationManager;
    private final RztProperties properties;

    public static final String SPLIT = "|";

    Cache<String, RztUserCache> CACHE = CacheBuilder.newBuilder()
            .initialCapacity(500) // 初始容量
            .maximumSize(100000)    // 最大容量，超过时按LRU算法清除缓存项
            .concurrencyLevel(8)  // 并发级别，用于并发访问控制
            // .expireAfterWrite(2, TimeUnit.DAYS) // 写入后过期时间，单位为分钟
            .build();

    @PostConstruct
    public void init() {
        // 加载用户信息到缓存
        RztUserManager userManager = SpringUtil.getBean(RztUserManager.class);
        if (userManager == null) {
            log.warn("用户管理器未注册，蓉政通系统可能无法正常使用");
            return;
        }
        List<SysUserInfo> users = userManager.load();
        if (CollUtil.isNotEmpty(users)) {
            for (SysUserInfo user : users) {
                String phone = user.getPhone();
                if (StrUtil.isBlank(phone)) {
                    log.warn("用户手机号为空，无法进行缓存， userid:{}", user.getId());
                }
                String key = DigestUtil.sha256Hex(phone);
                CACHE.put(key, new RztUserCache(null, user));
            }
        }
        log.info("Rzt 系统用户加载完成");
    }


    public String getRztUsers(String userId, String phone) {
        return organizationManager.getUserIds(userId, phone);
    }

    public void updateRztUsers(String filter, String searchType) {
        organizationManager.cacheUsers(1, 200, filter, searchType);
    }


    /**
     * 推送师市级任务执行数据详情
     *
     * @author Carlos
     * @date 2023/10/9 14:56
     */
    public void cacheUsers() {
        organizationManager.cacheUsers(1, 200, "", "");
    }

    /**
     * 获取用户id
     *
     * @param code 通过成员授权获取到的code，每次成员授权带上的code将不一样，code只能使用一次，5分钟未被使用自动过期
     * @return com.carlos.app.rzt.api.result.UserIdResult
     * @author Carlos
     * @date 2023/4/7 16:04
     */
    public UserIdResult getUserId(String code) {
        if (StrUtil.isBlank(code)) {
            throw new DockingException("code can't be null");
        }
        UserInfoResult result;
        try {
            result = feignRzt.getLoginInfo(getHeaders(), getToken(), code);
            log.info("feign get user id result:{}", result);
            checkResult(result);
        } catch (Exception e) {
            log.error("get userid failed: ", e);
            throw new DockingException(e);
        }
        // TODO 替换缓存方式
        //String key = result.getMobileEncrypt();
        //RztUserCache userCache = CACHE.getIfPresent(key);
        //if (userCache == null) {
        //    throw new DockingRztException("系统用户不存在");
        //}
        //userCache.setRztUser(result);
        //CACHE.put(key, userCache);
        //SysUserInfo sysUserInfo = userCache.getSysUserInfo();
        //return new UserIdResult().setUserId(sysUserInfo.getPhone());
        String phone = getPhoneByResult(result);
        // 更新缓存并获取手机号
        if (CharSequenceUtil.isBlank(phone)) {
            // 拼接过滤条件
            String filter = "displayName eq " + result.getName();
            updateRztUsers(filter, "");
            phone = getPhoneByResult(result);
        }
        return new UserIdResult().setUserId(phone);
    }

    /**
     * @Title: getPhoneByResult
     * @Description: 根据用户登录信息获取缓存手机号
     * @Date: 2025/4/10 16:24
     * @Parameters: [result]
     * @Return java.lang.String
     */
    @Nullable
    private String getPhoneByResult(UserInfoResult result) {
        String phone = organizationManager.getPhoneById(result.getUserid());
        if (CharSequenceUtil.isBlank(phone) && CollUtil.isNotEmpty(result.getUserids())) {
            List<String> userids = result.getUserids();
            for (String userid : userids) {
                phone = organizationManager.getPhoneById(userid);
                if (CharSequenceUtil.isNotBlank(phone)) {
                    break;
                }
            }

        }
        return phone;
    }

    /**
     * 获取用户信息
     *
     * @param userId 用户id
     * @return com.carlos.app.rzt.api.result.UserInfoResult
     * @author Carlos
     * @date 2023/4/7 16:40
     */
    public UserInfoResult getUserInfo(String userId) {
        if (StrUtil.isBlank(userId)) {
            throw new DockingException("userid can't be null");
        }
        try {
            UserInfoResult result = feignRzt.getUserInfo(getHeaders(), getToken(), userId);
            log.info("feign get user info result:{}", result);
            checkResult(result);
            return result;
        } catch (Exception e) {
            throw new DockingException(e);
        }
    }

    /**
     * 获取accessToken
     *
     * @author Carlos
     * @date 2023/4/7 16:06
     */
    private String getToken() {
        return tokenManager.getAccessToken();
    }

    /**
     * 生成请求头
     * 1.x-rio-paasid:固定值，见参数表
     * 2.x-rio-timestamp: 当前时间的时间戳（以秒为单位）
     * 3.x-rio-nonce: 随机串，不超过36位
     * 4.x-rio-signature: 使用CryptoJS库来计算SHA-256哈希值。将“时间戳”+  “paastoken”+ “随机数”+ “时间戳”拼接成一个字符串，
     * 然后计算这个字符串的SHA-256摘要。结果转换为十六进制字符串，并转换为大写形式。
     *
     * @return java.lang.String
     * @author Carlos
     * @date 2024-12-09 19:55
     */
    public MultiValueMap<String, String> getHeaders() {
        String paasToken = properties.getPaasToken();
        // 构建请求头Map
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("x-rio-paasid", properties.getPaasId());
        long timestamp = DateUtil.currentSeconds();
        int nonce = RandomUtil.randomInt(1000, 9999);
        headers.add("x-rio-timestamp", String.valueOf(timestamp));
        headers.add("x-rio-nonce", String.valueOf(nonce));
        String sign = timestamp + paasToken + nonce + timestamp;
        String signature = DigestUtil.sha256Hex(sign).toUpperCase();
        headers.add("x-rio-signature", signature);
        log.info("headers:{}", headers);
        return headers;
    }

    /**
     * 发送文本消息
     *
     * @param users   用户id
     * @param party   部门id
     * @param tags    标签id
     * @param content 消息内容
     * @return com.carlos.docking.rzt.result.MessageSendResult
     * @throws
     * @author Carlos
     * @date 2024-10-28 16:14
     */
    public MessageSendResult sendTextMessage(Set<String> users, Set<String> party, Set<String> tags, String content) {
        RztSendMessageParam param = new RztSendMessageParam();

        if (StrUtil.isBlank(content)) {
            throw new DockingException("content can't be null");
        }
        if (content.getBytes().length > 2048) {
            throw new DockingException("content length can't be greater than 2048");
        }
        if (CollUtil.isNotEmpty(users)) {
            if (users.size() > 1000) {
                throw new DockingException("users size can't be greater than 1000");
            }
            List<String> userIds = phoneToUserId(Lists.newArrayList(users));
            param.setTouser(CollUtil.join(userIds, SPLIT));
        }
        if (CollUtil.isNotEmpty(party)) {
            if (party.size() > 100) {
                throw new DockingException("party size can't be greater than 100");
            }
            param.setToparty(CollUtil.join(party, SPLIT));
        }
        if (CollUtil.isNotEmpty(tags)) {
            if (tags.size() > 100) {
                throw new DockingException("tags size can't be greater than 100");
            }
            param.setTotag(CollUtil.join(tags, SPLIT));
        }

        param.setAgentid(properties.getAgentId());
        param.setText(new TextMessage(content));
        param.setMsgtype("text");
        MultiValueMap<String, String> headers = getHeaders();
        headers.set("Content-Type", "application/json;charset=UTF-8");
        MessageSendResult result = null;
        try {
            param.setAccessToken(getToken());
            String message = JacksonUtil.toJsonString(param);
            log.info("send massage,{}", message);
            result = feignRzt.sendMessage(headers, message);
            log.info("feign send message result:{}", result);
        } catch (Exception e) {
            log.error("send message failed: ", e);
            throw new DockingException(e);
        }
        checkResult(result);
        return result;
    }

    /**
     * 手机号转用户id
     *
     * @param phones 手机号
     * @return java.util.List<java.lang.String>
     * @author Carlos
     * @date 2024-12-13 16:42
     */
    private List<String> phoneToUserId(List<String> phones) {
        if (CollUtil.isEmpty(phones)) {
            return null;
        }
        List<String> userIds = Lists.newArrayList();
        List<String> notExistUserPhones = Lists.newArrayList();
        for (String phone : phones) {
            // TODO 替换缓存获取方式
            //String key = DigestUtil.sha256Hex(phone);
            //RztUserCache userCache = CACHE.getIfPresent(key);
            //if (userCache == null) {
            //    throw new DockingRztException("系统用户不存在");
            //}
            //UserInfoResult rztUser = userCache.getRztUser();
            //if (rztUser == null) {
            //    throw new DockingRztException("蓉政通用户不存在");
            //}
            //userIds.add(rztUser.getUserid());
            String userId = organizationManager.getUserIds(null, phone);
            if (CharSequenceUtil.isBlank(userId)) {
                notExistUserPhones.add(phone);
                continue;
            }
            userIds.add(userId);
        }
        if (CollUtil.isNotEmpty(notExistUserPhones)) {
            log.error("rzt user  does not exist:{}", notExistUserPhones);
        }
        if (CollUtil.isEmpty(userIds)) {
            throw new DockingRztException("蓉政通用户不存在:" + String.join(StrPool.COMMA, phones));
        }
        return userIds;
    }

    private void checkResult(RztResult result) {
        String errcode = result.getErrcode();
        if (StrUtil.isNotBlank(errcode)) {
            String errmsg = result.getErrmsg();
            ErrorCodeEnum code = ErrorCodeEnum.ofCode(errcode);
            if (code != null) {
                if (code != ErrorCodeEnum.A_002) {
                    log.error("rzt api result error: message:{}", code);
                    throw new DockingException(code.getName());
                }
            } else {
                log.error("Rzt Service response error: errorCode:{}, errMsg:{}", errcode, errmsg);
                throw new DockingException(errmsg);
            }
        }
    }

    public void checkPageResult(RztPageResult result) {
        String errcode = result.getErrorCode();
        if (CharSequenceUtil.isNotBlank(errcode)) {
            String errmsg = result.getErrorMsg();
            ErrorCodeEnum code = ErrorCodeEnum.ofCode(errcode);
            if (code != null) {
                if (code != ErrorCodeEnum.A_002) {
                    log.error("rzt api result error: message:{}", code);
                    throw new DockingException(code.getName());
                }
            } else {
                log.error("Rzt Service response error: errorCode:{}, errMsg:{}", errcode, errmsg);
                throw new DockingException(errmsg);
            }
        }
    }

    /**
     * 撤回消息
     *
     * @param param 参数0
     * @author Carlos
     * @date 2024-11-18 22:38
     */

    public void revokeMessage(RztRevokeMessageParam param) {
        List<RztRevokeMessageParam.RevokeItem> revokelist = param.getRevokelist();
        if (CollUtil.isEmpty(revokelist)) {
            throw new DockingException("revokelist can't be null");
        }
        for (RztRevokeMessageParam.RevokeItem item : revokelist) {
            String userid = item.getUserid();

            if (StrUtil.isBlank(userid)) {
                throw new DockingException("userid can't be null");
            }
            List<String> id = phoneToUserId(Lists.newArrayList(userid));
            item.setUserid(id.get(0));
            List<String> msgid = item.getMsgid();
            if (CollUtil.isEmpty(msgid)) {
                throw new DockingException("msgid can't be null");
            }
        }
        RztResult result;
        try {
            result = feignRzt.revokeMessage(getHeaders(), getToken(), param);
            log.info("feign revoke message result:{}", result);
        } catch (Exception e) {
            log.error("revoke message failed: ", e);
            throw new DockingException(e);
        }
        checkResult(result);
    }
}
