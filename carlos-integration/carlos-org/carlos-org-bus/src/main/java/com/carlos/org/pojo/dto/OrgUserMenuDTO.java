package com.carlos.org.pojo.dto;


import com.carlos.org.pojo.emuns.UserMenuCollectionTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户菜单收藏表 数据传输对象，service和manager向外传输对象
 * </p>
 *
 * @author  yunjin
 * @date    2024-2-28 11:10:01
 */
@Data
@Accessors(chain = true)
public class OrgUserMenuDTO {
        /** 主键 */
        private String id;
        /** 用户id */
        private String userId;
        /** 菜单id */
        private String menuId;
        /** 收藏状态*/
        private UserMenuCollectionTypeEnum status;
}
