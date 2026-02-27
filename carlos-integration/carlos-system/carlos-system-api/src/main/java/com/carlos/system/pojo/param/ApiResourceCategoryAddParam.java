package com.carlos.system.pojo.param;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 资源分类 数据传输对象，service和manager向外传输对象
 *
 * @author carlos
 * @date 2022-1-5 17:23:27
 */
@Data
@Accessors(chain = true)
public class ApiResourceCategoryAddParam {

    /**
     * 主键
     */
    private Long id;
    /**
     * 父级ID
     */
    private Long parentId;
    /**
     * 类型名称
     */
    private String name;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
    /**
     * 下级
     */
    List<ApiResourceCategoryAddParam> children;
}
