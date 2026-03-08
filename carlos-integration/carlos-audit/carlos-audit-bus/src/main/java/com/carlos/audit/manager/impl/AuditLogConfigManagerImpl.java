package com.carlos.audit.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.audit.convert.AuditLogConfigConvert;
import com.carlos.audit.manager.AuditLogConfigManager;
import com.carlos.audit.mapper.AuditLogConfigMapper;
import com.carlos.audit.pojo.dto.AuditLogConfigDTO;
import com.carlos.audit.pojo.entity.AuditLogConfig;
import com.carlos.audit.pojo.param.AuditLogConfigPageParam;
import com.carlos.audit.pojo.vo.AuditLogConfigVO;
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
 * 审计日志配置（动态TTL与采样策略） 查询封装实现类
 * </p>
 *
 * @author Carlos
 * @date 2026年3月6日 下午9:31:12
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AuditLogConfigManagerImpl extends BaseServiceImpl<AuditLogConfigMapper, AuditLogConfig> implements AuditLogConfigManager {

    @Override
    public boolean add(AuditLogConfigDTO dto) {
        AuditLogConfig entity = AuditLogConfigConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'AuditLogConfig' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'AuditLogConfig' data: id:{}", entity.getId());
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
            log.warn("Remove 'AuditLogConfig' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'AuditLogConfig' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(AuditLogConfigDTO dto) {
        AuditLogConfig entity = AuditLogConfigConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'AuditLogConfig' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'AuditLogConfig' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public AuditLogConfigDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        AuditLogConfig entity = getBaseMapper().selectById(id);
        return AuditLogConfigConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<AuditLogConfigVO> getPage(AuditLogConfigPageParam param) {
        LambdaQueryWrapper<AuditLogConfig> wrapper = queryWrapper();
        wrapper.select(

            AuditLogConfig::getId,
            AuditLogConfig::getLogType,
            AuditLogConfig::getRetentionDays,
            AuditLogConfig::getSamplingRate,
            AuditLogConfig::getAsyncWrite,
            AuditLogConfig::getStoreDataChange,
            AuditLogConfig::getStoreTechnical,
            AuditLogConfig::getTenantId,
            AuditLogConfig::getCreateBy,
            AuditLogConfig::getCreateTime,
            AuditLogConfig::getUpdateBy,
            AuditLogConfig::getUpdateTime
        );
        PageInfo<AuditLogConfig> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, AuditLogConfigConvert.INSTANCE::toVO);
    }

}
