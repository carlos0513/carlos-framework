package com.carlos.audit.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.audit.convert.AuditLogTechnicalConvert;
import com.carlos.audit.manager.AuditLogTechnicalManager;
import com.carlos.audit.mapper.AuditLogTechnicalMapper;
import com.carlos.audit.pojo.dto.AuditLogTechnicalDTO;
import com.carlos.audit.pojo.entity.AuditLogTechnical;
import com.carlos.audit.pojo.param.AuditLogTechnicalPageParam;
import com.carlos.audit.pojo.vo.AuditLogTechnicalVO;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseServiceImpl;
import com.carlos.datasource.pagination.MybatisPage;
import com.carlos.datasource.pagination.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * <p>
 * 审计日志-技术上下文 查询封装实现类
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AuditLogTechnicalManagerImpl extends BaseServiceImpl<AuditLogTechnicalMapper, AuditLogTechnical> implements AuditLogTechnicalManager {

    @Override
    public boolean add(AuditLogTechnicalDTO dto) {
        AuditLogTechnical entity = AuditLogTechnicalConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'AuditLogTechnical' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'AuditLogTechnical' data: id:{}", entity.getId());
        }
        return true;
    }

    @Override
    public boolean delete(Serializable id) {
        if (id == null) {
            log.warn("id can't be null");
            return false;
        }
        boolean success = removeById(id);
        if (!success) {
            log.warn("Remove 'AuditLogTechnical' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'AuditLogTechnical' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(AuditLogTechnicalDTO dto) {
        AuditLogTechnical entity = AuditLogTechnicalConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'AuditLogTechnical' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'AuditLogTechnical' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public AuditLogTechnicalDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        AuditLogTechnical entity = getBaseMapper().selectById(id);
        return AuditLogTechnicalConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<AuditLogTechnicalVO> getPage(AuditLogTechnicalPageParam param) {
        LambdaQueryWrapper<AuditLogTechnical> wrapper = queryWrapper();
        wrapper.select(

            AuditLogTechnical::getId,
            AuditLogTechnical::getAuditLogId,
            AuditLogTechnical::getTraceId,
            AuditLogTechnical::getSpanId,
            AuditLogTechnical::getParentSpanId,
            AuditLogTechnical::getTracePath,
            AuditLogTechnical::getDbQueryCount,
            AuditLogTechnical::getDbQueryTimeMs,
            AuditLogTechnical::getExternalCallCount,
            AuditLogTechnical::getExternalCallTimeMs,
            AuditLogTechnical::getCustomMetrics,
            AuditLogTechnical::getRequestPayloadRef,
            AuditLogTechnical::getResponsePayloadRef,
            AuditLogTechnical::getPayloadStorageType,
            AuditLogTechnical::getAppName,
            AuditLogTechnical::getAppVersion,
            AuditLogTechnical::getCluster,
            AuditLogTechnical::getHostName,
            AuditLogTechnical::getCreatedTime
        );
        PageInfo<AuditLogTechnical> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, AuditLogTechnicalConvert.INSTANCE::toVO);
    }

}
