package com.carlos.org.pojo.vo;

import com.carlos.org.pojo.emuns.UserMenuCollectionTypeEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
* <p>
    * 用户菜单收藏表 显示层对象，向页面传输的对象
    * </p>
*
* @author  yunjin
* @date    2024-2-28 11:10:01
*/
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgUserMenuVO implements Serializable {
private static final long serialVersionUID = 1L;
    @Schema(description = "主键")
        private String id;
    @Schema(description = "用户id")
        private String userId;
    @Schema(description = "菜单id")
        private String menuId;
    @Schema(description = "状态")
    private UserMenuCollectionTypeEnum status;

}
