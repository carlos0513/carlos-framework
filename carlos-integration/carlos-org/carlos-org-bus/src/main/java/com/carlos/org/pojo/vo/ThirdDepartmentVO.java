package com.carlos.org.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 部门 显示层对象，向页面传输的对象
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 18:19:17
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
@NoArgsConstructor
public class ThirdDepartmentVO implements Serializable {

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
    @Schema(description = "部门排序")
    private int sort;
    @Schema(description = "组织机构类型")
    private String departmentType;
    @Schema(description = "组织机构层级")
    private String departmentLevelCode;
    @Schema(description = "管辖区域")
    private String regionCode;
    @Schema(description = "说明")
    private String description;
}
