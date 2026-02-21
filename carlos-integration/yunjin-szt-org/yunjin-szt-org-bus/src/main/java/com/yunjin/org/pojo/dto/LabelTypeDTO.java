package com.yunjin.org.pojo.dto;


import com.yunjin.org.enums.LabelTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 标签分类 数据传输对象，service和manager向外传输对象
 * </p>
 *
 * @author  yunjin
 * @date    2024-3-22 15:07:09
 */
@Data
@Accessors(chain = true)
public class LabelTypeDTO {
        /**
         * 主键ID
         */
        private String id;
        /**
         * 类型名称
         */
        private String name;
        /**
         * 父级id
         */
        private String parentId;
        /**
         * 标签类别
         */
        private LabelTypeEnum labelType;
        /**
         * 排序
         */
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
        /** 更新时间 */
        private LocalDateTime updateTime;
        /** 子集*/
        private List<LabelTypeDTO> subTypes;
}
