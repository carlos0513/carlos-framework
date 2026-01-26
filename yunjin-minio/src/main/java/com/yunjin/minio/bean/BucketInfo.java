package com.yunjin.minio.bean;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * <p>
 * 桶信息
 * </p>
 *
 * @author yunjin
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
