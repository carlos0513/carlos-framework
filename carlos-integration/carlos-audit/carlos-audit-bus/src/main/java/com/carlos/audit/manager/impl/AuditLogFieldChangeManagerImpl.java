package com.carlos.audit.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.audit.convert.AuditLogFieldChangeConvert;
import com.carlos.audit.manager.AuditLogFieldChangeManager;
import com.carlos.audit.mapper.AuditLogFieldChangeMapper;
import com.carlos.audit.pojo.dto.AuditLogFieldChangeDTO;
import com.carlos.audit.pojo.entity.AuditLogFieldChange;
import com.carlos.audit.pojo.param.AuditLogFieldChangePageParam;
import com.carlos.audit.pojo.vo.AuditLogFieldChangeVO;
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
 * 审计日志-字段级变更明细 查询封装实现类
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AuditLogFieldChangeManagerImpl extends BaseServiceImpl<AuditLogFieldChangeMapper, AuditLogFieldChange> implements AuditLogFieldChangeManager {

    @Override
    public boolean add(AuditLogFieldChangeDTO dto) {
        AuditLogFieldChange entity = AuditLogFieldChangeConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'AuditLogFieldChange' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'AuditLogFieldChange' data: id:{}", entity.getId());
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
            log.warn("Remove 'AuditLogFieldChange' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'AuditLogFieldChange' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(AuditLogFieldChangeDTO dto) {
        AuditLogFieldChange entity = AuditLogFieldChangeConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'AuditLogFieldChange' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'AuditLogFieldChange' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public AuditLogFieldChangeDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        AuditLogFieldChange entity = getBaseMapper().selectById(id);
        return AuditLogFieldChangeConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<AuditLogFieldChangeVO> getPage(AuditLogFieldChangePageParam param) {
        LambdaQueryWrapper<AuditLogFieldChange> wrapper = queryWrapper();
        wrapper.select(

            AuditLogFieldChange::getId,
            AuditLogFieldChange::getAuditLogId,
            AuditLogFieldChange::getDataChangeId,
            AuditLogFieldChange::getFieldName,
            AuditLogFieldChange::getFieldDesc,
            AuditLogFieldChange::getChangeType,
            AuditLogFieldChange::getOldValue,
            AuditLogFieldChange::getOldValueType,
            AuditLogFieldChange::getNewValue,
            AuditLogFieldChange::getNewValueType,
            AuditLogFieldChange::getSensitive,
            AuditLogFieldChange::getCreatedTime
        );
        PageInfo<AuditLogFieldChange> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, AuditLogFieldChangeConvert.INSTANCE::toVO);
    }

}
