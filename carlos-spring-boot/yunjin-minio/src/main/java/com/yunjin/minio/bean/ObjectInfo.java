package com.yunjin.minio.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * <p>
 * 对象信息
 * </p>
 *
 * @author yunjin
 * @date 2021/11/10 15:12
 */
@Data
@AllArgsConstructor
public class ObjectInfo {

    /**
     * 桶名
     */
    private String bucketName;
    /**
     * 原文件名
     */
    private String originalName;

    /**
     * 对象名
     */
    private String objectName;


}
