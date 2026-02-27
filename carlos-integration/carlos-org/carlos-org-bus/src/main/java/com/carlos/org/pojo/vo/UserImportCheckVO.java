package com.carlos.org.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 字段组件配置对象，向页面传输的对象
 * </p>
 *
 * @author carlos
 * @date 2022-12-07 10:25:02
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class UserImportCheckVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "校验结果")
    private Boolean checkResult;

    @Schema(description = "校验结果文档")
    private FileInfo fileInfo;

    @Data
    public static class FileInfo implements Serializable {
        private static final long serialVersionUID = 1L;
        @Schema(description = "文件id")
        private Long id;
        @Schema(description = "文件组id")
        private Long groupId;
        @Schema(description = "文件名")
        private String name;
        @Schema(description = "文件地址")
        private String url;
    }
}
