package com.yunjin.org.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 用户导入 显示层对象，向页面传输的对象
 * </p>
 *
 * @author Carlos
 * @date 2023-5-27 12:52:09
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserImportVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(value = "主键")
    private Serializable id;
    @Schema(value = "用户名")
    private String account;
    @Schema(value = "真实姓名")
    private String realname;
    @Schema(value = "证件号码")
    private String identify;
    @Schema(value = "手机号码")
    private String phone;
    @Schema(value = "角色名称")
    private String role;
    @Schema(value = "部门完整信息，以”-“分割部门级别")
    private String department;
    @Schema(value = "行政区域编码")
    private String regionCode;
    @Schema(value = "性别，0：保密, 1：男，2：女，默认0")
    private String gender;
    @Schema(value = "邮箱")
    private String email;

}
