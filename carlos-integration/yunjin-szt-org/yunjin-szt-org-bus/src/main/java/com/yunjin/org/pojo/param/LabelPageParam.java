package com.yunjin.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.yunjin.core.param.ParamPage;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

    import java.time.LocalDateTime;
    import java.lang.Boolean;
    import java.lang.String;
    import java.lang.Integer;


/**
 * <p>
 * 标签 列表查询参数封装
 * </p>
 *
 * @author  yunjin
 * @date    2024-3-23 12:31:52
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(value = "标签列表查询参数", description = "标签列表查询参数")
public class LabelPageParam extends ParamPage {
    @Schema(value = "标签名称")
    private String name;
    @Schema(value = "唯一编码")
    private String code;
    @Schema(value = "类型ID")
    private String typeId;
    @Schema(value = "排序")
    private Integer sort;
    @Schema(value = "是否隐藏")
    private Boolean hidden;
    @Schema("开始时间")
    private LocalDateTime start;

    @Schema("结束时间")
    private LocalDateTime end;
}
