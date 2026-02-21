package com.carlos.system.news.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 图片信息传输对象
 * </p>
 *
 * @author yunjin
 * @date 2022-11-14 23:48:53
 */
@Data
@Accessors(chain = true)
public class ImageDTO {

    /**
     * 文件Id
     */
    @ApiModelProperty(value = "文件id")
    private String id;

    /**
     * 文件名称
     */
    @ApiModelProperty(value = "文件名")
    private String name;

    /**
     * 文件地址
     */
    @ApiModelProperty(value = "文件url")
    private String url;

    /**
     * 文件地址
     */
    @ApiModelProperty(value = "文件大小")
    private float size;
}
