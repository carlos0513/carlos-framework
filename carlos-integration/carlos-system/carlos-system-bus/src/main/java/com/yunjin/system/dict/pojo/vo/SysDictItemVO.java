package com.carlos.system.dict.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统字典详情 显示层对象，向页面传输的对象
 * </p>
 *
 * @author yunjin
 * @date 2021-11-22 14:49:00
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysDictItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(value = "主键")
    private String id;

    @Schema(value = "字典id")
    private String dictId;

    @Schema(value = "字典项值")
    private String itemName;

    @Schema(value = "字典项key")
    private String itemCode;

    @Schema(value = "描述")
    private String description;

    @Schema(value = "排序")
    private Integer sort;

    @Schema(value = "状态（1启用 0不启用）")
    private Boolean enable;

    @Schema(value = "创建时间")
    private LocalDateTime createTime;


    @Schema(value = "更新时间")
    private LocalDateTime updateTime;

}
