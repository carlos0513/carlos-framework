package com.carlos.mongodb.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * 日志文档实体类
 * MongoDB 集合: log_document
 *
 * @author Carlos
 * @date 2026/3/15
 */
@Data
@Document(collection = "log_document")
public class LogDocument {

    @Id
    private String id;

    /**
     * 日志级别
     */
    private String level;

    /**
     * 日志模块
     */
    private String module;

    /**
     * 操作内容
     */
    private String content;

    /**
     * 操作用户ID
     */
    private Long userId;

    /**
     * 操作用户名
     */
    private String username;

    /**
     * 请求IP
     */
    private String ip;

    /**
     * 请求路径
     */
    private String requestUrl;

    /**
     * 请求方法
     */
    private String requestMethod;

    /**
     * 执行时长(毫秒)
     */
    private Long executionTime;

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
