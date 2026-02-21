package com.yunjin.org.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 角色资源组关联表 显示层对象，向页面传输的对象
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleResourceGroupRefVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Schema(value = "主键ID")
    private String id;
    @Schema(value = "角色id")
    private String roleId;
    @Schema(value = "资源组id")
    private String resourceGroupId;
    @Schema(value = "创建者编号")
    private String createBy;
    @Schema(value = "创建时间")
    private LocalDateTime createTime;
    @Schema(value = "更新者编号")
    private String updateBy;
    @Schema(value = "更新时间")
    private LocalDateTime updateTime;

}
