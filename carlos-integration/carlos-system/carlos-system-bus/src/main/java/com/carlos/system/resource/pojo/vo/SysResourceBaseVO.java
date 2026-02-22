package com.carlos.system.resource.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 系统资源基础信息
 * </p>
 *
 * @author carlos
 * @date 2021-12-28 15:26:57
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysResourceBaseVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(description = "主键")
    private String id;

    @Schema(description = "资源名称")
    private String name;

    @Schema(description = "接口路径")
    private String path;


}
