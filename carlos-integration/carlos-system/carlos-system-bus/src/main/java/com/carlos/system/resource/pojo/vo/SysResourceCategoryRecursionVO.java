package com.carlos.system.resource.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 资源分类 显示层对象，向页面传输的对象
 * </p>
 *
 * @author carlos
 * @date 2022-1-5 17:23:27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysResourceCategoryRecursionVO extends SysResourceCategoryVO {

    @Schema(description = "子类型")
    List<SysResourceCategoryRecursionVO> children;

}
