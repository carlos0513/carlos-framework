package com.yunjin.org.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yunjin.json.jackson.annotation.DepartmentCodeField;
import com.yunjin.json.jackson.annotation.UserIdField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 投诉建议处理节点日志 显示层对象，向页面传输的对象
 * </p>
 *
 * @author yunjin
 * @date 2024-9-23 16:01:35
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgComplaintLogVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Schema(value = "主键")
    private String id;
    @Schema(value = "投诉id")
    private String complaintId;
    @Schema(value = "处理类型")
    private Integer handleType;
    @Schema(value = "处理备注")
    private String remark;
    @Schema(value = "当前处理部门code")
    @DepartmentCodeField(limit = 1)
    private String deptCode;
    @Schema(value = "创建者")
    @UserIdField(type = UserIdField.SerializerType.REALNAME)
    private String createBy;
    @Schema(value = "创建时间")
    private LocalDateTime createTime;

}
