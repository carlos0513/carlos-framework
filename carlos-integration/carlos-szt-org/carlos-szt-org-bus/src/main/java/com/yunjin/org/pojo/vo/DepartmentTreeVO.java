package com.yunjin.org.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

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
public class DepartmentTreeVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(value = "主键")
    private String id;
    @Schema(value = "上级id")
    private String parentId;
    @Schema(value = "部门名称")
    private String deptName;
    @Schema(value = "部门编号")
    private String deptCode;
    @Schema(value = "部门排序")
    private int sort;
    @Schema(value = "创建时间")
    private LocalDateTime createTime;
    @Schema(value = "部门层级")
    private String departmentLevelCode;
    @Schema(value = "部门区域")
    private String regionCode;
    @Schema(value = "组织机构类型")
    private String departmentType;
    @Schema(value = "子部门信息")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<DepartmentTreeVO> children;

    @Schema(value = "用户信息")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<User> users;

    @Schema(value = "目标系统编码")
    private String targetCode;

    @Schema(value = "第三方部门id")
    private String thirdDeptId;

    @Schema(value = "部门管理员id")
    private String operatorId;

    @Schema(value = "子级数目")
    private Integer childNum;


    @Data
    public static class User {
        @Schema(value = "主键")
        private String id;
        @Schema(value = "用户id")
        private String userId;
        @Schema(value = "用户名")
        private String account;
        @Schema(value = "姓名")
        private String name;
        @Schema(value = "真实姓名")
        private String realname;
        @Schema(value = "用户排序")
        private int sort;
        @Schema(value = "创建时间")
        private LocalDateTime createTime;
        @Schema(value = "部门层级")
        private String departmentLevelCode;
    }
}
