package com.carlos.mongodb.config;

import com.carlos.core.interfaces.ApplicationExtend;
import com.carlos.mongodb.AbstractMybatisCommonField;
import com.carlos.mongodb.DefaultMetaObjectHandler;
import com.carlos.mongodb.MetaObjectHandler;
import com.carlos.mongodb.MybatisCommonField;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.lang.Nullable;

/**
 * <p>
 * MongoDB 自动配置类
 * </p>
 *
 * @author carlos
 * @date 2024/01/15
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(MongoTemplate.class)
@ConditionalOnProperty(prefix = "carlos.mongodb", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(MongoProperties.class)
@RequiredArgsConstructor
public class MongoAutoConfiguration {

    private final MongoProperties mongoProperties;

    /**
     * 默认的通用字段配置
     */
    @Bean
    @ConditionalOnMissingBean
    public MybatisCommonField mybatisCommonField() {
        return new DefaultMongoCommonField(mongoProperties);
    }

    /**
     * 默认的元对象处理器（字段自动填充）
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "carlos.mongodb", name = "field-fill-enabled", havingValue = "true", matchIfMissing = true)
    public MetaObjectHandler defaultMetaObjectHandler(@Nullable @Lazy ApplicationExtend requestExtend,
                                                      MybatisCommonField commonField) {
        log.debug("Initializing DefaultMetaObjectHandler for MongoDB");
        return new DefaultMetaObjectHandler(requestExtend, commonField);
    }

    /**
     * 默认的 MongoDB 通用字段实现
     */
    public static class DefaultMongoCommonField extends AbstractMybatisCommonField {

        private final MongoProperties properties;

        public DefaultMongoCommonField(MongoProperties properties) {
            this.properties = properties;
        }

        @Override
        public String primaryKeyFiledName() {
            return DEFAULT_PRIMARY_KEY_FILED_NAME;
        }

        @Override
        public String updateTimeFiledName() {
            return properties.getUpdateTimeField();
        }

        @Override
        public String updateUserFiledName() {
            return properties.getUpdateByField();
        }

        @Override
        public String createTimeFiledName() {
            return properties.getCreateTimeField();
        }

        @Override
        public String createUserFiledName() {
            return properties.getCreateByField();
        }

        @Override
        public String logicDeleteFiledName() {
            return properties.getDeletedField();
        }

        @Override
        public String versionFiledName() {
            return properties.getVersionField();
        }
    }
}
