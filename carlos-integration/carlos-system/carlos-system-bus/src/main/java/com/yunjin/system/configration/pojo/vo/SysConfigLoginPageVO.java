package com.carlos.system.configration.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(value = "背景图片")
    private String backgroundImg;
    @Schema(value = "logo")
    private String logo;
    @Schema(value = "导航标题")
    private String title;
    @Schema(value = "主标题")
    private String mainTitle;
    @Schema(value = "副标题")
    private String subTitle;
    @Schema(value = "版本号")
    private String version;
    @Schema(value = "供应商")
    private String supplier;
    @Schema(value = "登录标题")
    private String loginTitle;
}
