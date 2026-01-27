package com.carlos.docking.rzt.organization;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.carlos.docking.exception.DockingException;
import com.carlos.docking.rzt.RztService;
import com.carlos.docking.rzt.config.RztConstant;
import com.carlos.docking.rzt.config.RztProperties;
import com.carlos.docking.rzt.organization.result.RztUserInfoResult;
import com.carlos.docking.rzt.organization.result.RztUserPageResult;
import com.carlos.redis.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 蓉政通Organization管理器
 * </p>
 *
 * @author Carlos
 * @date 2023/4/6 10:12
 */
@Slf4j
public class RztOrganizationManager {

    private final RztProperties properties;

    private final FeignRztOrganization feignRztOrganization;
    private static final String SPLIT = "|";
    private static final String ASTERISK = "*";

    public RztOrganizationManager(RztProperties properties, FeignRztOrganization feignRztOrganization) {
        this.properties = properties;
        this.feignRztOrganization = feignRztOrganization;
    }

    public List<RztUserInfoResult> getUser(String userId, String phone) {
        // 判断缓存中是否有token
        if (CharSequenceUtil.isBlank(userId)) {
            userId = ASTERISK;
        }
        if (CharSequenceUtil.isBlank(phone)) {
            phone = ASTERISK;
        }
        String redisKey = String.format(RztConstant.USERS_KEY, userId, phone);
        List<RztUserInfoResult> users = RedisUtil.getValueList(redisKey);
        return users;
    }

    public String getUserIds(String userId, String phone) {
        // 判断缓存中是否有token
        if (CharSequenceUtil.isBlank(userId)) {
            userId = ASTERISK;
        }
        if (CharSequenceUtil.isBlank(phone)) {
            phone = ASTERISK;
        }
        String redisKey = String.format(RztConstant.USERS_KEY, userId, phone);
        List<RztUserInfoResult> users = RedisUtil.getValueList(redisKey);
        if (CollUtil.isNotEmpty(users)) {
            List<String> userIds = users.stream().map(RztUserInfoResult::getId).collect(Collectors.toList());
            return String.join(SPLIT, userIds);
        }
        return CharSequenceUtil.EMPTY;
    }


    public String getPhoneById(String userId) {
        // 判断缓存中是否有token
        if (CharSequenceUtil.isBlank(userId)) {
            return CharSequenceUtil.EMPTY;
        }
        String redisKey = String.format(RztConstant.USERS_KEY, userId, ASTERISK);
        List<RztUserInfoResult> users = RedisUtil.getValueList(redisKey);
        if (CollUtil.isNotEmpty(users)) {
            RztUserInfoResult rztUserInfoResult = users.get(0);
            return rztUserInfoResult.getUserName();
        }
        // return StrPool.COMMA;
        return CharSequenceUtil.EMPTY;
    }

    /**
     * 缓存 用户信息
     *
     * @author Carlos
     * @date 2023/4/7 14:53
     */
    public void cacheUsers(int startIndex, int count, String filter, String searchType) {
        RztService rztService = SpringUtil.getBean(RztService.class);
        // 获取蓉政通所有用户信息到缓存
        RztProperties.OrganizationProperties organization = properties.getOrganization();
        log.info("organization:{}", organization);
        // 添加对应接口特定headers
        // 构建请求头Map
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("x-rio-paasid", organization.getPaasId());
        long timestamp = DateUtil.currentSeconds();
        int nonce = RandomUtil.randomInt(1000, 9999);
        headers.add("x-rio-timestamp", String.valueOf(timestamp));
        headers.add("x-rio-nonce", String.valueOf(nonce));
        String sign = timestamp + organization.getPaasToken() + nonce + timestamp;
        String signature = DigestUtil.sha256Hex(sign).toUpperCase();
        headers.add("x-rio-signature", signature);
        // 毫秒
        long timestamp2 = DateUtil.current();
        String sign2 = organization.getAppId() + organization.getAppSecret() + timestamp2;
        String signature2 = DigestUtil.sha256Hex(sign2);
        headers.add("X-App-Token", signature2);
        headers.add("X-App-Id", organization.getAppId());
        headers.add("X-Timestamp", String.valueOf(timestamp2));
        headers.add("Content-Type", "application/json");
        log.info("headers:{}", headers);
        int total = 0;
        boolean flag = true;
        RztUserPageResult<RztUserInfoResult> result;
        do {
            try {
                result = feignRztOrganization.getUsers(headers, startIndex, count, filter, searchType);
                log.info("feign get users result:{}", result);
                rztService.checkPageResult(result);
            } catch (Exception e) {
                log.error("get users failed: ", e);
                throw new DockingException(e);
            }

            List<RztUserInfoResult> users = result.getResources();
            Integer totalResults = result.getTotalResults();
            if (totalResults == null) {
                log.error("get users totalResults:{} ", totalResults);
                break;
            }
            if (CollUtil.isNotEmpty(users)) {
                for (RztUserInfoResult user : users) {
                    // 写入缓存
                    String redisKey = String.format(RztConstant.USERS_KEY, user.getId(), user.getUserName());
                    RedisUtil.setValue(redisKey, user);
                }
                total = total + users.size();
            }
            if (startIndex + count < totalResults) {
                startIndex = startIndex + count;
            } else {
                flag = false;
            }
        } while (flag);
        log.info("Rzt 用户加载完成, 共缓存：{}", total);
    }
}
