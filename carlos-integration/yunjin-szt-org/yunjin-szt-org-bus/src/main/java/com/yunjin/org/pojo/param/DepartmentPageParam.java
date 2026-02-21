package com.yunjin.org.pojo.param;


import com.yunjin.core.param.ParamPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * <p>
 * 部门 列表查询参数封装
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 18:19:17
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(value = "部门列表查询参数", description = "部门列表查询参数")
public class DepartmentPageParam extends ParamPage {

    @Schema(value = "部门名称")
    private String deptName;
    @Schema(value = "部门编号")
    private String deptCode;
    @Schema(value = "部门id")
    private String deptId;
    @Schema(value = "搜索关键字")
    private String keyword;
}
