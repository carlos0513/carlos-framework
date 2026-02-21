package com.yunjin.org.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统角色 显示层对象，向页面传输的对象
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 18:19:17
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class RoleVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(value = "主键")
    private String id;
    @Schema(value = "角色名称")
    private String name;
    @Schema(value = "角色唯一编码")
    private String code;
    @Schema(value = "角色状态， 禁用， 启用")
    private String state;
    @Schema(value = "备注")
    private String description;
    @Schema(value = "创建时间")
    private LocalDateTime createTime;
    @Schema(value = "修改时间")
    private LocalDateTime updateTime;
    @Schema(value = "创建者 ")
    private String createBy;
    @Schema(value = "修改者 ")
    private String updateBy;
}
