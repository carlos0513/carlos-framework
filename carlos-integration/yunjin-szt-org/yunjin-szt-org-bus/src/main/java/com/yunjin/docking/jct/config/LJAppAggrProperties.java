package com.yunjin.docking.jct.config;

import cn.hutool.json.JSONUtil;
import com.yunjin.docking.config.FeignBaseProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * 黑龙江一体化平台
 * </p>
 *
 * @author Carlos
 * @date 2023/4/6 9:34
 */
@Slf4j
@Data
@ConfigurationProperties(prefix = "yunjin.docking.jct")
public class LJAppAggrProperties implements InitializingBean {

    private boolean enabled = false;
    /**
     * 接口信息
     */
    private FeignBaseProperties api = new FeignBaseProperties();


    private String appSign;

    private String defaultDeptId;

    private String defaultRoleId;


    @Override
    public void afterPropertiesSet() throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Hei Longjiang app aggr config:{}", JSONUtil.toJsonPrettyStr(this));
        }
    }
}
