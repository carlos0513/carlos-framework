package com.yunjin.org.pojo.param;


import com.yunjin.core.param.ParamPage;
import com.yunjin.org.enums.LabelTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * <p>
 * 标签分类 列表查询参数封装
 * </p>
 *
 * @author yunjin
 * @date 2024-3-22 15:07:09
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(value = "标签分类列表查询参数", description = "标签分类列表查询参数")
public class LabelTypePageParam extends ParamPage {
    @Schema(value = "标签名称")
    private String labelName;
    @Schema(value = "标签分类")
    private LabelTypeEnum labelTypeEnum;
}
