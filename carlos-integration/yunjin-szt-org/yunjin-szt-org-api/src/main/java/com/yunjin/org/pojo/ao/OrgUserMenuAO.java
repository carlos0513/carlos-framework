package com.yunjin.org.pojo.ao;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
    import java.lang.Boolean;
    import java.lang.String;

/**
* <p>
    * 用户菜单收藏表 API提供的对象(API Object)
    * </p>
*
* @author  yunjin
* @date    2024-2-28 11:10:01
*/
@Data
@Accessors(chain = true)
public class OrgUserMenuAO implements Serializable{
        /** 主键 */
        private String id;
        /** 用户id */
        private String userId;
        /** 菜单id */
        private String menuId;
}
