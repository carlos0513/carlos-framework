package com.carlos.org.pojo.vo;

import com.carlos.json.jackson.annotation.UserIdField;
import com.carlos.json.jackson.annotation.UserIdField.SerializerType;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * <p>
 * 系统角色 显示层对象，向页面传输的对象
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 18:19:17
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class RolePageVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(description = "主键")
    private String id;
    @Schema(description = "角色名称")
    private String name;
    @Schema(description = "角色唯一编码")
    private String code;
    //    @Schema(description = "归属机构")
//    private String departmentName;
    @Schema(description = "归属机构层级集合")
    private Set<String> departmentTypes;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "修改时间")
    private LocalDateTime updateTime;
    @UserIdField(type = SerializerType.REALNAME)
    @Schema(description = "创建者 ")
    private String createBy;
    @Schema(description = "修改者 ")
    private String updateBy;
}
