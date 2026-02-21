package com.yunjin.org.login.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yunjin.json.jackson.annotation.EnumField;
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
public class AppRoleResourceGroupItemVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Schema(value = "主键")
    private String id;
    @EnumField
    @Schema(value = "资源类型(可扩展):0按钮, 1指标 ")
    private ResourceTypeEnum resourceType;
    @Schema(value = "资源key")
    private String resourceKey;


}
