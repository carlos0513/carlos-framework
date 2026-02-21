package com.yunjin.org.pojo.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 投诉建议处理节点日志 数据传输对象，service和manager向外传输对象
 * </p>
 *
 * @author yunjin
 * @date 2024-9-23 16:01:35
 */
@Data
@Accessors(chain = true)
public class OrgComplaintLogDTO {
    /**
     * 主键
     */
    private String id;
    /**
     * 投诉id
     */
    private String complaintId;
    /**
     * 处理类型
     */
    private Integer handleType;
    /**
     * 处理备注
     */
    private String remark;
    /**
     * 当前处理部门code
     */
    private String deptCode;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
