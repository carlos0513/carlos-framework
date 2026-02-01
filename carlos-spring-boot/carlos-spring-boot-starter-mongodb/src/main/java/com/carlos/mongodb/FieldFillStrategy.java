package com.carlos.mongodb;

/**
 * 字段填充策略枚举类
 *
 * @author Carlos
 * @date 2021/12/24 14:01
 */
public enum FieldFillStrategy {
    /**
     * 默认不处理
     */
    DEFAULT,
    /**
     * 插入时填充字段
     */
    INSERT,
    /**
     * 更新时填充字段
     */
    UPDATE,
    /**
     * 插入和更新时填充字段
     */
    INSERT_UPDATE
}
