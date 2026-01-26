package com.yunjin.datasource.config;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.yunjin.snowflake.SnowflakeUtil;
import com.yunjin.util.IdUtils;


/**
 * <p>
 * 自定义32位id生成器
 * </p>
 *
 * @author yunjin
 * @date 2021/12/13 18:21
 */
public class CustomizeIdGenerator implements IdentifierGenerator {


    @Override
    public Number nextId(Object entity) {
        return SnowflakeUtil.longId();
    }

    @Override
    public String nextUUID(Object entity) {
        return IdUtils.date32Id();
    }


}
