package com.carlos.org.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 角色菜单操作表 显示层对象，向页面传输的对象
 * </p>
 *
 * @author carlos
 * @date 2023-7-7 14:19:55
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleOperateVO implements Serializable {
        private static final long serialVersionUID = 1L;
    @Schema(description = "主键")
        private String id;
    @Schema(description = "角色id")
        private String roleId;
    @Schema(description = "菜单操作id")
        private String operateId;

}
