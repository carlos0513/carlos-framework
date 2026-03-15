package com.carlos.redis.config;

import com.carlos.redis.serialize.ConfigurableRedisSerializer;
import com.carlos.redis.serialize.RedisSerializerStrategy;
import com.carlos.redis.serialize.SerializerFactory;
import com.carlos.redis.serialize.SerializerType;
import com.carlos.redis.util.CacheKeyGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * <p>
 * 缓存配置
 * </p>
 *
 * @author carlos
 * @date 2020/4/13 13:28
 */
@Slf4j
@Configuration
@EnableCaching
@AllArgsConstructor
@AutoConfigureBefore({RedisLettuceConnectionConfiguration.class, RedisAutoConfiguration.class})
@EnableConfigurationProperties(CacheProperties.class)
public class RedisCacheConfig extends CachingConfigurerSupport {

    private final LettuceConnectionFactory factory;
    private final CacheProperties cacheProperties;

    /**
     * 创建序列化策略
     */
    @Bean
    public RedisSerializerStrategy redisSerializerStrategy() {
        SerializerType type = cacheProperties.getSerializer();
        log.info("Initializing Redis serializer: {} ({})", type.getCode(), type.getDescription());
        return SerializerFactory.getSerializer(type);
    }

    /**
     * 创建 Redis 序列化器
     */
    @Bean
    public ConfigurableRedisSerializer configurableRedisSerializer(RedisSerializerStrategy strategy) {
        return new ConfigurableRedisSerializer(strategy);
    }

    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate(ConfigurableRedisSerializer valueSerializer) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // key 序列化使用 String
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        template.setKeySerializer(keySerializer);
        template.setHashKeySerializer(keySerializer);

        // value 序列化使用配置的序列化器
        template.setValueSerializer(valueSerializer);
        template.setHashValueSerializer(valueSerializer);

        template.afterPropertiesSet();
        log.info("RedisTemplate initialized with {} serializer", valueSerializer.getSerializerType());
        return template;
    }

    @Bean("onlyMasterTemplate")
    public RedisTemplate<String, Object> onlyMasterTemplate(
        @Qualifier("masterOnlyFactory") LettuceConnectionFactory factory,
        ConfigurableRedisSerializer valueSerializer) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        StringRedisSerializer keySerializer = new StringRedisSerializer();
        template.setKeySerializer(keySerializer);
        template.setHashKeySerializer(keySerializer);
        template.setValueSerializer(valueSerializer);
        template.setHashValueSerializer(valueSerializer);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 键序列化器（支持前缀）
     */
    @Bean
    public PrefixKeySerializer keySerializer() {
        return new PrefixKeySerializer(cacheProperties);
    }

    /**
     * 通用方法 key 生成器
     * <p>
     * 修复了原有问题：
     * 1. 使用 Arrays.deepToString 导致 Key 过长
     * 2. 没有长度限制
     * </p>
     */
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> CacheKeyGenerator.generate(
            target, method.getName(), params,
            cacheProperties.getKeyMaxLength(),
            cacheProperties.getKeyOverflowStrategy()
        );
    }

    @Bean
    @Override
    public CacheManager cacheManager() {
        ConfigurableRedisSerializer valueSerializer = configurableRedisSerializer(redisSerializerStrategy());

        // 生成一个默认配置，通过 config 对象即可对缓存进行自定义配置
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
            // 配置注解缓存键值序列化方式
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer))
            // 缓存不失效
            .entryTtl(Duration.ofSeconds(-1));

        // 配置键缓存前缀
        if (cacheProperties.isUsePrefix()) {
            config = config.prefixCacheNameWith(cacheProperties.getKeyPrefix());
        }

        // 使用自定义的缓存配置初始化一个 cacheManager
        return RedisCacheManager.builder(factory)
            .cacheDefaults(config)
            .build();
    }

    /**
     * 自定义缓存异常处理
     * <p>
     * 异常处理策略：
     * 1. 读取异常：记录日志，不抛异常，允许降级到数据库查询
     * 2. 写入异常：记录日志，不影响主业务流程
     * 3. 删除/清除异常：记录日志，不影响主业务流程
     * </p>
     *
     * @return CacheErrorHandler
     */
    @Bean
    @Override
    public CacheErrorHandler errorHandler() {
        return new CacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException e, Cache cache, Object key) {
                log.error("Redis cache get error, cacheName: {}, key: {}", cache.getName(), key, e);
                // 不抛异常，让业务层降级处理
            }

            @Override
            public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value) {
                log.error("Redis cache put error, cacheName: {}, key: {}, value type: {}",
                    cache.getName(), key, value != null ? value.getClass().getName() : "null", e);
                // 不抛异常，缓存更新失败不影响主业务
            }

            @Override
            public void handleCacheEvictError(RuntimeException e, Cache cache, Object key) {
                log.error("Redis cache evict error, cacheName: {}, key: {}", cache.getName(), key, e);
                // 不抛异常，缓存删除失败不影响主业务
            }

            @Override
            public void handleCacheClearError(RuntimeException e, Cache cache) {
                log.error("Redis cache clear error, cacheName: {}", cache.getName(), e);
                // 不抛异常
            }
        };
    }

    // region----------------------  数据结构操作 Bean  ------------------------

    @Bean
    public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForHash();
    }

    @Bean
    public ValueOperations<String, Object> valueOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    @Bean
    public ListOperations<String, Object> listOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForList();
    }

    @Bean
    public SetOperations<String, Object> setOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForSet();
    }

    @Bean
    public ZSetOperations<String, Object> zSetOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForZSet();
    }

    // endregion
}
