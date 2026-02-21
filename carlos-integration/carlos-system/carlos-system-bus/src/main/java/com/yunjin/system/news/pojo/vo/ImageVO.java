package com.carlos.system.news.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 图片信息显示层对象
 * </p>
 *
 * @author yunjin
 * @date 2022-11-28 10:48:53
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageVO {

    /**
     * 文件Id
     */
    private String id;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件地址
     */
    private String url;

    /**
     * 文件地址
     */
    private float size;
}
