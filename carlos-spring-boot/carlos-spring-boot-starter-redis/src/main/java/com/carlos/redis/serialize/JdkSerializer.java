package com.carlos.redis.serialize;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * JDK 原生序列化实现
 * <p>
 * 特点：
 * - 兼容性最好，无需额外依赖
 * - 性能一般，空间占用大
 * - 二进制格式，Redis 中不可读
 * - 适合对兼容性要求高的场景
 * </p>
 *
 * @author carlos
 * @date 2026-03-14
 */
@Slf4j
public class JdkSerializer implements RedisSerializerStrategy {

    @Override
    public byte[] serialize(Object obj) {
        if (obj == null) {
            return new byte[0];
        }
        if (!(obj instanceof Serializable)) {
            throw new SerializationException("Object must implement Serializable: " + obj.getClass(), null);
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            oos.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            log.error("JDK serialize error", e);
            throw new SerializationException("JDK serialize error: " + e.getMessage(), e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            Object obj = ois.readObject();
            if (obj == null) {
                return null;
            }
            // 类型检查
            if (!clazz.isInstance(obj)) {
                throw new ClassCastException("Cannot cast " + obj.getClass() + " to " + clazz);
            }
            return (T) obj;
        } catch (Exception e) {
            log.error("JDK deserialize error", e);
            throw new SerializationException("JDK deserialize error: " + e.getMessage(), e);
        }
    }

    @Override
    public String getType() {
        return "jdk";
    }

    @Override
    public boolean isBinary() {
        return true;
    }

    /**
     * 序列化异常
     */
    public static class SerializationException extends RuntimeException {
        public SerializationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
