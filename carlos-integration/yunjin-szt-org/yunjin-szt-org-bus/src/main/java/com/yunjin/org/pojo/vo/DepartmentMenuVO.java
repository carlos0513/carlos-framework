package com.yunjin.org.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 部门菜单表 显示层对象，向页面传输的对象
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 19:21:46
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class DepartmentMenuVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(value = "主键")
    private String id;
    @Schema(value = "部门id")
    private String departmentId;
    @Schema(value = "菜单id")
    private String menuId;
    @Schema(value = "创建者")
    private String createBy;
    @Schema(value = "创建时间")
    private LocalDateTime createTime;
    @Schema(value = "修改时间")
    private LocalDateTime updateTime;
    @Schema(value = "租户id")
    private String tenantId;

}
