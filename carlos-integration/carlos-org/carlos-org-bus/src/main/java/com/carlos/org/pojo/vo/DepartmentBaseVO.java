package com.carlos.org.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 部门 显示层对象，向页面传输的对象
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 18:19:17
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class DepartmentBaseVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(description = "主键")
    private Long id;
    @Schema(description = "部门名称")
    private String deptName;
    @Schema(description = "部门编号")
    private String deptCode;
    @Schema(description = "详细地址")
    private String address;
    @Schema(description = "部门排序")
    private int sort;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
