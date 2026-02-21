package com.yunjin.org.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuIdVO {

    @Schema(value = "pc菜单id")
    private Set<String> pcMenuIds;

    @Schema(value = "移动端菜单id")
    private Set<String> mobileMenuIds;

    @Schema(value = "管理端菜单id")
    private Set<String> manageMenuIds;
}
