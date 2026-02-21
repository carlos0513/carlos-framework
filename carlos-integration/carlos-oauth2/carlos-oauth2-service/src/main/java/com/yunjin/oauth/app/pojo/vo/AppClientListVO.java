package com.carlos.oauth.app.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 应用信息 显示层对象，向页面传输的对象
 * </p>
 *
 * @author Carlos
 * @date 2025-3-12 14:00:14
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppClientListVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Schema(value = "主键")
    private Long id;
    @Schema(value = "应用编号")
    private String appKey;
    @Schema(value = "应用名称")
    private String appName;
    @Schema(value = "应用logo")
    private String appLogo;
    @Schema(value = "应用密钥")
    private String appSecret;
}
