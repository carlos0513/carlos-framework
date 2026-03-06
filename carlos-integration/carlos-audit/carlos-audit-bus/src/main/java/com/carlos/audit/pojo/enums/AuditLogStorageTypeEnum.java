package com.carlos.audit.pojo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.carlos.core.enums.AppEnum;
import com.carlos.core.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 审计日志存储类型枚举
 * </p>
 *
 * @author Carlos
 * @date 2026-03-06
 */
@AppEnum(code = "AuditLogStorageType")
@Getter
@AllArgsConstructor
public enum AuditLogStorageTypeEnum implements BaseEnum {

    /**
     * 阿里云OSS
     */
    OSS(1, "阿里云OSS"),

    /**
     * AWS S3
     */
    S3(2, "AWS S3"),

    /**
     * MinIO
     */
    MINIO(3, "MinIO"),

    /**
     * GridFS
     */
    GRIDFS(4, "GridFS"),

    /**
     * 本地存储
     */
    LOCAL(5, "本地存储"),

    /**
     * 数据库
     */
    DB(6, "数据库");

    @EnumValue
    private final Integer code;

    private final String desc;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
