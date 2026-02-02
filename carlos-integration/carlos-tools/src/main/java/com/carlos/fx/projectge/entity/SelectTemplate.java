package com.carlos.fx.projectge.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 模板描述文件基本信息
 * </p>
 *
 * @author Carlos
 * @date 2021/11/22 18:15
 */
@Accessors(chain = true)
@Data
public class SelectTemplate implements Serializable {


    /**
     * 模板的名字
     */
    private String name;

    /**
     * 模板描述
     */
    private String describe;

    /**
     * 模板路径
     */
    private String path;

}
