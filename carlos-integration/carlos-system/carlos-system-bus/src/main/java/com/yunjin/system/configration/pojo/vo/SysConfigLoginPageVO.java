package com.carlos.system.configration.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 系统配置 显示层对象，向页面传输的对象
 * </p>
 *
 * @author yunjin
 * @date 2022-11-3 13:47:55
 */
@Data
@Accessors(chain = true)
public class SysConfigLoginPageVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "背景图片")
    private String backgroundImg;
    @ApiModelProperty(value = "logo")
    private String logo;
    @ApiModelProperty(value = "导航标题")
    private String title;
    @ApiModelProperty(value = "主标题")
    private String mainTitle;
    @ApiModelProperty(value = "副标题")
    private String subTitle;
    @ApiModelProperty(value = "版本号")
    private String version;
    @ApiModelProperty(value = "供应商")
    private String supplier;
    @ApiModelProperty(value = "登录标题")
    private String loginTitle;
}
