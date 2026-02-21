package com.yunjin.org.pojo.param;


import com.yunjin.core.param.ParamPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * <p>
 * 用户导入 列表查询参数封装
 * </p>
 *
 * @author Carlos
 * @date 2023-5-27 12:52:09
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(value = "用户导入列表查询参数", description = "用户导入列表查询参数")
public class UserImportPageParam extends ParamPage {

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
    @Schema("开始时间")
    private LocalDateTime start;

    @Schema("结束时间")
    private LocalDateTime end;
}
