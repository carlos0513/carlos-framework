package com.yunjin.org.pojo.dto;


import com.yunjin.org.enums.LabelTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 标签 数据传输对象，service和manager向外传输对象
 * </p>
 *
 * @author  yunjin
 * @date    2024-3-23 12:31:52
 */
@Data
@Accessors(chain = true)
public class LabelDTO {
        /** 主键ID */
        private String id;
        /** 标签名称 */
        private String name;
        /** 唯一编码 */
        private String code;
        /** 类型ID */
        private String typeId;
        /** 类型名称 */
        private String typeName;
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
