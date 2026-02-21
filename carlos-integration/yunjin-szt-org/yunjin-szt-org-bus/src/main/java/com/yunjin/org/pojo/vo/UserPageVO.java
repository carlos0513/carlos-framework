package com.yunjin.org.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yunjin.json.jackson.annotation.EnumField;
import com.yunjin.json.jackson.annotation.EnumField.SerializerType;
import com.yunjin.org.pojo.enums.UserStateEnum;
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
public class UserPageVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(value = "主键")
    private String id;
    @Schema(value = "用户名")
    private String account;
    @Schema(value = "真实姓名")
    private String realname;
    @Schema(value = "联系人")
    private String phone;
    @Schema(value = "身份证号")
    private String identify;
    @Schema(value = "部门")
    private String department;
    @Schema(value = "角色")
    private String role;
    @Schema(value = "区域")
    private String region;
    @EnumField(type = SerializerType.FULL)
    @Schema(value = "状态，0：禁用，1：启用，2：锁定")
    private UserStateEnum state;
    @Schema(value = "创建时间")
    private LocalDateTime createTime;
    @Schema(value = "排序")
    private int sort;
}
