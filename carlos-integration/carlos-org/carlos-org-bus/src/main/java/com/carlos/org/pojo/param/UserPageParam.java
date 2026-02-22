package com.carlos.org.pojo.param;


import com.carlos.core.param.ParamPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Set;


/**
 * <p>
 * 系统用户 列表查询参数封装
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 18:19:17
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "系统用户列表查询参数")
public class UserPageParam extends ParamPage {

    @Schema(description = "搜索关键字")
    private String keyword;

    private String deptCode;

    @Schema(description = "部门层级")
    private Set<String> deptLevels;
}
