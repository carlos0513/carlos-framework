package com.yunjin.org.pojo.ao;

import com.yunjin.org.enums.ResourceTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 资源组详情项 显示层对象，向页面传输的对象
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
@Data
@Accessors(chain = true)
public class RoleResourceGroupItemAO {
    /**
     * 主键
     */
    private String id;
    /**
     * 资源类型(可扩展):0按钮, 1指标
     */
    private ResourceTypeEnum resourceType;
    /**
     * 资源key
     */
    private String resourceKey;


}
