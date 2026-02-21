package com.yunjin.org.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yunjin.org.pojo.enums.OrgDockingTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 用户信息对接关联表 显示层对象，向页面传输的对象
 * </p>
 *
 * @author Carlos
 * @date 2025-2-27 15:41:32
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgDockingMappingVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Schema(value = "主键ID")
    private String id;
    @Schema(value = "系统数据id")
    private String systemId;
    @Schema(value = "目标系统id")
    private String targetId;
    @Schema(value = "目标系统标识")
    private String targetCode;
    @Schema(value = "对接类型")
    private OrgDockingTypeEnum dockingType;


}
