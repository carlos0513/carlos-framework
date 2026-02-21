package com.yunjin.org.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yunjin.org.pojo.emuns.HelpFileEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HelpFileVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Schema(value = "主键ID")
    private String id;
    @Schema(value = "文件描述")
    private String fileContent;
    @Schema(value = "文件名称")
    private String fileName;
    @Schema(value = "排序")
    private String sort;
    @Schema(value = "文件类型")
    private HelpFileEnum fileType;
    @Schema(value = "创建人")
    private String createBy;
    @Schema(value = "文件样例")
    private String fileSample;
    @Schema(value = "创建时间")
    private LocalDateTime createTime;
    @Schema(value = "更新人")
    private String updateBy;
    @Schema(value = "更新时间")
    private LocalDateTime updateTime;
}
