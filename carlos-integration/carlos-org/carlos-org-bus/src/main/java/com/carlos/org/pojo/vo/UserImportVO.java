package com.carlos.org.pojo.vo;

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
    @Schema(description = "主键")
    private Serializable id;
    @Schema(description = "用户名")
    private String account;
    @Schema(description = "真实姓名")
    private String realname;
    @Schema(description = "证件号码")
    private String identify;
    @Schema(description = "手机号码")
    private String phone;
    @Schema(description = "角色名称")
    private String role;
    @Schema(description = "部门完整信息，以”-“分割部门级别")
    private String department;
    @Schema(description = "行政区域编码")
    private String regionCode;
    @Schema(description = "性别，0：保密, 1：男，2：女，默认0")
    private String gender;
    @Schema(description = "邮箱")
    private String email;

}
