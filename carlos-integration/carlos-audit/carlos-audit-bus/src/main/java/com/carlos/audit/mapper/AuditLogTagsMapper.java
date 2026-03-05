package com.carlos.audit.mapper;

import com.carlos.audit.pojo.entity.AuditLogTags;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * <p>
 * 审计日志-动态标签 查询接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Mapper
public interface AuditLogTagsMapper extends MPJBaseMapper<AuditLogTags> {


}
