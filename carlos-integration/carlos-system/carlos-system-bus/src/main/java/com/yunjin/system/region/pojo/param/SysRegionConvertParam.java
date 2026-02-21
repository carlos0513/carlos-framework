package com.carlos.system.region.pojo.param;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * <p>
 * 文件上传参数
 * </p>
 *
 * @author Carlos
 * @date 2021-11-10 10:45:55
 */

@Data
public class SysRegionConvertParam {

    @NotNull(message = "上传文件不能为空")
    @ApiModelProperty(value = "文件")
    private MultipartFile file;

    @NotBlank(message = "行政区域编码字段key不能为空")
    @ApiModelProperty(value = "行政区域编码字段key")
    private String regionCode;
    @NotBlank(message = "行政区域名称字段key不能为空")
    @ApiModelProperty(value = "行政区域名称字段key")
    private String regionName;
    @NotBlank(message = "上级id字段key不能为空")
    @ApiModelProperty(value = "上级id字段")
    private String pid;
    @NotBlank(message = "id字段key不能为空")
    @ApiModelProperty(value = "id字段")
    private String id;
}
