package com.yunjin.org.pojo.ao;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
    import java.time.LocalDateTime;
    import java.lang.Boolean;
    import java.lang.String;
    import java.lang.Integer;

/**
* <p>
    * 标签分类 API提供的对象(API Object)
    * </p>
*
* @author  yunjin
* @date    2024-3-22 15:07:09
*/
@Data
@Accessors(chain = true)
public class LabelTypeAO implements Serializable{
        /** 主键ID */
        private String id;
        /** 类型名称 */
        private String name;
        /** 父级id */
        private String parentId;
        /** 排序 */
        private Integer sort;
        /** 是否隐藏 */
        private Boolean hidden;
        /** 创建者编号 */
        private String createBy;
        /** 创建时间 */
        private LocalDateTime createTime;
        /** 更新者编号 */
        private String updateBy;
        /** 更新时间 */
        private LocalDateTime updateTime;
}
