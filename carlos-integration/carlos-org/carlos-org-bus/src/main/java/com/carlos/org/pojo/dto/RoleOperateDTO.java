package com.carlos.org.pojo.dto;


import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色菜单操作表 数据传输对象，service和manager向外传输对象
 * </p>
 *
 * @author yunjin
 * @date 2023-7-7 14:19:55
 */
@Data
@Accessors(chain = true)
public class RoleOperateDTO {
        /**
         * 主键
         */
        private String id;
        /**
         * 角色id
         */
        private String roleId;
        /**
         * 菜单操作id
         */
        private String operateId;
}
