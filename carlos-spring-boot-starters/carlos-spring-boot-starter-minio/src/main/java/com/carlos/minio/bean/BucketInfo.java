package com.carlos.minio.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 桶信息
 * </p>
 *
 * @author carlos
 * @date 2021/11/10 15:12
 */
@Data
@AllArgsConstructor
public class BucketInfo {

    /**
     * 桶名
     */
    private String name;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
