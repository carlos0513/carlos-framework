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
@Schema(description = "系统配置修改参数")
public class SysConfigUpdateParam {

    @NotNull(message = "参数Id不能为空")
    @Schema(description = "参数Id")
    private String id;
    @Schema(description = "参数名称")
    private String configName;
    @Schema(description = "参数键值")
    private String configValue;
    @Schema(description = "值类型")
    private String valueType;
    @Schema(description = "状态")
    private Boolean state;
    @Schema(description = "备注")
    private String remark;
}
