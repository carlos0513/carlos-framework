package com.yunjin.resource.pojo.dto;


import com.yunjin.org.enums.ResourceTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 资源组详情项 数据传输对象，service和manager向外传输对象
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
@Data
@Accessors(chain = true)
public class ResourceGroupItemDTO {
    /**
     * 主键
     */
    private String id;
    /**
     * 资源组id
     */
    private String groupId;
    /**
     * 资源类型(可扩展):0按钮, 1指标
     */
    private ResourceTypeEnum resourceType;
    /**
     * 资源key
     */
    private String resourceKey;
    /**
     * 创建者编号
     */
    private String createBy;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新者编号
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
