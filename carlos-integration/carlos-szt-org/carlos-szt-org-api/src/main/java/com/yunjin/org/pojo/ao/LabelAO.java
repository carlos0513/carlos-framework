package com.yunjin.org.pojo.ao;


import com.yunjin.org.enums.LabelTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* <p>
    * 标签 API提供的对象(API Object)
    * </p>
*
* @author  yunjin
* @date    2024-3-23 12:31:52
*/
@Data
@Accessors(chain = true)
public class LabelAO implements Serializable{
        /** 主键ID */
        private String id;
        /** 标签名称 */
        private String name;
        /** 唯一编码 */
        private String code;
        /** 类型ID */
        private String typeId;
        /** 排序 */
        private Integer sort;
        /**
         * 是否隐藏
         */
        private Boolean hidden;
        /**
         * 创建者编号
         */
        private String createBy;
        /**
         * 创建时间
         */
        private LocalDateTime createTime;
        /**
         * 更新者编号
         */
        private String updateBy;
        /**
         * 更新时间
         */
        private LocalDateTime updateTime;
        /**
         * 标签描述
         */
        private String description;
        /**
         * 标签类别
         */
        private LabelTypeEnum labelType;
}
