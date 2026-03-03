package com.carlos.org.pojo.dto;


import com.carlos.org.pojo.enums.OrgPermissionStateEnum;
import com.carlos.org.pojo.enums.OrgPermissionTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 权限 数据传输对象
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Data
@Accessors(chain = true)
public class OrgPermissionDTO {

    /** 主键 */
    private Long id;

    /** 父权限ID */
    private Long parentId;

    /** 权限名称 */
    private String permName;

    /** 权限编码 */
    private String permCode;

    /** 权限类型 */
    private OrgPermissionTypeEnum permType;

    /** 资源路径 */
    private String resourceUrl;

    /** HTTP方法 */
    private String method;

    /** 菜单图标 */
    private String icon;

    /** 排序 */
    private Integer sort;

    /** 状态 */
    private OrgPermissionStateEnum state;

    /** 备注 */
    private String description;

    /** 版本 */
    private Integer version;

    /** 创建者 */
    private Long createBy;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 修改时间 */
    private LocalDateTime updateTime;

    /** 租户id */
    private Long tenantId;

    /** 子权限列表 */
    private List<OrgPermissionDTO> children;

}
