package com.carlos.audit.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.audit.convert.AuditLogDataChangeConvert;
import com.carlos.audit.manager.AuditLogDataChangeManager;
import com.carlos.audit.mapper.AuditLogDataChangeMapper;
import com.carlos.audit.pojo.dto.AuditLogDataChangeDTO;
import com.carlos.audit.pojo.entity.AuditLogDataChange;
import com.carlos.audit.pojo.param.AuditLogDataChangePageParam;
import com.carlos.audit.pojo.vo.AuditLogDataChangeVO;
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
 * 审计日志-数据变更详情 查询封装实现类
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AuditLogDataChangeManagerImpl extends BaseServiceImpl<AuditLogDataChangeMapper, AuditLogDataChange> implements AuditLogDataChangeManager {

    @Override
    public boolean add(AuditLogDataChangeDTO dto) {
        AuditLogDataChange entity = AuditLogDataChangeConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'AuditLogDataChange' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'AuditLogDataChange' data: id:{}", entity.getId());
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
            log.warn("Remove 'AuditLogDataChange' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'AuditLogDataChange' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(AuditLogDataChangeDTO dto) {
        AuditLogDataChange entity = AuditLogDataChangeConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'AuditLogDataChange' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'AuditLogDataChange' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public AuditLogDataChangeDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        AuditLogDataChange entity = getBaseMapper().selectById(id);
        return AuditLogDataChangeConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<AuditLogDataChangeVO> getPage(AuditLogDataChangePageParam param) {
        LambdaQueryWrapper<AuditLogDataChange> wrapper = queryWrapper();
        wrapper.select(

            AuditLogDataChange::getId,
            AuditLogDataChange::getAuditLogId,
            AuditLogDataChange::getOldData,
            AuditLogDataChange::getOldDataCompressed,
            AuditLogDataChange::getNewData,
            AuditLogDataChange::getNewDataCompressed,
            AuditLogDataChange::getChangedFieldCount,
            AuditLogDataChange::getCreatedTime
        );
        PageInfo<AuditLogDataChange> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, AuditLogDataChangeConvert.INSTANCE::toVO);
    }

}
