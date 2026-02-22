package com.carlos.org.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统用户 显示层对象，向页面传输的对象
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 18:19:17
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class UserListVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(description = "主键")
    private String id;
    @Schema(description = "用户名")
    private String account;
    @Schema(description = "真实姓名")
    private String realname;
    @Schema(description = "手机号码")
    private String phone;
    @Schema(description = "部门名称")
    private String departmentName;
    @Schema(description = "部门id")
    private String departmentId;
    @Schema(description = "部门层级")
    private String departmentLevelCode;
    @Schema(description = "创建者")
    private String createBy;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "用户排序")
    private int sort;
    @Schema(description = "角色名称")
    private String roleName;
}
