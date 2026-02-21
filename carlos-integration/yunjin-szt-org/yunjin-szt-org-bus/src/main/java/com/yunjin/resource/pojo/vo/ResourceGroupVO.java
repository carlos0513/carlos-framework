package com.yunjin.resource.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 资源组 显示层对象，向页面传输的对象
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResourceGroupVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Schema(value = "主键")
    private String id;
    @Schema(value = "资源组code")
    private String groupCode;
    @Schema(value = "资源组名称")
    private String groupName;
    @Schema(value = "资源说明")
    private String description;
    @Schema(value = "是否启用 1启用 0禁用")
    private Boolean state;
    @Schema(value = "创建时间")
    private LocalDateTime createTime;
    @Schema(value = "修改时间")
    private LocalDateTime updateTime;

}
