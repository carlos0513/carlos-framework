package com.carlos.org.pojo.vo;

import com.carlos.org.pojo.param.UserDeptRoleDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统用户 显示层对象，向页面传输的对象
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 18:19:17
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(description = "主键")
    private Long id;
    @Schema(description = "真实姓名")
    private String realname;
    @Schema(description = "手机号码")
    private String phone;
    @Schema(description = "用户账号")
    private String account;
    @Schema(description = "身份证号")
    private String identify;
    @Schema(description = "详细地址")
    private String address;
    @Schema(description = "行政区域")
    private String regionCode;
    @Schema(description = "角色id列表")
    private Set<String> roleIds;
    @Schema(description = "部门id列表")
    private Set<String> departmentIds;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "排序")
    private int sort;
    @Schema(description = "用户部门角色信息")
    List<UserDeptRoleDTO> deptRoles;

}
