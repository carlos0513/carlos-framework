package com.carlos.system.dict.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.carlos.json.jackson.annotation.EnumField;
import com.carlos.system.dict.pojo.enums.DictTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 系统字典 显示层对象，向页面传输的对象
 * </p>
 *
 * @author yunjin
 * @date 2021-11-22 14:49:00
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysDictVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(value = "主键")
    private String id;

    @Schema(value = "字典名称")
    private String dictName;

    @Schema(value = "字典编码")
    private String dictCode;

    @Schema(value = "描述")
    private String description;

    @EnumField(type = EnumField.SerializerType.FULL)
    @Schema(value = "字典类型 数字类型 字符类型")
    private DictTypeEnum type;

    @Schema(value = "创建时间")
    private LocalDateTime createTime;

    @Schema(value = "更新时间")
    private LocalDateTime updateTime;

    @Schema(value = "字典选项")
    private List<SysDictItemVO> items;

}
