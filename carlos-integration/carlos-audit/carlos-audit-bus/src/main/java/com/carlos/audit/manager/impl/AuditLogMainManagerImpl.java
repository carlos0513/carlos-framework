package com.carlos.audit.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.audit.convert.AuditLogMainConvert;
import com.carlos.audit.manager.AuditLogMainManager;
import com.carlos.audit.mapper.AuditLogMainMapper;
import com.carlos.audit.pojo.dto.AuditLogMainDTO;
import com.carlos.audit.pojo.entity.AuditLogMain;
import com.carlos.audit.pojo.param.AuditLogMainPageParam;
import com.carlos.audit.pojo.vo.AuditLogMainVO;
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
 * 审计日志主表 查询封装实现类
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AuditLogMainManagerImpl extends BaseServiceImpl<AuditLogMainMapper, AuditLogMain> implements AuditLogMainManager {

    @Override
    public boolean add(AuditLogMainDTO dto) {
        AuditLogMain entity = AuditLogMainConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'AuditLogMain' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'AuditLogMain' data: id:{}", entity.getId());
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
            log.warn("Remove 'AuditLogMain' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'AuditLogMain' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(AuditLogMainDTO dto) {
        AuditLogMain entity = AuditLogMainConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'AuditLogMain' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'AuditLogMain' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public AuditLogMainDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        AuditLogMain entity = getBaseMapper().selectById(id);
        return AuditLogMainConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<AuditLogMainVO> getPage(AuditLogMainPageParam param) {
        LambdaQueryWrapper<AuditLogMain> wrapper = queryWrapper();
        wrapper.select(

            AuditLogMain::getId,
            AuditLogMain::getLogSchemaVersion,
            AuditLogMain::getCategory,
            AuditLogMain::getLogType,
            AuditLogMain::getServerTime,
            AuditLogMain::getClientTime,
            AuditLogMain::getEventTime,
            AuditLogMain::getDurationMs,
            AuditLogMain::getRetentionDeadline,
            AuditLogMain::getPrincipalId,
            AuditLogMain::getPrincipalType,
            AuditLogMain::getPrincipalName,
            AuditLogMain::getTenantId,
            AuditLogMain::getDeptId,
            AuditLogMain::getDeptName,
            AuditLogMain::getDeptPath,
            AuditLogMain::getTargetType,
            AuditLogMain::getTargetId,
            AuditLogMain::getTargetName,
            AuditLogMain::getTargetSnapshot,
            AuditLogMain::getState,
            AuditLogMain::getResultCode,
            AuditLogMain::getResultMessage,
            AuditLogMain::getOperation,
            AuditLogMain::getRiskLevel,
            AuditLogMain::getClientIp,
            AuditLogMain::getClientPort,
            AuditLogMain::getServerIp,
            AuditLogMain::getUserAgent,
            AuditLogMain::getDeviceFingerprint,
            AuditLogMain::getLocationCountry,
            AuditLogMain::getLocationProvince,
            AuditLogMain::getLocationCity,
            AuditLogMain::getLocationLat,
            AuditLogMain::getLocationLon,
            AuditLogMain::getAuthType,
            AuditLogMain::getAuthProvider,
            AuditLogMain::getRoles,
            AuditLogMain::getPermissions,
            AuditLogMain::getBizChannel,
            AuditLogMain::getBizScene,
            AuditLogMain::getBizOrderNo,
            AuditLogMain::getRelatedBizIds,
            AuditLogMain::getMonetaryAmount,
            AuditLogMain::getBatchId,
            AuditLogMain::getBatchIndex,
            AuditLogMain::getBatchTotal,
            AuditLogMain::getProcessId,
            AuditLogMain::getTaskId,
            AuditLogMain::getApproverId,
            AuditLogMain::getApprovalComment,
            AuditLogMain::getHasDataChange,
            AuditLogMain::getEntityClass,
            AuditLogMain::getTableName,
            AuditLogMain::getChangeSummary,
            AuditLogMain::getTechnicalContextId,
            AuditLogMain::getDynamicTags,
            AuditLogMain::getDynamicExtras,
            AuditLogMain::getCreatedTime,
            AuditLogMain::getUpdatedTime
        );
        PageInfo<AuditLogMain> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, AuditLogMainConvert.INSTANCE::toVO);
    }

}
