package com.carlos.org.pojo.param;


import com.carlos.core.param.ParamPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * <p>
 * 系统角色 列表查询参数封装
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 18:19:17
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "系统角色列表查询参数")
public class RolePageParam extends ParamPage {

    @Schema(description = "关键字")
    private String keyword;


    /*@Schema(description = "角色状态， 禁用， 启用")
    private String state;*/

}
