package com.carlos.org.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 部门基础信息 显示层对象，向页面传输的对象
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 18:19:17
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class DepartmentBaseInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(description = "主键")
    private String id;
    @Schema(description = "上级Id")
    private String parentId;
    @Schema(description = "上级名称")
    private String parentDeptName;
    @Schema(description = "部门名称")
    private String deptName;
    @Schema(description = "部门编号")
    private String deptCode;
    @Schema(description = "详细地址")
    private String address;
}
