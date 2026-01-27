package com.carlos.datasource.config;


import com.baomidou.mybatisplus.autoconfigure.IdentifierGeneratorAutoConfiguration;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.carlos.core.interfaces.ApplicationExtend;
import com.carlos.datascope.DataScopeHandler;
import com.carlos.datasource.scope.YunJinDataPermissionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MP配置
 *
 * @author yunjin
 */
@Configuration(proxyBeanMethods = false)
@EnableTransactionManagement
@RequiredArgsConstructor
@AutoConfigureBefore(value = {IdentifierGeneratorAutoConfiguration.class})
public class MyBatisPlusConfig {

    @Bean
    public IdentifierGenerator customizeIdGenerator() {
        return new CustomizeIdGenerator();
    }

    /**
     * 方法注入
     */
    // @Bean
    // public CustomizedSqlInjector customizedSqlInjector() {
    //     return new CustomizedSqlInjector();
    // }

    /**
     * 共同字段名获取
     *
     * @return com.carlos.mybatis.config.MybatisCommonField
     * @author yunjin
     * @date 2021/11/12 10:50
     */
    @Bean
    @ConditionalOnMissingBean
    public MybatisCommonField commonFiled() {
        return new DefaultMybatisCommonField();
    }

    @Bean
    @ConditionalOnMissingBean
    public MetaObjectHandler defaultMetaObjectHandler(@Nullable @Lazy ApplicationExtend requestExtend,
                                                      MybatisCommonField commonField) {
        return new DefaultMetaObjectHandler(requestExtend, commonField);
    }

    /**
     * 插件配置
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(@Nullable DataPermissionHandler dataPermissionHandler) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        if (dataPermissionHandler != null) {
            // 数据权限插件
            interceptor.addInnerInterceptor(new DataPermissionInterceptor(dataPermissionHandler));
        }
        // 租户控制插件
        // interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(tenantLineHandler));
        // 禁全表更删插件
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());

        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }


    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(DataScopeHandler.class)
    @RequiredArgsConstructor
    static class DataPermissionConfig {

        private final DataScopeHandler dataScopeHandler;

        @Bean
        public YunJinDataPermissionHandler dataPermissionHandler() {
            return new YunJinDataPermissionHandler(dataScopeHandler);
        }
    }


}
