package com.carlos.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * <p>
 * 部门 分类树查询参数封装
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 18:19:17
 */
@Data
@Accessors(chain = true)
@Schema(description = "部门分类树查询参数")
public class DepartmentTreeParam {

    @Schema(description = "部门的父级id")
    private String deptParentId;
    @Schema(description = "部门名称")
    private String deptName;
}
