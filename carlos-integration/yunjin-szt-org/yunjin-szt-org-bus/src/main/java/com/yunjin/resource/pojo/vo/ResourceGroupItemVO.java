package com.yunjin.resource.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yunjin.org.enums.ResourceTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 资源组详情项 显示层对象，向页面传输的对象
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResourceGroupItemVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Schema(value = "主键")
    private String id;
    @Schema(value = "资源组id")
    private String groupId;
    @Schema(value = "资源类型(可扩展):0按钮, 1指标 ")
    private ResourceTypeEnum resourceType;
    @Schema(value = "资源key")
    private String resourceKey;


}
