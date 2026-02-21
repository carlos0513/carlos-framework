package com.yunjin.resource.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统指标管理 显示层对象，向页面传输的对象
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MetricsManagementVO implements Serializable {
        private static final long serialVersionUID = 1L;
        @Schema(value = "主键")
        private String id;
        @Schema(value = "指标编码")
        private String metricsCode;
        @Schema(value = "指标名称")
        private String metricsName;
        @Schema(value = "指标类型(可扩展)：0 首页指标 ")
        private Integer metricsType;
        @Schema(value = "是否启用 1启用 0禁用")
        private Boolean state;
        @Schema(value = "指标描述")
        private String description;
        @Schema(value = "创建者编号")
        private String createBy;
        @Schema(value = "创建时间")
        private LocalDateTime createTime;
        @Schema(value = "更新者编号")
        private String updateBy;
        @Schema(value = "修改时间")
        private LocalDateTime updateTime;

}
