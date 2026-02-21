package com.carlos.oauth.app.pojo.param;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 应用信息 更新参数封装
 * </p>
 *
 * @author Carlos
 * @date 2025-3-12 14:00:14
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "应用秘钥重置参数", description = "应用秘钥重置参数")
public class AppClientResetSecretParam {
    @NotNull(message = "主键不能为空")
    @ApiModelProperty(value = "主键")
    private Long id;
}
