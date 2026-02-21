package com.yunjin.org.pojo.param;

import com.yunjin.org.pojo.emuns.HelpFileEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
@Schema(value = "文档修改参数", description = "文档修改参数")
public class HelpFileUpdateParam {
    @NotBlank(message = "主键ID不能为空")
    @Schema(value = "主键ID")
    private String id;
    @Schema(value = "文件名称")
    private String fileName;
    @Schema(value = "文件类型")
    private HelpFileEnum fileType;
    @Schema(value = "文件描述")
    private String fileContent;
    @Schema(value = "排序")
    private String sort;
    @Schema(value = "文件样例")
    private String fileSample;
}
