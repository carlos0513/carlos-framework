package com.yunjin.org.pojo.param;

import com.yunjin.org.pojo.emuns.HelpFileEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
@Schema(value = "帮助中心文件新增", description = "帮助中心文件新增参数")
public class HelpFileCreateParam {
    @Schema(value = "文件描述")
    private String fileContent;
    @Schema(value = "文件名称")
    private String fileName;
    @NotNull(message = "排序不能为空")
    @Schema(value = "排序")
    private String sort;
    @Schema(value = "文件类型")
    private HelpFileEnum fileType;
    @Schema(value = "文件样例")
    private String fileSample;
}
