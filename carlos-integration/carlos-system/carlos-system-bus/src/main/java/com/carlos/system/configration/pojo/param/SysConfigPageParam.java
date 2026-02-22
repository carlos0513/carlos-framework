package com.carlos.system.configration.pojo.param;


import com.carlos.core.param.ParamPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * <p>
 * 系统配置 列表查询参数封装
 * </p>
 *
 * @author yunjin
 * @date 2022-11-3 13:47:55
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "系统配置列表查询参数")
public class SysConfigPageParam extends ParamPage {

    @Schema(description = "参数名称")
    private String configName;

}
