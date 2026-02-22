package com.carlos.system.upload.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 文件上传记录 显示层对象，向页面传输的对象
 * </p>
 *
 * @author Carlos
 * @date 2022-2-7 15:22:31
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UploadRecordVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(description = "主键")
    private Long id;
    @Schema(description = "文件分组")
    private Long groupId;
    @Schema(description = "文件库名称")
    private String repositoryName;
    @Schema(description = "文件库地址")
    private String repositoryUrl;
    @Schema(description = "源文件名")
    private String originalName;
    @Schema(description = "文件用途")
    private String uses;
    @Schema(description = "上传状态 成功 失败")
    private String state;
    @Schema(description = "上传人")
    private Long createBy;
    @Schema(description = "上传时间")
    private LocalDateTime createTime;

}
