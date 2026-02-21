package com.yunjin.org.pojo.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 数据传输对象，service和manager向外传输对象
 * </p>
 *
 * @author yunjin
 * @date 2024-9-23 16:01:35
 */
@Data
@Accessors(chain = true)
public class OrgComplaintReportDTO {
    /**
     * 主键
     */
    private String id;
    /**
     * 投诉来源
     */
    private Integer complaintSource;
    /**
     * 任务来源
     */
    private Integer taskSource;
    /**
     * 任务来源系统
     */
    private String taskSys;
    /**
     * 投诉任务
     */
    private String complaintTask;
    /**
     * 投诉表单
     */
    private String complaintForm;
    /**
     * 投诉类型
     */
    private Integer complaintType;
    /**
     * 投诉原因
     */
    private String reason;
    /**
     * 投诉部门
     */
    private String formDept;
    /**
     * 投诉状态
     */
    private Integer status;
    /**
     * 投诉反馈
     */
    private String reply;
    /**
     * 处理层级
     */
    private Integer handleLevel;
    /**
     * 投诉截图
     */
    private String pictures;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 标识
     */
    private String sgin;
}
