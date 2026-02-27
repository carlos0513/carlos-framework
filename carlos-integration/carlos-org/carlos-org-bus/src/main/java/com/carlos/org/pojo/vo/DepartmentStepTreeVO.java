package com.carlos.org.pojo.vo;

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
    @Schema(description = "部门信息")
    private List<DeptInfo> deptList;
    @Schema(description = "用户信息")
    private List<User> userList;

    @Data
    public static class DeptInfo {
        @Schema(description = "主键")
        private Long id;
        @Schema(description = "上级id")
        private Long parentId;
        @Schema(description = "部门名称")
        private String deptName;
        @Schema(description = "部门编号")
        private String deptCode;
        @Schema(description = "子部门信息")
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        List<DepartmentStepTreeVO> children;
    }

    @Data
    public static class User {
        @Schema(description = "主键")
        private Long id;
        @Schema(description = "用户id")
        private Long userId;
        @Schema(description = "用户名")
        private String account;
        @Schema(description = "姓名")
        private String name;
        @Schema(description = "真实姓名")
        private String realname;
    }
}
