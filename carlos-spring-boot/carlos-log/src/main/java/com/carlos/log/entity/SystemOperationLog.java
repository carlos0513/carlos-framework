package com.carlos.log.entity;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SystemOperationLog {

    /**
     * 主键ID
     */
    private String id;
    /**
     * 操作人id
     */
    private String operatorId;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 备注
     */
    private String remark;
    /**
     * 级别
     */
    private Long layer;
    /**
     * 信息
     */
    private String message;
    /**
     * 地址
     */
    private String url;
    /**
     * 错误
     */
    private String exception;
    /**
     * IP
     */
    private String ip;
    /**
     * 浏览器
     */
    private String browser;
    /**
     * 操作账户
     */
    private String operatorAccount;
    /**
     * 操作人
     */
    private String operator;
}
