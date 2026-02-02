package com.carlos.fx.encrypt.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 表基本信息
 * </p>
 *
 * @author Carlos
 * @date 2021/6/27 23:27
 */
@Accessors(chain = true)
@Data
public class TableInfo {

    /**
     * 表名
     */
    private String name;
    /**
     * 表备注
     */
    private String comment;

}
