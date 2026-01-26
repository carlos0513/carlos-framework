package com.yunjin.core.base;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 字典字段枚举
 * </p>
 *
 * @author yunjin
 * @date 2020/4/12 2:58
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Dict implements Serializable {

    /**
     * 字典id
     */
    private Serializable dictId;

    /**
     * 字典code
     */
    private String dictCode;

    /**
     * 字典名称
     */
    private String dictName;

    /**
     * 字典选项code
     */
    private String code;

    /**
     * 字典选项名称
     */
    private String name;

    /**
     * 字典选项id
     */
    private Serializable id;
}
