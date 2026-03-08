package com.carlos.mq.utils;

import cn.hutool.json.JSONUtil;
import com.carlos.core.exception.ServiceException;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Set;

/**
 * <p>
 * 消息发送工具
 * </p>
 *
 * @author Carlos
 * @date 2022/5/11 18:17
 */
@Slf4j
public class MessageSender {

    private final static Cache<Object, KafkaTemplate<Object, Object>> KAFKA = CacheBuilder.newBuilder()
        // 设置并发级别为cpu核心数，默认为4
        .concurrencyLevel(Runtime.getRuntime().availableProcessors())
        .build();

    /**
     * 使用kafka发送消息
     *
     * @param server  消息服务器信息
     * @param topic   主题
     * @param content 消息内容
     * @author Carlos
     * @date 2022/5/11 18:17
     */
    public static void sendWithKafka(Set<String> server, String topic, Object content) {
        KafkaTemplate<Object, Object> kafkaTemplate = getKafkaTemplate(server);
        try {
            kafkaTemplate.send(topic, topic, JSONUtil.toJsonStr(content));
            if (log.isDebugEnabled()) {
                log.debug("消息发送成功: server:{}  topic:{}", server, topic);
            }
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }


    /**
     * 获取Kafka client
     *
     * @param servers 参数0
     * @return org.springframework.kafka.core.KafkaTemplate<java.lang.Object, java.lang.Object>
     * @author Carlos
     * @date 2022/5/11 18:18
     */
    private static KafkaTemplate<Object, Object> getKafkaTemplate(Set<String> servers) {
        int code = servers.hashCode();
        KafkaTemplate<Object, Object> kafkaTemplate = KAFKA.getIfPresent(code);
        if (kafkaTemplate != null) {
            return kafkaTemplate;
        }
        KafkaProperties properties = new KafkaProperties();
        KafkaProperties.Producer producer = properties.getProducer();
        producer.setKeySerializer(StringSerializer.class);
        producer.setValueSerializer(StringSerializer.class);
        properties.setBootstrapServers(Lists.newArrayList(servers));

//        HashMap<String, Object> propsMap = new HashMap<>();
//        propsMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Lists.newArrayList(servers));
//        propsMap.put(ProducerConfig.RETRIES_CONFIG, 0);
//        propsMap.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 6000);
//        propsMap.put(ProducerConfig.BATCH_SIZE_CONFIG, 4066);
//        propsMap.put(ProducerConfig.LINGER_MS_CONFIG, 1);
//        propsMap.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "none");
//        propsMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        propsMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        DefaultKafkaProducerFactory<Object, Object> factory = new DefaultKafkaProducerFactory<>(properties.buildProducerProperties());
        kafkaTemplate = new KafkaTemplate<>(factory);
        KAFKA.put(code, kafkaTemplate);
        return kafkaTemplate;
    }


}
