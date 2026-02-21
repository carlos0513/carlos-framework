package com.carlos.system.configration.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统配置 更新参数封装
 * </p>
 *
 * @author yunjin
 * @date 2022-11-3 13:47:55
 */
@Data
@Accessors(chain = true)
@Schema(value = "系统配置修改参数", description = "系统配置修改参数")
public class SysConfigUpdateParam {

    @NotNull(message = "参数Id不能为空")
    @Schema(value = "参数Id")
    private String id;
    @Schema(value = "参数名称")
    private String configName;
    @Schema(value = "参数键值")
    private String configValue;
    @Schema(value = "值类型")
    private String valueType;
    @Schema(value = "状态")
    private Boolean state;
    @Schema(value = "备注")
    private String remark;
}
