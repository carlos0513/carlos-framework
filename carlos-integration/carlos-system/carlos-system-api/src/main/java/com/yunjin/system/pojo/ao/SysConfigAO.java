package com.carlos.system.pojo.ao;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 系统配置 显示层对象，向页面传输的对象
 * </p>
 *
 * @author yunjin
 * @date 2022-11-3 13:47:55
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysConfigAO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(value = "参数Id")
    private String id;
    @Schema(value = "参数名称")
    private String configName;
    @Schema(value = "参数编码")
    private String configCode;
    @Schema(value = "参数键值")
    private String configValue;
    @Schema(value = "值类型")
    private String valueType;
}
