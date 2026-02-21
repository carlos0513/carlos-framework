package com.yunjin.docking.tftd;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.yunjin.docking.exception.DockingException;
import com.yunjin.docking.tftd.config.TfAuthProperties;
import com.yunjin.docking.tftd.exception.DockingRequestTfAuthException;
import com.yunjin.docking.tftd.param.DeptListParam;
import com.yunjin.docking.tftd.param.UserListParam;
import com.yunjin.docking.tftd.result.*;
import com.yunjin.org.service.UserImportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
public class TfAuthService {

    private final FeignTfAuth feignTfAuth;
    private final TfAuthProperties properties;
    private static final String INIT_SCHEDULE_REPORT = "INIT_SCHEDULE_REPORT";


    @PostConstruct
    public void init() {
        syncUserInfoTask();
    }

    /**
     * 刷新accessToken任务
     *
     * @author Carlos
     * @date 2023/10/9 14:57
     */
    public void syncUserInfoTask() {
        String jobId = CronUtil.schedule("SYNC-TF-USER-INFO", "0 0 1 * * ?", new Task() {
            @Override
            public void execute() {
                syncUserInfo();
            }
        });
        log.info("Job '{}' has been registered", jobId);
    }

    /**
     * 同步天府用户信息
     *
     * @author Carlos
     * @date 2023/11/2 11:44
     */
    public void syncUserInfo() {
        boolean locked = false;
        RedissonClient redissonClient = SpringUtil.getBean(RedissonClient.class);
        RLock lock = redissonClient.getLock(INIT_SCHEDULE_REPORT);
        try {
            log.info("尝试获取锁：{}", INIT_SCHEDULE_REPORT);
            locked = lock.tryLock(0, 300, TimeUnit.SECONDS);
            log.info("获取锁是否成功：{}", locked);
            if(locked) {
                log.info("获取锁成功,开始刷新accessToken任务");
                log.info("start sync tf user info:date:{}", new Date());
                AccessTokenResult clientToken = getClientToken();
                String accessToken = clientToken.getAccessToken();
                accessToken = "Bearer " + accessToken;

                String clientId = properties.getClientId();
                String tenantId = properties.getTenantId();
                DeptListParam deptListParam = new DeptListParam();
                TfAuthResult<List<TfDeptInfoResult>> deptInfo = feignTfAuth.deptList(accessToken, tenantId, deptListParam);
                checkResult(deptInfo);
                List<TfDeptInfoResult> depts = deptInfo.getData();

                UserListParam userListParam = new UserListParam();
                TfAuthResult<List<TfUserInfoResult>> userInfo = feignTfAuth.userList(accessToken, tenantId, userListParam);
                checkResult(userInfo);
                List<TfUserInfoResult> users = userInfo.getData();
                if(CollUtil.isEmpty(users)) {
                    log.warn("Tf user info is null");
                    return;
                }

                // List<TfDeptInfoResult> depts = JSONUtil.toList(JSONUtil.readJSONArray(new File("D:\\log\\dept.json"), StandardCharsets.UTF_8),
                //         TfDeptInfoResult.class);
                // List<TfUserInfoResult> users = JSONUtil.toList(JSONUtil.readJSONArray(new File("D:\\log\\user.json"), StandardCharsets.UTF_8),
                //         TfUserInfoResult.class);
                UserImportService userImportService = SpringUtil.getBean(UserImportService.class);
                try {
                    userImportService.syncTfUser(depts, users);
                } catch(Exception e) {
                    log.error("tf sync user info failed: message:{}", e.getMessage(), e);
                }
            } else {
                // 获取锁失败，说明其他实例正在执行，直接返回
                log.info("未获取到分布式锁，不执行刷新accessToken任务");
            }
        } catch(InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("获取分布式锁时被中断", e);
        } catch(Exception e) {
            log.error("刷新accessToken任务异常", e);
        } finally {
            // 如果当前线程持有锁，则释放锁
            if(locked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }


    /**
     * 获取token
     *
     * @param code 前端获取的code
     * @return com.yunjin.docking.tftd.result.AccessTokenResult
     * @author Carlos
     * @date 2023/7/10 13:34
     */
    public AccessTokenResult getAccessToken(String code) {
        try {
            String basicAuth = HttpUtil.buildBasicAuth(properties.getClientId(), properties.getClientSecret(), StandardCharsets.UTF_8);

            AccessTokenResult result = feignTfAuth.oauthToken(
                    "authorization_code",
                    "bbt",
                    code,
                    URLEncodeUtil.encode(properties.getRedirectUri()),
                    basicAuth,
                    properties.getTenantId()
            );
            log.info("Get accessToken success: result:{}", JSONUtil.toJsonPrettyStr(result));
            return result;
        } catch (Exception e) {
            log.error("Send getAccessToken request failed: message:{}", e.getMessage(), e);
            throw new DockingRequestTfAuthException("获取token失败");
        }
    }


    /**
     * 获取token
     *
     * @return com.yunjin.docking.tftd.result.AccessTokenResult
     * @author Carlos
     * @date 2023/7/10 13:34
     */
    public AccessTokenResult getClientToken() {
        log.info("Tf start get client token, client config:{}", JSONUtil.toJsonPrettyStr(properties));
        try {
            String basicAuth = HttpUtil.buildBasicAuth(properties.getClientId(), properties.getClientSecret(), StandardCharsets.UTF_8);

            AccessTokenResult result = feignTfAuth.oauthToken(
                    "client_credentials",
                    null,
                    null,
                    null,
                    basicAuth,
                    properties.getTenantId()
            );
            log.info("Tf Get client accessToken success: result:{}", JSONUtil.toJsonPrettyStr(result));
            return result;
        } catch (Exception e) {
            log.error("Send getAccessToken request failed: message:{}", e.getMessage(), e);
            throw new DockingRequestTfAuthException("获取token失败");
        }
    }


    /**
     * 获取用户信息
     *
     * @param token 通过code获取的token
     * @return com.yunjin.docking.tftd.result.UserInfoResult
     * @author Carlos
     * @date 2023/7/10 13:37
     */
    public TfOauthUserInfoResult getUserInfo(String token) {
        if (StrUtil.isBlank(token)) {
            throw new DockingException("token can't be null");
        }
        try {
            TfAuthResult<TfOauthUserInfoResult> result = feignTfAuth.getUserInfo(token, properties.getTenantId());
            log.info("feign get user info result:{}", result);
            checkResult(result);
            return result.getData();
        } catch (Exception e) {
            throw new DockingException(e);
        }
    }

    private void checkResult(TfAuthResult result) {
        String code = result.getCode();
        if (StrUtil.isNotBlank(code)) {
            String errmsg = result.getMsg();

            if (code != null) {
                if (!code.equals("0")) {
                    log.error("tfAuth api result error: message:{}", errmsg);
                    throw new DockingException(errmsg);
                }
            } else {
                log.error("TfAuth Service response error: errorCode:{}, errMsg:{}", code, errmsg);
                throw new DockingException(errmsg);
            }
        }
    }
}
