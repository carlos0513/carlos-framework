package com.yunjin.org.pojo.param;

import com.yunjin.core.param.ParamPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(value = "文件分页查询", description = "文件分页查询参数")
public class HelpFilePageParam extends ParamPage {
    @Schema(value = "文件名称")
    private String fileName;
}
