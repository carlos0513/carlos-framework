package com.carlos.audit.mapper;

import com.carlos.audit.pojo.entity.AuditLogDataChange;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * <p>
 * 审计日志-数据变更详情 查询接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Mapper
public interface AuditLogDataChangeMapper extends MPJBaseMapper<AuditLogDataChange> {


}
