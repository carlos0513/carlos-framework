package com.yunjin.org.pojo.ao;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 标签API提供的对象(API Object)
 * </p>
 *
 * @author yunjin
 * @date 2024-3-23 12:31:52
 */
@Data
@Accessors(chain = true)
public class LabelBaseAO implements Serializable {
        /**
         * 主键ID
         */
        private String id;
        /**
         * 标签名称
         */
        private String name;
        /**
         * 标签描述
         */
        private String description;
}
