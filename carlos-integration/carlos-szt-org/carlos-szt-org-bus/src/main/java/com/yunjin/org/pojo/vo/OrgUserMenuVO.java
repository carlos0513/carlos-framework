package com.yunjin.org.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yunjin.org.pojo.emuns.UserMenuCollectionTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
    import java.lang.Boolean;
    import java.lang.String;

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
        @Schema(value = "主键")
        private String id;
        @Schema(value = "用户id")
        private String userId;
        @Schema(value = "菜单id")
        private String menuId;
    @Schema(value = "状态")
    private UserMenuCollectionTypeEnum status;

}
