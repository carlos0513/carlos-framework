package com.yunjin.resource.pojo.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 资源组 数据传输对象，service和manager向外传输对象
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
@Data
@Accessors(chain = true)
public class ResourceGroupDTO {
    /**
     * 主键
     */
    private String id;
    /**
     * 资源组code
     */
    private String groupCode;
    /**
     * 资源组名称
     */
    private String groupName;
    /**
     * 资源说明
     */
    private String description;
    /**
     * 是否启用 1启用 0禁用
     */
    private Boolean state;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
