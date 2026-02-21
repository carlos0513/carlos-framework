package com.yunjin.org.pojo.param;


import com.yunjin.core.param.ParamPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * <p>
 * 系统用户 列表查询参数封装
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 18:19:17
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(value = "系统用户列表查询参数", description = "系统用户列表查询参数")
public class UserExcludeDeptPageParam extends ParamPage {

    @Schema(value = "搜索关键字")
    private String keyword;

    @Schema("部门id列表")
    private String id;
}
