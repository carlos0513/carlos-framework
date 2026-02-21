package com.yunjin.org.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author： lvbw
 * @date： 2026/02/02 10:59
 * @Description：
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class DepartmentStepTreeVO implements Serializable {
    @Schema(value = "部门信息")
    private List<DeptInfo> deptList;
    @Schema(value = "用户信息")
    private List<User> userList;

    @Data
    public static class DeptInfo {
        @Schema(value = "主键")
        private String id;
        @Schema(value = "上级id")
        private String parentId;
        @Schema(value = "部门名称")
        private String deptName;
        @Schema(value = "部门编号")
        private String deptCode;
        @Schema(value = "子部门信息")
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        List<DepartmentStepTreeVO> children;
    }

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
    }
}
