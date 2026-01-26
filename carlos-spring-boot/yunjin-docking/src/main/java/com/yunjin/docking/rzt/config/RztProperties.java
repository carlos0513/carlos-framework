package com.yunjin.docking.rzt.config;

import cn.hutool.json.JSONUtil;
import com.yunjin.docking.config.FeignBaseProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * <p>
 * 蓉政通属性配置
 * </p>
 *
 * @author Carlos
 * @date 2023/4/6 9:34
 */
@Slf4j
@Data
@ConfigurationProperties(prefix = "yunjin.docking.rzt")
public class RztProperties implements InitializingBean {


    /**
     * 接口信息
     */
    private FeignBaseProperties api = new FeignBaseProperties();


    /**
     * corpid：每个单位都拥有唯一的corpid，获取此信息可在管理后台“我的单位”－“单位信息”下查看（需要有管理员权限）。 secret：secret是单位应用里面用于保障数据安全的“钥匙”，每一个应用都有一个独立的访问密钥，为了保证数据的安全。 目前secret有两种：1.
     * 通讯录管理secret； 2. 应用secret。 通讯录管理secret。在“管理工具”-“通讯录同步及内部登录集成”里面查看（需开启“API接口同步”）； 应用secret。在管理后台->“单位应用”->点进应用，即可看到。
     */
    private String corpid;

    /**
     * 未加密密码
     */
    private String secret;

    /**
     * paasId
     */
    private String paasId;

    /**
     * paasToken
     */
    private String paasToken;

    /**
     * agentId
     */
    private Integer agentId;
    /**
     * AccessToken过期时间
     */
    private Duration tokenDuration;
    /**
     * 是否开启debug模式
     */
    private Boolean debug = false;
    /**
     * token刷新时间
     */
    private String accessTokenRefreshCorn = "0 0/59 * * * *";

    /**
     * token刷新时间
     */
    private String userCacheRefreshCorn = "0 0 3 * * ?";

    /**
     * 用户/组织机构信息
     */
    private OrganizationProperties organization = new OrganizationProperties();

    @Override
    public void afterPropertiesSet() throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("rzt config:{}", JSONUtil.toJsonPrettyStr(this));
        }
        api.check();
    }

    /**
     * 用户/组织机构对接
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class OrganizationProperties extends FeignBaseProperties implements InitializingBean {

        /**
         * 默认关闭
         */
        private boolean enabled = false;
        /**
         * paasId
         */
        private String paasId;

        /**
         * paasToken
         */
        private String paasToken;
        /**
         * appId
         */
        private String appId;

        /**
         * appSecret
         */
        private String appSecret;


        @Override
        public void afterPropertiesSet() throws Exception {
            check();
        }


    }

}
